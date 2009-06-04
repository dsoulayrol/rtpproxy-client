/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.client;

/**
 *
 * @author mhack
 */
public class NoServerAvailableException extends Exception {
    public NoServerAvailableException(String message) {
        super(message);
    }
}
