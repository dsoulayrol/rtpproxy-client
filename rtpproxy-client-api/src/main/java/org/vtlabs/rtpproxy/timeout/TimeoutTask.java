/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.timeout;

import org.vtlabs.rtpproxy.command.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mhack
 */
public class TimeoutTask implements Runnable {
    private Command command;
    private TimeoutManager manager;
    private Logger log = LoggerFactory.getLogger(TimeoutTask.class);

    public TimeoutTask(Command command, TimeoutManager manager) {
        this.command = command;
        this.manager = manager;
    }

    public void run() {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Running command timeout ");
            sb.append("task for command ").append(command);
            log.debug(sb.toString());
        }

        command.processTimeout();
    }
}
