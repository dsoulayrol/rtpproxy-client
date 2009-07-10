package org.vtlabs.rtpproxy.client;

import java.net.InetSocketAddress;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class RTPProxySessionImpl implements RTPProxySession {
    private String sessionID;
    private InetSocketAddress callerMediaAddress;
    private InetSocketAddress calleeMediaAddress;
    private RTPProxyServer server;
    private RTPProxySessionState state;

    public InetSocketAddress getCalleeMediaAddress() {
        return calleeMediaAddress;
    }

    public void setCalleeMediaAddress(InetSocketAddress calleeMediaAddress) {
        this.calleeMediaAddress = calleeMediaAddress;
    }

    public InetSocketAddress getCallerMediaAddress() {
        return callerMediaAddress;
    }

    public void setCallerMediaAddress(InetSocketAddress callerMediaAddress) {
        this.callerMediaAddress = callerMediaAddress;
    }

    public RTPProxyServer getServer() {
        return server;
    }

    public void setServer(RTPProxyServer server) {
        this.server = server;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
    
    public RTPProxySessionState getState() {
		return state;
	}
    
    public void setState(RTPProxySessionState state) {
    	this.state = state;
    }

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RTPProxySession[");
        sb.append("sessionID = ").append(sessionID);
        sb.append(", calleeMediaAddress = ").append(calleeMediaAddress);
        sb.append(", callerMediaAddress = ").append(callerMediaAddress);
        sb.append(", server = ").append(server);
        sb.append("]");
        return sb.toString();
    }
}
