package org.vtlabs.rtpproxy.command;

import java.net.InetSocketAddress;
import org.vtlabs.rtpproxy.client.RTPProxySession;
import org.vtlabs.rtpproxy.exception.RTPProxyClientException;
import org.vtlabs.rtpproxy.client.RTPProxySessionImpl;
import org.vtlabs.rtpproxy.client.RTPProxySessionState;

/**
 * Update sesssion, creating a new one if it doesn't exist.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class UpdateCommand extends CreateCommand {

    /**
     * RTPProxy session target of the update command. Its is 'null' in the first
     * update command used to create the session.
     */
    protected RTPProxySessionImpl session;

    public UpdateCommand(RTPProxySession session) {
        super();
        this.session = (RTPProxySessionImpl) session;
        setSessionID(session.getSessionID());
    }

    @Override
    public void processResponse(String message) {
        log.debug("Session updated");

        InetSocketAddress callerMediaAddr = parseMediaAddress(message);
        session.setCallerMediaAddress(callerMediaAddr);

        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Caller media address ");
            sb.append(callerMediaAddr);
            log.debug(sb.toString());
        }

        listener.sessionUpdated(session, appData);
    }

    @Override
    public void processError(String message) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Session update failed: ");
            sb.append(message);
            log.debug(sb.toString());
        }

        session.setState(RTPProxySessionState.FAILED);
        listener.updateSessionFailed(session, appData, createException(
                "Error creating RTPProxy session", message));
    }

    @Override
    public void processTimeout() {
        log.debug("Update command timeout");
        session.setState(RTPProxySessionState.FAILED);
        listener.updateSessionTimeout(session, appData);
    }

    public RTPProxySessionImpl getSession() {
        return session;
    }

    public void setSession(RTPProxySessionImpl session) {
        this.session = session;
    }
}
