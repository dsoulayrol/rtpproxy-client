package org.vtlabs.rtpproxy.command;

import java.net.InetSocketAddress;

import org.apache.commons.lang.StringUtils;
import org.vtlabs.rtpproxy.client.RTPProxyClient;
import org.vtlabs.rtpproxy.client.RTPProxyServer;
import org.vtlabs.rtpproxy.client.RTPProxySessionImpl;
import org.vtlabs.rtpproxy.client.RTPProxySessionState;

/**
 * Update sesssion, creating a new one if it doesn't exist.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class CreateCommand extends ParameterizedCommand {

    protected InetSocketAddress prefillingAddress;

    /**
     * RTPProxy server to which the command will be send. Its primary use is to
     * associate the RTPProxy server to created sessions, remember that the
     * session creation is made with 'Update' command.
     */
    protected RTPProxyServer server;

    public CreateCommand() {
        super();
        prefillingAddress = null;
    }

    /**
     * Command format: 'cookie U[args] callid addr port from_tag to_tag'
     */
    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(getCookie());
        sb.append(" U");
        String parameters = getParameters();
        if (parameters != null)
            sb.append(parameters);
        sb.append(" ").append(getSessionID());

        String strAddress;
        int port;
        if (prefillingAddress != null) {
            strAddress = prefillingAddress.getAddress().getHostAddress();
            port = prefillingAddress.getPort();
        } else {
            strAddress = "0";
            port = 0;
        }
        sb.append(" ").append(strAddress).append(" ").append(port);

        String fromTag = getFromTag();
        sb.append(" ").append(fromTag != null ? fromTag : 0);
        String toTag = getToTag();
        sb.append(" ").append(toTag != null ? toTag : 0);
        return sb.toString();
    }

    @Override
    public void processResponse(String message) {
        log.debug("Session created");

        RTPProxySessionImpl newSession = new RTPProxySessionImpl();
        newSession.setSessionID(sessionID);
        newSession.setServer(server);
        newSession.setState(RTPProxySessionState.CREATED);

        InetSocketAddress calleeMediaAddr = parseMediaAddress(message);
        newSession.setCalleeMediaAddress(calleeMediaAddr);

        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Callee media address ");
            sb.append(calleeMediaAddr);
            log.debug(sb.toString());
        }

        listener.sessionCreated(newSession, appData);
    }

    @Override
    public void processError(String message) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Session creation failed: ");
            sb.append(message);
            log.debug(sb.toString());
        }

        listener.createSessionFailed(sessionID, appData, createException(
                "Error creating RTPProxy session", message));
    }

    @Override
    public void processTimeout() {
        log.debug("Create command timeout");
        listener.createSessionTimeout(sessionID, appData);
    }

    protected InetSocketAddress parseMediaAddress(String strAddr) {
        String[] arrMediaAddr = StringUtils.split(strAddr, " ", 2);
        int port = Integer.parseInt(arrMediaAddr[0]);
        String addr = arrMediaAddr[1];
        return new InetSocketAddress(addr, port);
    }

    /**
     * Set the IP address to be prefilled in the RTPProxy session. See
     * {@link RTPProxyClient} <code>createSession()</code> and 
     * <code>updateSession()</code> methos for more information about address 
     * prefilling.
     *
     * @param address
     */
    public void setPrefillingAddress(InetSocketAddress address) {
        this.prefillingAddress = address;
    }

    public RTPProxyServer getServer() {
        return server;
    }

    public void setServer(RTPProxyServer server) {
        this.server = server;
    }
}
