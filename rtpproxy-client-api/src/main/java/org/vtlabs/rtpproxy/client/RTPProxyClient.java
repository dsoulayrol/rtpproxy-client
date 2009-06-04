package org.vtlabs.rtpproxy.client;

import java.io.IOException;
import org.vtlabs.rtpproxy.command.CommandTimeoutManager;
import org.vtlabs.rtpproxy.callback.CallbackHandler;
import org.vtlabs.rtpproxy.udp.DatagramListener;
import org.vtlabs.rtpproxy.udp.DatagramService;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class RTPProxyClient {

    /**
     * Default bind port used by UDP service if it isn't specified.
     */
    public static final int DEFAULT_BIND_PORT = 9876;

    private CommandTimeoutManager commandTimeout;
    private DatagramService udpService;
    private CallbackHandler callbackHandler;

    public RTPProxyClient() throws IOException {
        this(DEFAULT_BIND_PORT);
    }

    public RTPProxyClient(int bindPort) throws IOException {
        commandTimeout = createCommandTimeoutManager();
        callbackHandler = createCallbackHandler(commandTimeout);
        udpService = createDatagraService(bindPort, callbackHandler);
    }

    /**
     * Asssincronously create a new RTPProxy session. The listener will be
     * notified through on of RTPProxyClientListener callback methods.
     *
     * @param Application data object, will be passed as argument to callback
     *        method.
     * @param Listener o receive callback events.
     * @see RTPProxyClientListener
     */
    public void createSession(Object appData, RTPProxyClientListener listener) {
    }
    
    /**
     * Factory method to create CommandManager.
     *
     * @return
     */
    protected CommandTimeoutManager createCommandTimeoutManager() {
        return new CommandTimeoutManager();
    }

    /**
     * Factory method to create DatagramService.
     *
     * @return
     */
    protected DatagramService createDatagraService(int bindPort,
            DatagramListener listener) throws IOException {
        return new DatagramService(bindPort, listener);
    }
    
    /**
     * Factory method to create RTPClientResponseHandler.
     *
     * @return
     */
    protected CallbackHandler createCallbackHandler(
            CommandTimeoutManager commandManager) {
        return new CallbackHandler(commandManager);
    }
}
