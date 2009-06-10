/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.test.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.mina.common.IoSession;
import org.junit.Test;
import org.vtlabs.rtpproxy.test.BaseTest;
import org.vtlabs.rtpproxy.udp.DatagramListener;
import org.vtlabs.rtpproxy.udp.DatagramService;
import static org.junit.Assert.*;

/**
 *
 * @author mhack
 */
public class DatagramServiceTest extends BaseTest {

    @Test
    public void send() throws Exception {
        DatagramListenerMOCK listener = new DatagramListenerMOCK();
        DatagramServiceMOCK service = new DatagramServiceMOCK(9998, listener);
        String message = "cookie message";
        InetSocketAddress dstAddr = new InetSocketAddress("localhost", 9999);
        service.send(message, dstAddr);

        assertEquals("Invalid sent message", message, service.messageSent);
    }

    protected class DatagramServiceMOCK extends DatagramService {

        public String messageSent;

        public DatagramServiceMOCK(int bindPort, DatagramListener listener)
                throws IOException {
            super(bindPort, listener);
        }
        
        @Override
        protected void send(String message, IoSession session) {
            messageSent = message;
        }
    }

    protected class DatagramListenerMOCK implements DatagramListener {

        public void processResponse(String cookie, String message,
                InetSocketAddress srcAddr) {
        }
    }
}
