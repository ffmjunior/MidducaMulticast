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
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Handle connetions on reciever side.
 *
 * @author ffmj
 */
public class ServerRequestHandler extends Thread {

    private Marshaller marshaller;
    private Invoker invoker;
    private int portaIn = 12319;
    private int portaOut = 12318;
    private String ipIn = "239.0.0.1";
    private String ipOut = "238.0.0.1";

    public ServerRequestHandler() {
        marshaller = new Marshaller();
        invoker = new Invoker();
        this.run();
    }

    @Override
    public void run() {
        receber();
    }

    /**
     * Starts main loop on server
     */
    public void receber() {
        receberMulticast();
    }

    /**
     * Server waits for datagrams ate a specified multicast channel. The server
     * starts a thead for eachFor each received datagram through
     * {@link ConnectionMulticastHandler}
     */
    public void receberMulticast() {
        try {
            while (true) {
                MulticastSocket mcs = new MulticastSocket(portaIn);
                InetAddress grp = InetAddress.getByName(ipIn);
                mcs.joinGroup(grp);
                byte rec[] = new byte[1024];
                DatagramPacket pkg = new DatagramPacket(rec, rec.length);
                mcs.receive(pkg);
                String ip = pkg.getAddress().getHostAddress();
                String received = new String(pkg.getData());
                received = marshaller.cleanMsg(received);
                int porta = pkg.getPort();
                Thread rf = new ConnectionMulticastHandler(received, ip, porta, invoker);
                rf.start();
            }
        } catch (Exception ex) {
            System.out.println("Erro SRH");
            System.out.println(ex.getMessage());
            System.exit(0);
        }

    }

}
