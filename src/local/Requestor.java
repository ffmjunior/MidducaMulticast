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
package local;

import comum.Marshaller;

/**
 *
 * @author ffmj
 */
public class Requestor {

    public Requestor() {
        marshaller = new Marshaller();
        crh = new ClientRequestHandler();
    }

    Marshaller marshaller;
    ClientRequestHandler crh;

    public final String READ = "read";
    public final String WRITE = "write";
    public final String TAKE = "take";

    /**
     * Requests Client Request Handler to make some operations.
     *
     * @param o An Object to be sent to Space
     * @param operation A string value like read, write and take.
     *
     */
    public Object request(Object o, String operation) {
        Object reply = null;
        if (operation.equalsIgnoreCase("write")) {
            crh.sendNoReturn(marshaller.marshall(o, operation));
            reply = "null";
        } else {
            reply = crh.sendNeedReturn(marshaller.marshall(o, operation));
            reply = marshaller.unmarshall(o, (String) reply);
        }
        return reply;
    }

}
