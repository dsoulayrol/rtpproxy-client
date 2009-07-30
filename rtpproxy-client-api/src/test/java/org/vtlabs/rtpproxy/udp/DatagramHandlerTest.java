package org.vtlabs.rtpproxy.udp;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.junit.Test;
import org.vtlabs.rtpproxy.BaseTest;
import org.vtlabs.rtpproxy.message.ResponseMessage;
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

        assertEquals("Invalid cookie", cookie, listener.message.getCookie());
        assertEquals("Invalid message", message, listener.message.getMessageLine());
        assertEquals("Invalid source address", srcAddr, listener.message.getSrcAddr());
    }

    protected class DatagramListenerMOCK implements DatagramListener {
        public ResponseMessage message;

        public void processResponse(ResponseMessage message) {
            this.message = message;
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
