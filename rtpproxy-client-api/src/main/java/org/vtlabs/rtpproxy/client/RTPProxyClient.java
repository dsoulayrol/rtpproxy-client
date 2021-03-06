package org.vtlabs.rtpproxy.client;

import org.vtlabs.rtpproxy.exception.RTPProxyClientTerminatedException;
import org.vtlabs.rtpproxy.exception.RTPProxyClientConfigException;
import org.vtlabs.rtpproxy.config.RTPProxyClientConfig;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.vtlabs.rtpproxy.timeout.TimeoutManager;
import org.vtlabs.rtpproxy.callback.CallbackHandler;
import org.vtlabs.rtpproxy.command.Command;
import org.vtlabs.rtpproxy.command.CreateCommand;
import org.vtlabs.rtpproxy.command.DestroyCommand;
import org.vtlabs.rtpproxy.command.RecordCommand;
import org.vtlabs.rtpproxy.command.UpdateCommand;
import org.vtlabs.rtpproxy.exception.RTPProxyClientException;
import org.vtlabs.rtpproxy.scheduler.RTPProxyScheduler;
import org.vtlabs.rtpproxy.scheduler.RTPProxySchedulerFactory;
import org.vtlabs.rtpproxy.udp.DatagramListener;
import org.vtlabs.rtpproxy.udp.DatagramService;

