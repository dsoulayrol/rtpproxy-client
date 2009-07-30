package org.vtlabs.rtpproxy.exception;

/**
 *
 * @author mhack
 */
public class RTPProxyClientException extends Exception {

    public RTPProxyClientException(String message) {
        super(message);
    }

    public RTPProxyClientException(String message, Throwable t) {
        super(message, t);
    }
}
