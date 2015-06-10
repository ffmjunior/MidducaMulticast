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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Handle any new received datagrams.
 *
 * @author junior
 */
public class ConnectionMulticastHandler extends Thread {

    private Marshaller marshaller;
    private Invoker invoker;
    private int portOut = 12318;
    private String ipOut = "238.0.0.1";
    String received;
    int port;
    String ip;

    /**
     * Starts the a connection thread that deal with a message and reply.
     */
    @Override
    public void run() {
        treat();
    }

    /**
     * Any new Hanldler receive a messagem from {@link ServerRequestHandler} to
     * provide a reply.
     *
     * @param received The message itself
     * @param ip The ip from sender. Used to reply
     * @param port The port from sender. Used to reply.
     * @param i A {@link Invoker}
     */
    ConnectionMulticastHandler(String received, String ip, int port, Invoker i) {
        invoker = i;
        this.received = received;
        this.port = port;
        this.ip = ip;
    }

    /**
     * Reply a message if there's a answer from {@link Invoker}
     */
    public void treat() {
        String ret = (String) invoker.treat(received);
        try {
            boolean equalsIgnoreCase = ret.equalsIgnoreCase("null");
            sendMulticast(ret, ip, port);
        } catch (Exception e) {
            System.out.println("SRF - No answer");
        }
    }

    /**
     * Sends a reply from a request.
     *
     * @param o The reply
     * @param ip The requester's ip
     * @param port The requester's port
     */
    public void sendMulticast(Object o, String ip, int port) {
        try {
            String m = (String) o;
            byte[] b = m.getBytes();
            InetAddress addr = InetAddress.getByName(ip);
            DatagramSocket ds = new DatagramSocket(0);
            DatagramPacket pkg = new DatagramPacket(b, b.length, addr, port); //lonlon
            ds.send(pkg);
        } catch (Exception e) {
        }
    }

}
