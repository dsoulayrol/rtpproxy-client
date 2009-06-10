package org.vtlabs.rtpproxy.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.DatagramAcceptor;
import org.apache.mina.transport.socket.nio.DatagramAcceptorConfig;
import org.apache.mina.transport.socket.nio.DatagramSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RTPProxyClient Datagram (UDP) transport services.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class DatagramService {

    private Logger log = LoggerFactory.getLogger(DatagramService.class);
    private DatagramAcceptor acceptor;
    private DatagramHandler handler;
    private int bindPort;

    public DatagramService(int bindPort, DatagramListener listener)
            throws IOException {
        acceptor = createDatagramAcceptor();
        handler = createDatagramHandler(listener);
        this.bindPort = bindPort;
        start();
    }

    /**
     * Stop DatagramService and release all used resources.
     *
     * @throws java.io.IOException
     */
    public void stop() throws IOException {
        acceptor.unbindAll();
    }

    /**
     * Initialize and bind DatagramService to the socket port informed in the
     * constructor.
     *
     * @throws java.io.IOException
     */
    protected void start() throws IOException {
        ByteBuffer.setUseDirectBuffers(false);
        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());

        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        //chain.addLast("logger", new LoggingFilter());

        TextLineCodecFactory codecFactory =
                new TextLineCodecFactory(Charset.forName("ASCII"));
        ProtocolCodecFilter codecFilter = new ProtocolCodecFilter(codecFactory);
        chain.addLast("codec", codecFilter);

        DatagramAcceptorConfig acceptorConfig = acceptor.getDefaultConfig();
        acceptorConfig.setDisconnectOnUnbind(true);
        DatagramSessionConfig sessionConfig = acceptorConfig.getSessionConfig();
        sessionConfig.setReuseAddress(true);

        bind(new InetSocketAddress(bindPort), handler);
    }


    public void send(String cookie, String message, InetSocketAddress dstAddr) {
        InetSocketAddress localAddr = new InetSocketAddress(bindPort);
        IoSession session = acceptor.newSession(dstAddr, localAddr);

        // Create message in the format "COOKIE MESSAGE"
        StringBuilder sbMessage = new StringBuilder(cookie);
        sbMessage.append(" ").append(message);
        send(sbMessage.toString(), session);
    }

    /**
     * Bind DatagramService to the given bindAddr and configure handler as the
     * IoHandler of the DatagramAcceptor.
     * 
     * @param bindAddr
     * @param handler
     * @throws java.io.IOException
     */
    protected void bind(InetSocketAddress bindAddr, DatagramHandler handler)
            throws IOException {
        acceptor.bind(bindAddr, handler);
    }

    /**
     * Send text message using the given IoSession.
     *
     * This method is just to get easy to unit test DatagramService class as
     * we're unable to MOCK DatagramAcceptor class.
     * 
     * @param message
     * @param session
     */
    protected void send(String message, IoSession session) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Sending message \'");
            sb.append(message);
            sb.append("\' to ").append(session.getRemoteAddress());
            log.debug(sb.toString());
        }
        
        session.write(message);
        session.close();
    }

    /**
     * Factory method to create the DatagramAcceptor.
     *
     * @return DatagramAcceptor
     */
    protected DatagramAcceptor createDatagramAcceptor() {
        return new DatagramAcceptor();
    }

    /**
     * Factory method to create the DatagramHandler to handle the
     * DatagramAcceptor IO events and notity the DatagramListener using its
     * callback methods.
     *
     * @param Listener to received RTPProxy server callback events.
     * @return DatagramHandler
     */
    protected DatagramHandler createDatagramHandler(DatagramListener listener) {
        return new DatagramHandler(listener);
    }
}
