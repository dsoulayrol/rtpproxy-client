/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.command;

/**
 *
 * @author mhack
 */
public interface CommandListener {
    public void commandTimeout(Command command);
}
