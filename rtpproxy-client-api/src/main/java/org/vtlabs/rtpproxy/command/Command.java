/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.command;

import java.util.UUID;
import org.vtlabs.rtpproxy.client.RTPProxyClientListener;

/**
 *
 * @author mhack
 */
public abstract class Command {

    /**
     * Command cookie, used to identify callback messages from the RTPProxy
     * server. It's used as the first argument of all commands.
     */
    private String cookie;

    /**
     * Session ID used as 'callid' parameter of messages sent to RTPProxy
     * server.
     */
    private String sessionID;

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
    private Object appData;

    /**
     * Listener of the callback events received from the RTPProxy server.
     */
    private RTPProxyClientListener listener;

    /**
     * Command listener to receive timeout event from the CommandTimeoutManager.
     */
    private CommandListener cmdListener;

    public Command(CommandListener cmdListener) {
        this.cmdListener = cmdListener;
        cookie = UUID.randomUUID().toString();
        fromTag = null;
        toTag = null;
    }

    /**
     * Text representation of the command without the COOKIE prefix, as defined
     * in the RTPPRoxy.
     */
    public abstract String getMessage();

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public RTPProxyClientListener getCallbackListener() {
        return listener;
    }

    public void setCallbackListener(RTPProxyClientListener listener) {
        this.listener = listener;
    }

    public CommandListener getListener() {
        return cmdListener;
    }

    public void setListener(CommandListener listener) {
        cmdListener = listener;
    }

    public void commandTimeout() {
        cmdListener.commandTimeout(this);
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
