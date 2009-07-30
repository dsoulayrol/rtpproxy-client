/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.udp;

import java.net.InetSocketAddress;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vtlabs.rtpproxy.message.ResponseMessage;

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
    public void messageReceived(IoSession session, Object objectMessage)
            throws Exception {

        try {
            ResponseMessage message = ResponseMessage.parseMessage(
                    objectMessage.toString(),
                    (InetSocketAddress) session.getRemoteAddress());

            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Message received ");
                sb.append(message);
                log.debug(sb.toString());
            }

            listener.processResponse(message);

        } catch (Exception e) {
            log.error("Error processing response message", e);
        }
    }
}
