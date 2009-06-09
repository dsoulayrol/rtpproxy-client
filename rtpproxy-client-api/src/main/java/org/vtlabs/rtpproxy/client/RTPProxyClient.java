package org.vtlabs.rtpproxy.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.vtlabs.rtpproxy.command.CommandTimeoutManager;
import org.vtlabs.rtpproxy.callback.CallbackHandler;
import org.vtlabs.rtpproxy.command.UpdateCommand;
import org.vtlabs.rtpproxy.udp.DatagramListener;
import org.vtlabs.rtpproxy.udp.DatagramService;

/**
 * RTPProxy client API facade class.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class RTPProxyClient {

    protected CommandTimeoutManager commandTimeout;
    protected DatagramService udpService;
    protected CallbackHandler callbackHandler;
    protected ScheduledThreadPoolExecutor executor;
    protected RTPProxyClientConfig config;

    public RTPProxyClient(RTPProxyClientConfig config)
            throws IOException, RTPProxyClientConfigException {
        this.config = config;
        executor = createThreadPoolExecutor(config.getPoolSize());
        commandTimeout = createCommandTimeoutManager(executor, config.getCommandTimeout());
        callbackHandler = createCallbackHandler(commandTimeout);
        udpService = createDatagraService(config.getBindPort(), callbackHandler);
    }

    /**
     * Asynchronously create a new RTPProxy session filled only with the Callee
     * media address.
     * 
     * To create the Caller media address of an existing session use the method
     * updateSession().
     *
     * @param String to be used as session ID.
     * @param Application data object, will be passed as argument in the
     *        callback method.
     * @param Listener that will receive the callback event.
     * @see RTPProxyClientListener
     */
    public void createSession(String sessionID, Object appData,
            RTPProxyClientListener listener) throws NoServerAvailableException {
        UpdateCommand updateCmd = new UpdateCommand(callbackHandler);
        updateCmd.setSessionID(sessionID);

        // No matter the fromtag content since it matchs that used in the
        // updateSession() method below to link the callee and caller session in
        // the RTPProxy.
        updateCmd.setFromTag("fromtag");

        String cookie = updateCmd.getCookie();
        String message = updateCmd.getMessage();
        InetSocketAddress serverAddr = getServer().getAddress();

        commandTimeout.addPendingCommand(updateCmd);
        udpService.send(cookie, message, serverAddr);
    }

    /**
     * Asynchronously create the Caller media address of an existing session.
     *
     * @param session to be updated
     * @param Application data object, will be passed as argument in the
     *        callback method.
     * @param Listener that will receive the callback event.
     */
    public void updateSession(RTPProxySession session, Object appData,
            RTPProxyClientListener listener) throws NoServerAvailableException {
        UpdateCommand updateCmd = new UpdateCommand(session, callbackHandler);

        // No matter the fromtag and totag content since it matchs that used in
        // the createSession() method above to link the callee and caller
        // session in the RTPProxy.
        updateCmd.setFromTag("totag");
        updateCmd.setToTag("fromtag");

        String cookie = updateCmd.getCookie();
        String message = updateCmd.getMessage();
        InetSocketAddress serverAddr = session.getServer().getAddress();

        commandTimeout.addPendingCommand(updateCmd);
        udpService.send(cookie, message, serverAddr);
    }

    /**
     * Asynchronously destroy the given RTPProxySession releasing all resources
     * in the RTPProxy server.
     *
     * @param Session to be destroyed.
     */
    public void destroySession(RTPProxySession session, Object appData,
            RTPProxyClientListener listener) throws NoServerAvailableException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * Get RTPProxyClient configuration.
     *
     * @return Client configuration.
     */
    public RTPProxyClientConfig getConfig() {
        return config;
    }

    /**
     * Get the next avaiable RTPProxy server to be used.
     *
     * @return
     * @throws org.vtlabs.rtpproxy.client.NoServerAvailableException if there
     *         aren't servers available.
     */
    protected RTPProxyServer getServer() throws NoServerAvailableException {
        // TODO [marcoshack] RTPProxy servers load balance algorithm
        List<RTPProxyServer> serverList = config.getServerList();

        if (serverList.size() > 0) {
            return serverList.get(0);
        } else {
            throw new NoServerAvailableException("Server list is empty");
        }
    }
    
    /**
     * Factory method to create CommandManager.
     *
     * @return
     */
    protected CommandTimeoutManager createCommandTimeoutManager(
            ScheduledThreadPoolExecutor executor, long commandTimeout) {
        return new CommandTimeoutManager(executor, commandTimeout);
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

    /**
     * Factory method to create ScheduledThreadPoolExecutor.
     *
     * @return
     */
    protected ScheduledThreadPoolExecutor createThreadPoolExecutor(
            int poolSize) {
        return new ScheduledThreadPoolExecutor(poolSize);
    }
}
