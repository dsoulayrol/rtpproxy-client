package org.vtlabs.rtpproxy.callback;

import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.util.SessionLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vtlabs.rtpproxy.client.RTPProxyClientListener;
import org.vtlabs.rtpproxy.client.RTPProxySession;
import org.vtlabs.rtpproxy.client.RTPProxySessionException;
import org.vtlabs.rtpproxy.command.Command;
import org.vtlabs.rtpproxy.command.CommandListener;
import org.vtlabs.rtpproxy.command.CommandTimeoutManager;
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
        Command command = timeoutManager.removePendingCommand(cookie);

        if (command instanceof UpdateCommand) {
            processUpdateCommand((UpdateCommand) command, message);

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
        RTPProxySession session = command.getSession();
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
     * Handle error for an RTPProxyClient.updateSession() request.
     *
     * @param Update command
     * @param Error message
     */
    protected void processSessionCreateFailed(UpdateCommand command,
            String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("Error creating a RTPProxy session: ");
        sb.append("\'").append(message).append("\'");
        Exception exception = new Exception(sb.toString());
        
        RTPProxyClientListener listener = command.getCallbackListener();
        listener.createSessionFailed(command.getCallID(), command.getAppData(),
                exception);
    }

    /**
     * Handle update command response that create a new session. It contains the
     * callee media port and address.
     *
     * @param Command
     * @param Response message
     */
    protected void processSessionCreated(UpdateCommand command, String message) {
        RTPProxySession session = new RTPProxySession();
        session.setSessionID(command.getSessionID());
        session.setServer(command.getServer());

        InetSocketAddress calleeMediaAddr = parseMediaAddress(message);
        session.setCalleeMediaAddress(calleeMediaAddr);

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
            String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("Error updating RTPProxy session: ");
        sb.append("\'").append(message).append("\'");
        Throwable t = new RTPProxySessionException(sb.toString());

        RTPProxyClientListener listener = command.getCallbackListener();
        listener.updateSessionFailed(command.getSession(), command.getAppData(),
                t);
    }

    /**
     *
     * @param command
     * @param message
     */
    protected void processSessionUpdated(UpdateCommand command, String message) {
        RTPProxySession session = command.getSession();
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
        RTPProxyClientListener listener = command.getCallbackListener();
        listener.createSessionTimeout(command.getSessionID(),
                command.getAppData());
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
}
