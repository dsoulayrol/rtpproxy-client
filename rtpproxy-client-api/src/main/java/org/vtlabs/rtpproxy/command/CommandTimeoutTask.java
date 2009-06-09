/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mhack
 */
public class CommandTimeoutTask implements Runnable {
    private Command command;
    private CommandTimeoutManager manager;
    private Logger log = LoggerFactory.getLogger(CommandTimeoutTask.class);

    public CommandTimeoutTask(Command command, CommandTimeoutManager manager) {
        this.command = command;
        this.manager = manager;
    }

    public void run() {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Running command timeout ");
            sb.append("task for command ").append(command);
            log.debug(sb.toString());
        }

        CommandListener listener = command.getListener();
        listener.commandTimeout(command);
    }
}
