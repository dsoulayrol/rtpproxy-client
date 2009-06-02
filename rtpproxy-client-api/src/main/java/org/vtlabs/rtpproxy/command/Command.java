/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.command;

import java.util.UUID;

/**
 *
 * @author mhack
 */
public abstract class Command {
    private Object appData;
    private String cookie;

    public Command() {
        UUID uuid = UUID.randomUUID();
        cookie = uuid.toString();
    }

    public abstract String getMessage();

    public String getCookie() {
        return cookie;
    }
}
