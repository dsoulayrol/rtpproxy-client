package org.vtlabs.rtpproxy.command;

import org.vtlabs.rtpproxy.client.RTPProxySession;

/**
 * Update sesssion, creating a new one if it doesn't exist.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class UpdateCommand extends Command {
    private RTPProxySession session;
    private String callID;
    private String address;
    private Integer port;
    private String fromTag;
    private String toTag;

    public UpdateCommand() {
        session = null;
        callID = null;
        address = null;
        port = null;
        fromTag = null;
        toTag = null;
    }

    public UpdateCommand(RTPProxySession session) {
        this();
        this.session = session;
        callID = session.getSessionID();
    }

    /**
     * Command format: 'cookie U[args] callid addr port from_tag to_tag'
     */
    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder("U ").append(callID);
        sb.append(" ").append(address != null ? address : 0);
        sb.append(" ").append(port != null ? port : 0);
        sb.append(" ").append(fromTag != null ? fromTag : 0);
        sb.append(" ").append(toTag != null ? toTag : 0);
        return sb.toString();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCallID() {
        return callID;
    }

    public void setCallID(String callID) {
        this.callID = callID;
    }

    public String getFromTag() {
        return fromTag;
    }

    public void setFromTag(String fromTag) {
        this.fromTag = fromTag;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getToTag() {
        return toTag;
    }

    public void setToTag(String toTag) {
        this.toTag = toTag;
    }

    public RTPProxySession getSession() {
        return session;
    }

    public void setSession(RTPProxySession session) {
        this.session = session;
    }
}
