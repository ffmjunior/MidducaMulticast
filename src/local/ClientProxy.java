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

import remote.ServerRequestHandler;

/**
 * Connects any class to the midleware and starts exchangind objects. Demands a 
 * running {@link ServerRequestHandler} to work with.
 * @author ffmj
 */
public class ClientProxy {

    Requestor rq = new Requestor();

    /**
     * Send a template object to be matched in space server and returns a copy
     * of matched object. A template is a object that contains no values assign
     * to its variable (null for all) or at least one null valued field.
     *
     * @param o The template object to me matched.
     * @return A copy of the object after matching process or the same object if
     * there ir no match object in space.
     */
    public Object read(Object o) {
        Object reply = null;
        reply = rq.request(o, rq.READ);
        return reply;
    }

    /**
     * Sends a object to Space Server.
     *
     * @param o Any Object to be sent by application to space.
     * @return The same obect that was sent. Can be used by application to
     * verify the stoge.
     */
    public Object write(Object o) {
        Object reply = null;
        reply = rq.request(o, rq.WRITE);
        return reply;
    }

    /**
     * Send a object to be matched in server and takes template off space.
     *
     * @param o The object to me matched in space.
     * @return The object itself or objects itselves to be handled by
     * application. The objects is taken off space server. If no object matches
     * template the same object is returned.
     */
    public Object take(Object o) {
        Object reply = null;
        reply = (Object) rq.request(o, rq.TAKE);
        return reply;
    }

}
