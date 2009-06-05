package org.vtlabs.rtpproxy.test.udp;

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
    public void messageReceived() throws Exception {
        DatagramListenerMOCK listener = new DatagramListenerMOCK();
        SocketAddress srcAddr = new InetSocketAddress("127.0.0.1", 9999);
        IoSessionMOCK session = new IoSessionMOCK(srcAddr);
        String cookie = "cookie";
        String message = "message";
        String messageLine = cookie + " " + message;

        DatagramHandler handler = new DatagramHandler(listener);
        handler.messageReceived(session, messageLine);

        assertEquals("Invalid cookie", cookie, listener.cookie);
        assertEquals("Invalid message", message, listener.message);
        assertEquals("Invalid source address", srcAddr, listener.srcAddr);
    }

    protected class DatagramListenerMOCK implements DatagramListener {
        public String cookie;
        public String message;
        public InetSocketAddress srcAddr;

        public void processResponse(String cookie, String message,
                InetSocketAddress srcAddr) {
            this.cookie = cookie;
            this.message = message;
            this.srcAddr = srcAddr;
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
