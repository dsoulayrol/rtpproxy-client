package org.vtlabs.rtpproxy.client;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public interface RTPProxyClientListener {
    public void sessionCreated(RTPProxySession session, Object appData);
    public void sessionUpdated(RTPProxySession session, Object appData);
    public void sessionDestroyed(RTPProxySession session, Object appData);
    public void createSessionTimeout(String sessionID, Object appData);
    public void createSessionFailed(String sessionID, Object appData, Throwable t);
    public void updateSessionTimeout(RTPProxySession session, Object appData);
    public void updateSessionFailed(RTPProxySession session, Object appData, Throwable t);
    public void destroySessionTimeout(RTPProxySession session, Object appData);
    public void destroySessionFailed(RTPProxySession session, Object appData, Throwable t);
}
