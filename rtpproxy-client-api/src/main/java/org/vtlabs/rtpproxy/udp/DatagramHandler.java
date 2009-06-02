/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.udp;

import java.net.InetSocketAddress;
import org.apache.log4j.Logger;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

/**
 *
 * @author mhack
 */
public class DatagramHandler extends IoHandlerAdapter {

    private Logger log = Logger.getLogger(DatagramHandler.class);
    
    private DatagramListener listener;

    public DatagramHandler(DatagramListener listener) {
        this.listener = listener;
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable t)
            throws Exception {
    }

    @Override
    public void messageReceived(IoSession session, Object objectMessage)
            throws Exception {

        String message = objectMessage.toString();
        InetSocketAddress srcAddr = (InetSocketAddress) session.getRemoteAddress();

        
    }
}
