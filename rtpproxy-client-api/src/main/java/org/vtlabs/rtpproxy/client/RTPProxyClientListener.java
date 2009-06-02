package org.vtlabs.rtpproxy.client;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public interface RTPProxyClientListener {
    public void createSessionTimeout(Object appData);
    public void createSessionFailed(Object appData, Throwable t);
    public void sessionCreated(Object appData, RTPProxySession session);
}
