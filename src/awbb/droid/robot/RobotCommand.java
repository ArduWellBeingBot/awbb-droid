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
package awbb.droid.robot;

/**
 * Robot command.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public enum RobotCommand {

    /** */
    None(null),
    /** */
    Connect(null),
    /** */
    Disconnect(null),
    /** */
    Init("I"),
    /** */
    Start("A"),
    /** */
    Stop("S"),
    /** */
    Download("G"),
    /** */
    Count("C"),
    /** Reinit robot data file cursor position to the beginning. */
    Reinit("R"),
    /** */
    Acquire("O");

    private String identifier;

    /**
     * Constructor.
     * 
     * @param identifier
     */
    private RobotCommand(String identifier) {
        this.identifier = identifier;
    }

    /**
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

}
