package org.vtlabs.rtpproxy.client;

import java.net.InetSocketAddress;

public interface RTPProxySession {

	public InetSocketAddress getCalleeMediaAddress();

	public InetSocketAddress getCallerMediaAddress();

	public RTPProxyServer getServer();

	public String getSessionID();

	public RTPProxySessionState getState();
}
