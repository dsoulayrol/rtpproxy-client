package org.vtlabs.rtpproxy.exception;

/**
 *
 * @author mhack
 */
public class RTPProxyClientConfigException extends RTPProxyClientException {

    public RTPProxyClientConfigException(String message) {
        super(message);
    }

    public RTPProxyClientConfigException(String message, Throwable t) {
        super(message, t);
    }
}
