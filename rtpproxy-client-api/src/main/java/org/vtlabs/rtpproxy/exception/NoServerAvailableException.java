/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.exception;

/**
 *
 * @author mhack
 */
public class NoServerAvailableException extends RTPProxyClientException {
    public NoServerAvailableException(String message) {
        super(message);
    }
}
