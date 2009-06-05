/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.test.callback;

import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.junit.Before;
import org.junit.Test;
import org.vtlabs.rtpproxy.callback.CallbackHandler;
import org.vtlabs.rtpproxy.client.RTPProxyClientListener;
import org.vtlabs.rtpproxy.client.RTPProxySession;
import org.vtlabs.rtpproxy.command.Command;
import org.vtlabs.rtpproxy.command.CommandTimeoutManager;
import org.vtlabs.rtpproxy.command.UpdateCommand;
import org.vtlabs.rtpproxy.test.BaseTest;

/**
 *
 * @author mhack
 */
public class CallbackHandlerTest extends BaseTest {

    private CommandTimeoutManagerMOCK timeoutMngr;
    private CallbackHandler callbackHandler;
    private RTPProxyClientListenerMOCK listener;

    @Before
    public void init() {
        timeoutMngr = new CommandTimeoutManagerMOCK();
        callbackHandler = new CallbackHandler(timeoutMngr);
        listener = new RTPProxyClientListenerMOCK();
    }

    @Test
    public void processSessionCreated() {
        // Create a update command to be recoverd by the callback handler via
        // the command timeout manager
        UpdateCommand updateCommand = new UpdateCommand();
        updateCommand.setCallbackListener(listener);
        String sessionID = "session_created_sessionid";
        updateCommand.setCallID(sessionID);
        Long appData = 12345L;
        updateCommand.setAppData(appData);
        String cmdCookie = "session_created_cookie";
        updateCommand.setCookie(cmdCookie);
        // Finally add it to the timeout manager
        timeoutMngr.addPendingCommand(updateCommand);

        // Create a fake RTPProxy response message with the session port and IP
        // address
        int sessionPort = 30000;
        String sessionIP = "127.0.0.1";
        String message = cmdCookie + " " + sessionPort + " " + sessionIP;
        InetSocketAddress srcAddr = new InetSocketAddress("localhost", 22222);

        // Burn it!
        callbackHandler.processResponse(cmdCookie, message, srcAddr);

    // TODO [marcoshack] Assert CallbackHandlerTest
    }

    @Test
    public void processSessionUpdated() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected class CommandTimeoutManagerMOCK extends CommandTimeoutManager {

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

    protected class RTPProxyClientListenerMOCK
            implements RTPProxyClientListener {

        public String timeoutSessionID;
        public Object timeoutAppData;
        public String failedSessionID;
        public Object failedAppData;
        public Object failedThrowable;
        public RTPProxySession createdSession;
        public Object createdAppData;
        public RTPProxySession updatedSession;
        public Object updatedAppData;

        public void createSessionTimeout(String sessionID, Object appData) {
            timeoutSessionID = sessionID;
            timeoutAppData = appData;
        }

        public void createSessionFailed(String sessionID, Object appData,
                Throwable t) {
            failedSessionID = sessionID;
            failedAppData = appData;
        }

        public void sessionCreated(RTPProxySession session, Object appData) {
            createdSession = session;
            createdAppData = appData;
        }

        public void sessionUpdated(RTPProxySession session, Object appData) {
            updatedSession = session;
            updatedAppData = appData;
        }
    }
}
