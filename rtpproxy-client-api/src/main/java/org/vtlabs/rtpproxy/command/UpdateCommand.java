package org.vtlabs.rtpproxy.command;

import java.net.InetSocketAddress;
import org.vtlabs.rtpproxy.client.RTPProxyServer;
import org.vtlabs.rtpproxy.client.RTPProxySessionImpl;

/**
 * Update sesssion, creating a new one if it doesn't exist.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class UpdateCommand extends Command {
    
    private InetSocketAddress address;

    /**
     * RTPProxy session target of the update command. Its is 'null' in the first
     * update command used to create the session.
     */
    private RTPProxySessionImpl session;

    /**
     * RTPProxy server to which the command will be send. Its primary use is to
     * associate the RTPProxy server to created sessions, remember that the
     * session creation is made with 'Update' command.
     */
    private RTPProxyServer server;

    public UpdateCommand(CommandListener cmdListener) {
        super(cmdListener);
        session = null;
        address = null;
    }

    public UpdateCommand(RTPProxySessionImpl session, CommandListener cmdListener) {
        this(cmdListener);
        this.session = session;
        setSessionID(session.getSessionID());
    }

    /**
     * Command format: 'cookie U[args] callid addr port from_tag to_tag'
     */
    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(getCookie());
        sb.append(" U ");
        sb.append(getSessionID());

        String strAddress;
        int port;
        if (address != null) {
            strAddress = address.getAddress().getHostAddress();
            port = address.getPort();
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

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public RTPProxySessionImpl getSession() {
        return session;
    }

    public void setSession(RTPProxySessionImpl session) {
        this.session = session;
    }

    public RTPProxyServer getServer() {
        return server;
    }

    public void setServer(RTPProxyServer server) {
        this.server = server;
    }
}
