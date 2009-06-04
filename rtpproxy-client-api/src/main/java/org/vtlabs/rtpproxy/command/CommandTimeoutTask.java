/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.command;

/**
 *
 * @author mhack
 */
public class CommandTimeoutTask implements Runnable {
    private Command command;
    private CommandTimeoutManager manager;

    public CommandTimeoutTask(Command command, CommandTimeoutManager manager) {
        this.command = command;
        this.manager = manager;
    }

    public void run() {
        command.commandTimeout();
        manager.removePendingCommand(command.getCookie());
    }
}
