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
        init();
    }

    public void init() throws IOException {
        ByteBuffer.setUseDirectBuffers(false);
        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());

        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        //chain.addLast("logger", new LoggingFilter());

        TextLineCodecFactory codecFactory = new TextLineCodecFactory(Charset.forName("ASCII"));
        ProtocolCodecFilter codecFilter = new ProtocolCodecFilter(codecFactory);
        chain.addLast("codec", codecFilter);

        DatagramAcceptorConfig acceptorConfig = acceptor.getDefaultConfig();
        acceptorConfig.setDisconnectOnUnbind(true);
        DatagramSessionConfig sessionConfig = acceptorConfig.getSessionConfig();
        sessionConfig.setReuseAddress(true);

        acceptor.bind(new InetSocketAddress(bindPort), handler);
    }

    public void stop() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void send(String cookie, String message, InetSocketAddress dstAddr) {
        InetSocketAddress localAddr = new InetSocketAddress(bindPort);
        IoSession session = acceptor.newSession(dstAddr, localAddr);

        // Create message in the format "COOKIE MESSAGE"
        StringBuilder sbMessage = new StringBuilder(cookie);
        sbMessage.append(" ").append(message);

        session.write(sbMessage.toString());
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
    private DatagramHandler createDatagramHandler(DatagramListener listener) {
        return new DatagramHandler(listener);
    }
}
