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
package awbb.droid.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.view.WindowManager;
import android.widget.Toast;
import awbb.droid.R;

/**
 * Data transfert task.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public abstract class DataTransfertTask extends AsyncTask<Void, Integer, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataTransfertTask.class);

    private Context context;

    private PowerManager.WakeLock wakeLock;
    private ProgressDialog progressDialog;

    /**
     * Constructor.
     * 
     * @param context
     */
    public DataTransfertTask(Context context) {
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        wakeLock.acquire();

        // create the progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getText(R.string.robot_download_message));
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                DataTransfertTask.this.cancel(true);
            }
        });

        // disable sleep screen
        progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        progressDialog.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);

        LOGGER.debug("onProgressUpdate progress=" + progress);

        // if we get here, length is known, now set indeterminate to false
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(progress[1]);
        progressDialog.setProgress(progress[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(String result) {
        wakeLock.release();
        progressDialog.dismiss();

        // play a notification sound
        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(context, notificationUri);
        ringtone.play();

        // display a message
        if (result != null) {
            Toast.makeText(context, R.string.robot_download_error_message + result, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.robot_downloaded_message, Toast.LENGTH_SHORT).show();
        }
    }

}
