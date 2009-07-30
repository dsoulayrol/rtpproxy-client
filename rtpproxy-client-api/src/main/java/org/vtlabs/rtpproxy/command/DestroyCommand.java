package org.vtlabs.rtpproxy.command;

import org.vtlabs.rtpproxy.exception.RTPProxyClientException;
import org.vtlabs.rtpproxy.client.RTPProxySessionImpl;
import org.vtlabs.rtpproxy.client.RTPProxySessionState;

/**
 *
 * @author mhack
 */
public class DestroyCommand extends Command {

    RTPProxySessionImpl session;

    public DestroyCommand(RTPProxySessionImpl session) {
        super();
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

    @Override
    public void processError(String message) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Failed to destroy session: ");
            sb.append(message);
            log.debug(sb.toString());
        }

        session.setState(RTPProxySessionState.FAILED);
        listener.destroySessionFailed(session, appData, createException(
                "Error destroying RTPProxy session", message));
    }

    @Override
    public void processResponse(String message) {
        log.debug("Session destroyed");
        session.setState(RTPProxySessionState.DESTROYED);
        listener.sessionDestroyed(session, appData);
    }

    @Override
    public void processTimeout() {
        log.debug("Destroy command timeout");
        session.setState(RTPProxySessionState.FAILED);
        listener.destroySessionTimeout(session, appData);
    }

    public RTPProxySessionImpl getSession() {
        return session;
    }

    public void setSession(RTPProxySessionImpl session) {
        this.session = session;
    }
}