/**
 * RTPProxy client API facade class.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class RTPProxyClient {

    /* The fromtag and totag values didn't matter since they are the same in
     * create and update commands to link the callee and caller session in the
     * RTPProxy server.
     */
    private static final String DEFAULT_FROMTAG = "fromtag";
    private static final String DEFAULT_TOTAG = "totag";
    protected TimeoutManager commandTimeout;
    protected DatagramService udpService;
    protected CallbackHandler callbackHandler;
    protected ScheduledThreadPoolExecutor executor;
    protected RTPProxyClientConfig config;
    protected RTPProxyScheduler scheduler;
    protected boolean isTerminated;

    public RTPProxyClient(RTPProxyClientConfig config)
            throws RTPProxyClientException {
        this.config = config;

        try {
            executor = createThreadPoolExecutor(config.getPoolSize());
            commandTimeout = createCommandTimeoutManager(executor, config.getCommandTimeout());
            callbackHandler = createCallbackHandler(commandTimeout);
            udpService = createDatagraService(config.getBindPort(), callbackHandler);
            scheduler = createScheduler(config.getSchedulerName(), config.getServerList());

        } catch (Exception e) {
            throw new RTPProxyClientException("Error starting RTPProxy-Client",
                    e);
        }
    }

    /**
     * Asynchronously create a new session containing the Callee media address.
     * This method doesn't pre-fill caller's address so RTPProxy server will
     * wait for caller's RTP packets before forwarding callee RTP packets to
     * him.
     * 
     * To create the Caller media address of an existing session use the method
     * updateSession().
     *
     * @param String to be used as session ID.
     * @param Application data object, will be passed as argument in the
     *        callback method, it isn't used internally.
     * @param Listener that will receive the callback events for this command.
     *        (see {@link RTPProxyClientListener} for more information about
     *        callback methods).
     */
    public void createSession(String sessionID, Object appData,
            RTPProxyClientListener listener) throws RTPProxyClientException {

        createSession(sessionID, null, appData, listener);
    }

    /**
     * Asynchronously create a new session containing the Callee media address
     * pre-filling caller's address with the given 'callerAddress'. That way the
     * RTPProxy servers doesn't need to wait caller's RTP packets to arrive
     * before start forwarding the callee's RTP packets to him.
     *
     * To create the Caller media address of an existing session use the method
     * updateSession().
     *
     * @param String to be used as session ID.
     * @param Caller's media address to be pre-filled in the new session
     * @param Application data object, will be passed as argument in the
     *        callback method, it isn't used internally.
     * @param Listener that will receive the callback events for this command.
     *        (see {@link RTPProxyClientListener} for more information about
     *        callback methods).
     */
    public void createSession(String sessionID, InetSocketAddress callerAddress,
            Object appData, RTPProxyClientListener listener)
            throws RTPProxyClientException {

        CreateCommand createCommand = new CreateCommand();
        createCommand.setSessionID(sessionID);
        createCommand.setListener(listener);
        createCommand.setAppData(appData);
        createCommand.setServer(scheduler.getNextServer());
        createCommand.setPrefillingAddress(callerAddress);
        createCommand.setFromTag(DEFAULT_FROMTAG);

        sendCommand(createCommand, createCommand.getServer().getAddress());
    }

    /**
     * Asynchronously asks the RTPProxy record a particular session
     * 
     * @param sessionID
     *            String to be used as session ID.
     * @param appData
     *            Application data object, will be passed as argument in the
     *            callback method, it isn't used internally.
     * @param listener
     *            that will receive the callback events for this command. (see
     *            {@link RTPProxyClientListener} for more information about
     *            callback methods).
     * @param isOwner
     * 
     * @throws RTPProxyClientException
     */
    public void recordSession(String sessionID, Object appData, RTPProxyClientListener listener, String fileName, boolean isOwner) throws RTPProxyClientException {
	RecordCommand command = new RecordCommand();
	command.setSessionID(sessionID);
	command.setAppData(appData);
	command.setListener(listener);
	command.setServer(scheduler.getNextServer());
	command.setFileName(fileName);

	if (isOwner) {
	    command.setFromTag(DEFAULT_FROMTAG);
	    command.setToTag(DEFAULT_TOTAG);
	} else {
	    command.setFromTag(DEFAULT_TOTAG);
	    command.setToTag(DEFAULT_FROMTAG);
	}

	sendCommand(command, command.getServer().getAddress());
    }

    /**
     * Asynchronously update an existing session to create the Caller media
     * address. This method doesn't pre-fill callee's address so RTPProxy server
     * will wait for callee's RTP packets before start forwarding caller's RTP
     * packets to him.
     *
     * @param Session to be updated.
     * @param Application data object, will be passed as argument in the
     *        callback method, it isn't used internally.
     * @param Listener that will receive the callback events for this command.
     *        (see {@link RTPProxyClientListener} for more information about
     *        callback methods).
     */
    public void updateSession(RTPProxySession session, Object appData,
            RTPProxyClientListener listener) throws RTPProxyClientException {

        updateSession(session, null, appData, listener);
    }

    /**
     * Asynchronously update an existing session to create the Caller media
     * address pre-filling callee's address with 'calleeAddress'. That way the
     * RTPProxy servers doesn't need to wait callee's RTP packets to arrive
     * before start forwarding caller's RTP packets to him.
     *
     * @param session to be updated
     * @param Callee's media address to be pre-filled
     * @param Application data object, will be passed as argument in the
     *        callback method, it isn't used internally.
     * @param Listener that will receive the callback events for this command.
     *        (see {@link RTPProxyClientListener} for more information about
     *        callback methods).
     */
    public void updateSession(RTPProxySession session,
            InetSocketAddress calleeAddress,
            Object appData, RTPProxyClientListener listener)
            throws RTPProxyClientException {

        UpdateCommand updateCmd = new UpdateCommand(session);
        updateCmd.setListener(listener);
        updateCmd.setServer(session.getServer());
        updateCmd.setAppData(appData);
        updateCmd.setPrefillingAddress(calleeAddress);

        // It's necessary to invert from/to tag to let RTPProxy server know we
        // want to get the Caller media address with this command.
        updateCmd.setFromTag(DEFAULT_TOTAG);
        updateCmd.setToTag(DEFAULT_FROMTAG);

        sendCommand(updateCmd, session.getServer().getAddress());
    }

    /**
     * Asynchronously destroy the given session releasing all resources in the
     * RTPProxy server.
     *
     * @param Session to be destroyed.
     * @param Application data object, will be passed as argument in the
     *        callback method, it isn't used internally.
     * @param Listener that will receive the callback events for this command.
     *        (see {@link RTPProxyClientListener} for more information about
     *        callback methods).
     */
    public void destroySession(RTPProxySession sessionIface, Object appData,
            RTPProxyClientListener listener) throws RTPProxyClientException {

        RTPProxySessionImpl session = (RTPProxySessionImpl) sessionIface;
        DestroyCommand destroyCmd = new DestroyCommand(session);
        destroyCmd.setListener(listener);
        destroyCmd.setAppData(appData);
        destroyCmd.setFromTag(DEFAULT_FROMTAG);
        destroyCmd.setToTag(DEFAULT_TOTAG);

        sendCommand(destroyCmd, session.getServer().getAddress());
    }

    /**
     * Add the command to the {@link CommandTimeoutManager} and send it to the
     * given server address using the {@link DatagramService}.
     */
    protected void sendCommand(Command command, InetSocketAddress serverAddr)
            throws RTPProxyClientTerminatedException {

        if (!isTerminated) {
            commandTimeout.addCommand(command);
            udpService.send(command.getMessage(), serverAddr);

        } else {
            throw new RTPProxyClientTerminatedException(
                    "RTPProxyClient instance is already terminated.");
        }
    }

    /**
     * Get RTPProxy-Client service configuration.
     *
     * @return Client configuration.
     */
    public RTPProxyClientConfig getConfig() {
        return config;
    }

    /**
     * Terminate RTPProxyClient instance and release all resources. All
     * subsequent call to client methods will throw an exception
     * RTPProxyClientTerminatedException.
     */
    public void terminate() throws RTPProxyClientException {
        try {
            synchronized (this) {
                if (!isTerminated) {
                    udpService.stop();
                    commandTimeout.terminate();
                    executor.shutdownNow();
                } else {
                    throw new RTPProxyClientTerminatedException(
                            "RTPProxyClient instance is already terminated.");
                }
            }
        } catch (Exception e) {
            throw new RTPProxyClientException(
                    "Error terminating RTPProxy-Client", e);
        }
    }

    public boolean isTerminated() {
        return isTerminated;
    }

    /**
     * Factory method to create a {@link CommandTimeoutManager}.
     *
     * @return
     */
    protected TimeoutManager createCommandTimeoutManager(
            ScheduledThreadPoolExecutor executor, long commandTimeout) {
        return new TimeoutManager(executor, commandTimeout);
    }

    /**
     * Factory method to create a {@link DatagramService}.
     *
     * @return
     */
    protected DatagramService createDatagraService(int bindPort,
            DatagramListener listener) throws IOException {
        return new DatagramService(bindPort, listener);
    }

    /**
     * Factory method to create a {@link RTPClientResponseHandler}.
     *
     * @return
     */
    protected CallbackHandler createCallbackHandler(
            TimeoutManager commandManager) {
        return new CallbackHandler(commandManager);
    }

    /**
     * Factory method to create a {@link ScheduledThreadPoolExecutor}.
     *
     * @return
     */
    protected ScheduledThreadPoolExecutor createThreadPoolExecutor(
            int poolSize) {
        return new ScheduledThreadPoolExecutor(poolSize);
    }

    /**
     * Factory method to create a {@link RTPProxyScheduler}.
     * 
     * @return
     */
    protected RTPProxyScheduler createScheduler(String schedulerName,
            List<RTPProxyServer> servers) throws RTPProxyClientConfigException {
        return RTPProxySchedulerFactory.createScheduler(schedulerName, servers);
    }
}
