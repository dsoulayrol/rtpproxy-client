package org.vtlabs.rtpproxy.client;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public interface RTPProxyEventListener {
    public void onSessionTimeout(RTPProxySession session);
}
