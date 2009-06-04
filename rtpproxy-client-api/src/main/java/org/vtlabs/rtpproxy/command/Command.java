/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.command;

import java.util.UUID;
import org.vtlabs.rtpproxy.client.RTPProxyClientListener;

/**
 *
 * @author mhack
 */
public abstract class Command {
    private Object appData;
    private String cookie;
    private RTPProxyClientListener listener;

    public Command() {
        UUID uuid = UUID.randomUUID();
        cookie = uuid.toString();
    }

    public abstract String getMessage();

    public String getCookie() {
        return cookie;
    }

    public RTPProxyClientListener getListener() {
        return listener;
    }

    public void setListener(RTPProxyClientListener listener) {
        this.listener = listener;
    }
}
