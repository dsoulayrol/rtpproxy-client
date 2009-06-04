/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.client;

/**
 *
 * @author mhack
 */
public class ConfigErrorException extends Exception {

    public ConfigErrorException(String message) {
        super(message);
    }

    public ConfigErrorException(String message, Throwable t) {
        super(message, t);
    }
}
