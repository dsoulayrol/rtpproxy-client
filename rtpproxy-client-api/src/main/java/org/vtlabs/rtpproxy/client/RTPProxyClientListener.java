package org.vtlabs.rtpproxy.client;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public interface RTPProxyClientListener {
    public void createSessionTimeout(String sessionID, Object appData);
    public void createSessionFailed(String sessionID, Object appData, Throwable t);
    public void updateSessionFailed(RTPProxySession session, Object appData, Throwable t);
    public void sessionCreated(RTPProxySession session, Object appData);
    public void sessionUpdated(RTPProxySession session, Object appData);
}
