/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.udp;

import java.net.InetSocketAddress;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mhack
 */
public class DatagramHandler extends IoHandlerAdapter {

    private Logger log = LoggerFactory.getLogger(DatagramHandler.class);
    
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

        String responseLine = objectMessage.toString();
        String arrCookieMessage[] = StringUtils.split(responseLine, " ", 2);
        String cookie = arrCookieMessage[0];
        String message = arrCookieMessage[1];
        InetSocketAddress srcAddr = (InetSocketAddress) session.getRemoteAddress();

        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Message received from ");
            sb.append(srcAddr);
            sb.append(": cookie = \'").append(cookie).append("\'");
            sb.append(", message = \'").append(message).append("\'");
            log.debug(sb.toString());
        }
        
        listener.processResponse(cookie, message, srcAddr);
    }
}
