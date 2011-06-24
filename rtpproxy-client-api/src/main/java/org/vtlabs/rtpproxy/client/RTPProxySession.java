package org.vtlabs.rtpproxy.client;

import java.net.InetSocketAddress;

public interface RTPProxySession {

	public InetSocketAddress getCalleeMediaAddress();

	public InetSocketAddress getCallerMediaAddress();

	public RTPProxyServer getServer();

	public String getSessionID();

	public String getOriginalCallerTag();

	public RTPProxySessionState getState();
	
	public boolean isActive();
}
