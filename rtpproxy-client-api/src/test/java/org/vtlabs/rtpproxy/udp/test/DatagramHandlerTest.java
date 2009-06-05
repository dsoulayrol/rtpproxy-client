package org.vtlabs.rtpproxy.udp.test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.junit.Test;
import org.vtlabs.rtpproxy.test.BaseTest;
import org.vtlabs.rtpproxy.udp.DatagramHandler;
import org.vtlabs.rtpproxy.udp.DatagramListener;
import static org.junit.Assert.*;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class DatagramHandlerTest extends BaseTest {

    @Test
    public void processResponse() throws Exception {
        DatagramListenerMOCK listener = new DatagramListenerMOCK();
        SocketAddress srcAddr = new InetSocketAddress("127.0.0.1", 9999);
        IoSessionMOCK session = new IoSessionMOCK(srcAddr);
        String cookie = "cookie";
        String message = "message";
        String messageLine = cookie + " " + message;

        DatagramHandler handler = new DatagramHandler(listener);
        handler.messageReceived(session, messageLine);

        assertEquals("Invalid cookie", cookie, listener.getCookie());
        assertEquals("Invalid message", message, listener.getMessage());
        assertEquals("Invalid source address", srcAddr, listener.getSrcAddr());
    }

    protected class DatagramListenerMOCK implements DatagramListener {
        String cookie;
        String message;
        InetSocketAddress srcAddr;

        public void processResponse(String cookie, String message,
                InetSocketAddress srcAddr) {
            this.cookie = cookie;
            this.message = message;
            this.srcAddr = srcAddr;
        }

        public String getCookie() {
            return cookie;
        }

        public String getMessage() {
            return message;
        }

        public InetSocketAddress getSrcAddr() {
            return srcAddr;
        }
    }

    protected class IoSessionMOCK extends IoSessionAdapter {
        private SocketAddress remoteadAddress;

        public IoSessionMOCK(SocketAddress remoteAddress) {
            this.remoteadAddress = remoteAddress;
        }
        
        @Override
        public SocketAddress getRemoteAddress() {
            return remoteadAddress;
        }
    }
}
