/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.mock.command;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.vtlabs.rtpproxy.command.Command;
import org.vtlabs.rtpproxy.command.CommandTimeoutManager;

/**
 *
 * @author mhack
 */
public class CommandTimeoutManagerMOCK extends CommandTimeoutManager {

    public Command pendingCommand;

    public CommandTimeoutManagerMOCK() {
        super(null);
    }

    public CommandTimeoutManagerMOCK(ScheduledThreadPoolExecutor executor) {
        super(executor);
        pendingCommand = null;
    }

    @Override
    public void addPendingCommand(Command command) {
        pendingCommand = command;
    }

    @Override
    public Command removePendingCommand(String cookie) {
        return pendingCommand;
    }
}