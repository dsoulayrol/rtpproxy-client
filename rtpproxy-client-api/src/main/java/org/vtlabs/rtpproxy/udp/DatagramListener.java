/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.udp;

import java.net.InetSocketAddress;

/**
 *
 * @author mhack
 */
public interface DatagramListener {
    public void processResponse(String cookie, String message, InetSocketAddress srcAddr);
}
