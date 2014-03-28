/**
 * AWBB Droid - Android manager for AWBB.
 * 
 * Copyright (c) 2014 Benoit Garrigues <bgarrigues@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package awbb.droid.data.viz;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import awbb.droid.R;
import awbb.droid.bm.History;
import awbb.droid.bm.Location;
import awbb.droid.bm.SensorData;
import awbb.droid.dao.DatabaseDataSource;
import awbb.droid.dao.HistoryDao;
import awbb.droid.dao.LocationDao;
import awbb.droid.dao.SensorDataDao;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

/**
 * Graph activity.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class GraphActivity extends Activity {

    public static final String EXTRA_HISTORY_ID = "org.awbb.HISTORY_ID";

    private static final String TAG = GraphActivity.class.getSimpleName();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    // http://android-graphview.org/

    private LinearLayout graphLayout;
    private TextView locationText;
    private TextView beginDateText;
    private TextView endDateText;
    private Spinner dataSpinner;

    private GraphView graphView;

    private History history;
    private List<SensorData> dataList;

    /**
     * Constructor.
     */
    public GraphActivity() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load view
        setContentView(R.layout.activity_graph);

        graphLayout = (LinearLayout) findViewById(R.id.graph);
        locationText = (TextView) findViewById(R.id.graphLocationText);
        beginDateText = (TextView) findViewById(R.id.graphBeginDateText);
        endDateText = (TextView) findViewById(R.id.graphEndDateText);
        dataSpinner = (Spinner) findViewById(R.id.graphDataSpinner);

        // get data from intent
        Intent intent = getIntent();
        long historyId = intent.getLongExtra(EXTRA_HISTORY_ID, -1);

        Log.d(TAG, "onCreate historyId=" + historyId);

        // data source
        DatabaseDataSource.create(this);
        DatabaseDataSource.open();

        // get history and location
        history = HistoryDao.get(historyId);
        Location location = LocationDao.get(history.getLocationId());

        // title
        locationText.setText(location.getName());
        beginDateText.setText(DATE_FORMAT.format(history.getBegin()));
        endDateText.setText(DATE_FORMAT.format(history.getEnd()));

        // sensor spinner
        ArrayAdapter<GraphData> adapter = new ArrayAdapter<GraphData>(this, android.R.layout.simple_spinner_item,
                GraphData.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataSpinner.setAdapter(adapter);
        dataSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dataSpinner.setSelection(0);
            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long rowId) {
                refreshGraph();
            }

        });

        // get sensor data
        dataList = SensorDataDao.get(location, history.getBegin(), history.getEnd());

        Log.d(TAG, "onCreate dataList.size=" + dataList.size());

        refreshGraph();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        // data source
        DatabaseDataSource.open();

        super.onResume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause() {
        // data source
        DatabaseDataSource.close();

        super.onPause();
    }

    /**
     * Refresh the graph.
     */
    private void refreshGraph() {
        GraphData graphData = (GraphData) dataSpinner.getSelectedItem();

        Log.d(TAG, "refreshGraph graphData=" + graphData.name());

        if (graphView == null) {
            graphView = new LineGraphView(this, "");

            // graphView.setBackgroundColor(Color.BLACK);

            graphView.setScrollable(true);
            graphView.setScalable(true);
        }

        if (graphData == GraphData.All) {
            for (GraphData gd : GraphData.values()) {
                if (gd != GraphData.All) {
                    GraphViewSeries series = getGraphSeries(graphData);

                    graphView.removeAllSeries();
                    graphView.addSeries(series);
                }
            }

            graphView.setShowLegend(true);
        } else {
            GraphViewSeries series = getGraphSeries(graphData);

            graphView.removeAllSeries();
            graphView.addSeries(series);

            graphView.setShowLegend(false);
        }

        graphLayout.removeAllViews();
        graphLayout.addView(graphView);
    }

    /**
     * 
     * @param graphData
     * @return
     */
    private GraphViewSeries getGraphSeries(GraphData graphData) {
        // create graph line
        GraphViewDataInterface list[] = new GraphViewDataInterface[dataList.size()];

        double valueX = 0;
        double valueY = 0;
        int i = 0;
        for (SensorData data : dataList) {
            // number of minutes from the start of the history
            valueX = (data.getDate().getTime() - history.getBegin().getTime()) / 1000 / 60;

            switch (graphData) {
            case Temperature:
                valueY = data.getTemperature();
                break;

            case Humidity:
                valueY = data.getHumidity();
                break;

            case Co2:
                valueY = data.getCo2();
                break;

            case Light:
                valueY = data.getLight();
                break;

            case Sound:
                valueY = data.getSound();
                break;

            default:
                break;
            }

            list[i] = new GraphViewData(valueX, valueY);
            i++;
        }

        // create a graph series
        GraphViewSeries series = new GraphViewSeries(graphData.name(),
                new GraphViewSeriesStyle(graphData.getColor(), 3), list);

        return series;
    }

}
