/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.mock.command;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.vtlabs.rtpproxy.command.Command;
import org.vtlabs.rtpproxy.command.CommandTimeoutManager;
import static org.junit.Assert.*;

/**
 *
 * @author mhack
 */
public class CommandTimeoutManagerMOCK extends CommandTimeoutManager {

    public Command pendingCommand;

    public CommandTimeoutManagerMOCK(ScheduledThreadPoolExecutor executor,
            long commandTimeout) {
        super(executor, commandTimeout);
        pendingCommand = null;
    }

    @Override
    public void addPendingCommand(Command command) {

        // assert expected command atributes
        assertNotNull("Command callback listener is null",
                command.getCallbackListener());
        assertNotNull("Command listener is null", command.getListener());
        assertNotNull("Command cookie is null", command.getCookie());
        assertNotNull("Command session ID is null", command.getSessionID());

        pendingCommand = command;
    }

    @Override
    public Command removePendingCommand(String cookie) {
        return pendingCommand;
    }
}