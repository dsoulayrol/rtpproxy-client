package org.vtlabs.rtpproxy.client;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class RTPProxySession {
    private String callId;
    private String callerIPAddr;
    private int callerPort;
    private String calleeIPAddr;
    private int calleePort;
    private String toTag;
    private String fromTag;
    private Object userData;
}
