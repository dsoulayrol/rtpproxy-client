package org.vtlabs.rtpproxy.command;

import org.vtlabs.rtpproxy.client.RTPProxySession;

/**
 *
 * @author mhack
 */
public class DestroyCommand extends Command {

    RTPProxySession session;

    public DestroyCommand(CommandListener cmdListener) {
        super(cmdListener);
    }

    public DestroyCommand(RTPProxySession session, CommandListener listener) {
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

    public RTPProxySession getSession() {
        return session;
    }

    public void setSession(RTPProxySession session) {
        this.session = session;
    }
}
