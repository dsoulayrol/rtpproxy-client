/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.config;

/**
 *
 * @author mhack
 */
public class RTPProxyClientConfigException extends Exception {

    public RTPProxyClientConfigException(String message) {
        super(message);
    }

    public RTPProxyClientConfigException(String message, Throwable t) {
        super(message, t);
    }
}
