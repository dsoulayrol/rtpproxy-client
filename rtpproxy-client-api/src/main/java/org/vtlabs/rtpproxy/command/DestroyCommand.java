package org.vtlabs.rtpproxy.command;

import org.vtlabs.rtpproxy.client.RTPProxySessionImpl;

/**
 *
 * @author mhack
 */
public class DestroyCommand extends Command {

    RTPProxySessionImpl session;

    public DestroyCommand(CommandListener cmdListener) {
        super(cmdListener);
    }

    public DestroyCommand(RTPProxySessionImpl session, CommandListener listener) {
        super(listener);
        this.session = session;
        setSessionID(session.getSessionID());
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(getCookie());
        sb.append(" D ").append(getSessionID());
        sb.append(" ").append(getFromTag());
        sb.append(" ").append(getToTag());
        return sb.toString();
    }

    public RTPProxySessionImpl getSession() {
        return session;
    }

    public void setSession(RTPProxySessionImpl session) {
        this.session = session;
    }
}
