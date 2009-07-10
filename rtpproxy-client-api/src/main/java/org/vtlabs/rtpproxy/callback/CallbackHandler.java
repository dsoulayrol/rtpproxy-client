package org.vtlabs.rtpproxy.callback;

import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vtlabs.rtpproxy.client.RTPProxyClientListener;
import org.vtlabs.rtpproxy.client.RTPProxySessionImpl;
import org.vtlabs.rtpproxy.client.RTPProxySessionException;
import org.vtlabs.rtpproxy.client.RTPProxySessionState;
import org.vtlabs.rtpproxy.command.Command;
import org.vtlabs.rtpproxy.command.CommandListener;
import org.vtlabs.rtpproxy.command.CommandTimeoutManager;
import org.vtlabs.rtpproxy.command.DestroyCommand;
import org.vtlabs.rtpproxy.command.UpdateCommand;
import org.vtlabs.rtpproxy.udp.DatagramListener;

/**
 * Handler of RTPProxy responses and notify RTPProxyClientListener.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class CallbackHandler implements DatagramListener, CommandListener {

    private CommandTimeoutManager timeoutManager;
    private Logger log = LoggerFactory.getLogger(CallbackHandler.class);
    private static Pattern errorPattern = Pattern.compile("^E(.*)$");
    private Matcher errorMatcher;

    public CallbackHandler(CommandTimeoutManager commandManager) {
        this.timeoutManager = commandManager;

        // initialize the error matcher is a blank string, it will be reseted
        // for each message received in the method processResponse().
        errorMatcher = errorPattern.matcher("");
    }

    /**
     * Callback method for received datagram messages (DatagramListener).
     *
     * @param Command cookie
     * @param Response message
     * @param Datagram source address
     */
    public void processResponse(String cookie, String message,
            InetSocketAddress srcAddr) {

        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Processing reponse message ");
            sb.append("\'").append(cookie);
            sb.append(" ").append(message).append("\'");
            sb.append(" received from ").append(srcAddr);
            log.debug(sb.toString());
        }

        Command command = timeoutManager.removePendingCommand(cookie);

        if (command instanceof UpdateCommand) {
            processUpdateCommand((UpdateCommand) command, message);

        } else if (command instanceof DestroyCommand) {
            processDestroyCommand((DestroyCommand) command, message);

        } else {
            processUnknownCommand(command, message);
        }
    }

    /**
     * Handle Update command callback.
     *
     * @param command that originated this callback message
     * @param callback message received from RTPPRoxy server
     */
    protected void processUpdateCommand(UpdateCommand command, String message) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Processing Update ");
            sb.append(command);
            log.debug(sb.toString());
        }

        RTPProxySessionImpl session = command.getSession();
        boolean isError = errorMatcher.reset(message).matches();

        if (session == null && isError) {
            processSessionCreateFailed(command, message);

        } else if (session == null) {
            processSessionCreated(command, message);

        } else if (session.getCallerMediaAddress() == null && isError) {
            processSessionUpdateFailed(command, message);

        } else if (session.getCallerMediaAddress() == null) {
            processSessionUpdated(command, message);

        } else {
            StringBuilder sb = new StringBuilder("Unknown update command ");
            sb.append(" for message \'").append(message).append("\'");
            sb.append(": ").append(command);
            log.warn(sb.toString());
        }
    }

    /**
     * Handle Update command callback.
     *
     * @param command that originated this callback message
     * @param callback message received from RTPPRoxy server
     */
    protected void processDestroyCommand(DestroyCommand command,
            String message) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Processing Destroy ");
            sb.append(command);
            log.debug(sb.toString());
        }

        RTPProxySessionImpl session = command.getSession();
        RTPProxyClientListener listener = command.getCallbackListener();
        Object appData = command.getAppData();
        boolean isError = errorMatcher.reset(message).matches();

        if (!isError) {
        	session.setState(RTPProxySessionState.DESTROYED);
            listener.sessionDestroyed(session, appData);

        } else {
            Throwable t = createSessionException(
                    "Error destroying RTPProxy session",
                    message);
            
            session.setState(RTPProxySessionState.FAILED);
            listener.destroySessionFailed(session, appData, t);
        }
    }

    /**
     * Handle error for an RTPProxyClient.updateSession() request.
     *
     * @param Update command
     * @param Error message
     */
    protected void processSessionCreateFailed(UpdateCommand command,
            String errorMessage) {
        log.debug("Processing event SessionCreateFailed");

        Throwable t = createSessionException("Error creating a RTPProxy session"
                , errorMessage);

        RTPProxyClientListener listener = command.getCallbackListener();
        listener.createSessionFailed(command.getSessionID(),
                command.getAppData(), t);
    }

    /**
     * Handle update command response that create a new session. It contains the
     * callee media port and address.
     *
     * @param Command
     * @param Response message
     */
    protected void processSessionCreated(UpdateCommand command,
            String message) {
        log.debug("Processing event SessionCreated");

        RTPProxySessionImpl session = new RTPProxySessionImpl();
        session.setSessionID(command.getSessionID());
        session.setServer(command.getServer());
        session.setState(RTPProxySessionState.CREATED);

        InetSocketAddress calleeMediaAddr = parseMediaAddress(message);
        session.setCalleeMediaAddress(calleeMediaAddr);

        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Callee media address ");
            sb.append(calleeMediaAddr);
            log.debug(sb.toString());
        }

        RTPProxyClientListener callbackListener = command.getCallbackListener();
        callbackListener.sessionCreated(session, command.getAppData());
    }

    /**
     * Handle error for an RTPProxyClient.updateSession() request.
     *
     * @param Update command
     * @param Error message
     */
    protected void processSessionUpdateFailed(UpdateCommand command,
            String errorMessage) {
        log.debug("Processing event SessionUpdateFailed");

        Throwable t = createSessionException("Error updating RTPProxy session",
                errorMessage);

        RTPProxySessionImpl session = command.getSession();
        session.setState(RTPProxySessionState.FAILED);
        RTPProxyClientListener listener = command.getCallbackListener();
        listener.updateSessionFailed(session, command.getAppData(), t);
    }

    /**
     *
     * @param command
     * @param message
     */
    protected void processSessionUpdated(UpdateCommand command,
            String message) {
        log.debug("Processing event SessionUpdated");

        RTPProxySessionImpl session = command.getSession();
        InetSocketAddress callerMediaAddr = parseMediaAddress(message);
        session.setCallerMediaAddress(callerMediaAddr);

        RTPProxyClientListener callbackListener = command.getCallbackListener();
        callbackListener.sessionUpdated(session, command.getAppData());
    }

    /**
     * 
     * @param command
     * @param message
     */
    protected void processUnknownCommand(Command command, String message) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Command timeout callback method (CommandListener interface).
     *
     * @param Command that reached the timeout period.
     */
    public void commandTimeout(Command command) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Processing command timeout ");
            sb.append(command);
            log.debug(sb.toString());
        }

        if (command instanceof UpdateCommand) {
            processUpdateCommandTimeout((UpdateCommand) command);

        } else if (command instanceof DestroyCommand) {
            processDestroyCommandTimeout((DestroyCommand) command);

        } else {
            StringBuilder sb = new StringBuilder("Unknown command timeout ");
            sb.append(command);
            log.error(sb.toString());
        }
    }

    /**
     * Process update command timeout.
     */
    public void processUpdateCommandTimeout(UpdateCommand command) {
        RTPProxyClientListener listener = command.getCallbackListener();
        RTPProxySessionImpl session = command.getSession();
        Object appData = command.getAppData();
        String sessionID = command.getSessionID();

        if (session == null) {
            listener.createSessionTimeout(sessionID, appData);
        } else {
        	session.setState(RTPProxySessionState.FAILED);
            listener.updateSessionTimeout(session, appData);
        }
    }

     /**
     * Process destroy command timeout.
     */
    public void processDestroyCommandTimeout(DestroyCommand command) {
        RTPProxyClientListener listener = command.getCallbackListener();
        RTPProxySessionImpl session = command.getSession();
        session.setState(RTPProxySessionState.FAILED);
        Object appData = command.getAppData();
        listener.destroySessionTimeout(session, appData);
    }

    /**
     * Parse the media address returned in the RTPProxy callback message.
     *
     * Callback message format: MEDIA_PORT MEDIA_SERVER_IP
     *
     * @param Update callback message
     * @return InetSocketAddress
     */
    private InetSocketAddress parseMediaAddress(String strAddr) {
        String[] arrMediaAddr = StringUtils.split(strAddr, " ", 2);
        int port = Integer.parseInt(arrMediaAddr[0]);
        String addr = arrMediaAddr[1];
        return new InetSocketAddress(addr, port);
    }

    /**
     * Create an RTPProxyClient
     */
    private RTPProxySessionException createSessionException(String message,
            String errorMessage) {
        StringBuilder sb = new StringBuilder(message).append(": ");
        sb.append(errorMessage);
        return new RTPProxySessionException(sb.toString());
    }
}
