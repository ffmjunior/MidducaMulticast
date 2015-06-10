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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Client-side connection management, threading, and result dispatching need to
 * managed in a coordinated and optimized fashion.
 *
 * @author ffmj
 */
public class ClientRequestHandler {

    public ClientRequestHandler() {
        System.out.println("ClientRequestHandler criado!");
        marshaller = new Marshaller();
    }

    private Marshaller marshaller;
    private int portaIn = 12318;
    private int portaOut = 12319;
    private String ipIn = "238.0.0.1";
    private String ipOut = "239.0.0.1";
    private DatagramSocket datagramSocket;

    public Object send(Object o) {
        Object reply = null;
        try {
            String msg = (String) o;
            InetAddress addr = InetAddress.getByName(ipOut);
            datagramSocket = new DatagramSocket(0);
            byte[] b = msg.getBytes();
            DatagramPacket pkg = new DatagramPacket(b, b.length, addr, portaOut); //lonlon
            datagramSocket.send(pkg);
            //System.out.println("CRH Enviado: " + new String(pkg.getData()));            
        } catch (Exception e) {
            System.out.println("Nao foi possivel enviar a mensagem");
        }
        return reply;
    }

    /**
     * Send a object to read or take operations.
     *
     * @param o Object to be sent
     * @return
     */
    public Object sendNeedReturn(Object o) {
        Object reply = null;
        try {
            send(o);
            reply = receiveUnicast();
        } catch (Exception e) {
            System.out.println("Nao foi possivel enviar a mensagem");
        }
        return reply;
    }

    /**
     *  Send a object to operations that do not need reply (write).
     *
     * @param o Object to be sent
     * @return null
     */
    public Object sendNoReturn(Object o) {
        send(o);
        return null;
    }

    /**
     * Waits reply from read or take
     *
     * @return A reply
     */
    private Object receiveUnicast() {
        Object ret = null;
        try {
            byte rec[] = new byte[1024];
            DatagramPacket pkg = new DatagramPacket(rec, rec.length);
            //System.out.println("RECEBENDO...");
            datagramSocket.setSoTimeout(1000);
            datagramSocket.receive(pkg);
            ret = new String(pkg.getData());
            ret = marshaller.cleanMsg((String) ret);
            //System.out.println("RECEBIDO..." + ret);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return ret;
    }
}
