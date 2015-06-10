/*
 * Copyright (C) 2015 junior
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package remote;

import comum.Marshaller;

/**
 * Commands {@link Remote} on what operations to do with Space's data.
 *
 * @author ffmj
 */
public class Invoker {

    private Remote remoto;
    private Marshaller marshaller;

    Invoker() {
        remoto = new Remote();
        marshaller = new Marshaller();
    }

    /**
     * Send Json object to correct operation.
     *
     * @param o A received object
     * @return The reply
     */
    Object treat(Object o) {
        Object reply = null;
        String operation = marshaller.getJsonOperation((String) o);
        o = marshaller.removeOperationLetter((String) o);
        if (operation.equalsIgnoreCase("w")) {
            remoto.write(o);
        }
        if (operation.equalsIgnoreCase("r")) {
            reply = remoto.read(o);
        }
        if (operation.equalsIgnoreCase("t")) {
            reply = remoto.take(o);
        }
        return reply;
    }

}
