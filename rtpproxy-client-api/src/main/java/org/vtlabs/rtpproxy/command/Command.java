package org.vtlabs.rtpproxy.command;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vtlabs.rtpproxy.callback.CallbackHandler;
import org.vtlabs.rtpproxy.client.RTPProxyClientListener;
import org.vtlabs.rtpproxy.client.RTPProxySessionException;

/**
 * {@link Command} implementations are used to build command lines to be sent to
 * the RTPProxy servers and to process callback events generated by the
 * {@link CallbackHandler}.
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public abstract class Command {

    /**
     * Command cookie, used to identify callback messages from the RTPProxy
     * server. It's used as the first argument of all commands and is
     * automaticaly generated in the {@link Command} constructor.
     */
    private String cookie;
    /**
     * Session ID used as 'callid' parameter of messages sent to RTPProxy
     * server.
     */
    protected String sessionID;
    /**
     * Command 'fromtag' argument.
     */
    private String fromTag;
    /**
     * Command 'totag' argument.
     */
    private String toTag;
    /**
     * Arbitrary object that is passed back as argument to the callback
     * listener. The RTPProxy-Client doesn't use it internally.
     */
    protected Object appData;
    /**
     * Listener of the callback events received from the RTPProxy server.
     */
    protected RTPProxyClientListener listener;
    protected Logger log;

    public Command() {
        cookie = UUID.randomUUID().toString();
        fromTag = null;
        toTag = null;
        log = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Text representation of the command without the COOKIE prefix, as defined
     * in the RTPPRoxy.
     */
    public abstract String getMessage();

    /**
     * Sucessful response callback method.
     */
    public void processResponse(String message) {
    }

    /**
     * Error response callback method.
     */
    public void processError(String message) {
    }

    /**
     * Timeout callback method.
     */
    public abstract void processTimeout();

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public RTPProxyClientListener getListener() {
        return listener;
    }

    public void setListener(RTPProxyClientListener listener) {
        this.listener = listener;
    }

    public Object getAppData() {
        return appData;
    }

    public void setAppData(Object appData) {
        this.appData = appData;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getFromTag() {
        return fromTag;
    }

    public void setFromTag(String fromTag) {
        this.fromTag = fromTag;
    }

    public String getToTag() {
        return toTag;
    }

    public void setToTag(String toTag) {
        this.toTag = toTag;
    }

    protected RTPProxySessionException createException(String message,
            String errorMessage) {
        StringBuilder sb = new StringBuilder(message).append(": ");
        sb.append(errorMessage);
        return new RTPProxySessionException(sb.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Command[");
        sb.append("commandType = ").append(this.getClass().getName());
        sb.append(", sessionID = ").append(getSessionID());
        sb.append(", cookie = ").append(getCookie());
        sb.append("]");
        return sb.toString();
    }
}
