/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.test.callback;

import java.net.InetSocketAddress;
import org.junit.Before;
import org.junit.Test;
import org.vtlabs.rtpproxy.callback.CallbackHandler;
import org.vtlabs.rtpproxy.client.RTPProxyServer;
import org.vtlabs.rtpproxy.client.RTPProxySession;
import org.vtlabs.rtpproxy.command.UpdateCommand;
import org.vtlabs.rtpproxy.test.BaseTest;
import org.vtlabs.rtpproxy.mock.client.RTPProxyClientListenerMOCK;
import org.vtlabs.rtpproxy.mock.command.CommandTimeoutManagerMOCK;
import static org.junit.Assert.*;

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
        String sessionID = "session_created_sessionid";
        Long appData = 12345L; // A long object to be used as AppData.
        String cmdCookie = "session_created_cookie";
        String fromTag = "session_created_fromtag";
        String toTag = "session_created_totag";
        InetSocketAddress srvAddr = new InetSocketAddress("127.0.0.1", 22222);
        RTPProxyServer server = new RTPProxyServer();
        server.setAddress(srvAddr);

        // Create a update command to be recovered by the callback handler via
        // the command timeout manager
        UpdateCommand updateCommand = new UpdateCommand(callbackHandler);
        updateCommand.setCallbackListener(listener);
        updateCommand.setAppData(appData);
        updateCommand.setCallID(sessionID);
        updateCommand.setCookie(cmdCookie);
        updateCommand.setFromTag(fromTag);
        updateCommand.setToTag(toTag);
        updateCommand.setServer(server);

        // Finally add the command to the timeout manager
        timeoutMngr.addPendingCommand(updateCommand);

        // Create a fake RTPProxy response message with the session port and IP
        // address
        int sessionPort = 30000;
        String sessionIP = "127.0.0.1";
        String message = sessionPort + " " + sessionIP;


        // Burn it!
        callbackHandler.processResponse(cmdCookie, message, srvAddr);

        // Check
        assertTrue("Listener didn't receive a 'create' event",
                listener.isCreate);

        assertFalse("Listener received an invalid 'fail' event",
                listener.isCreateFail);

        assertFalse("Listener received an invalid 'update' event",
                listener.isUpdate);

        assertFalse("Listener received an invalid 'update fail' event",
                listener.isUpdateFail);

        assertFalse("Listener received an invalid 'timeout' event",
                listener.isTimeout);

        RTPProxySession session = listener.createdSession;
        assertNotNull("RTP session wasn't created", listener.createdSession);

        assertNotNull("Session server wasn't associated", session.getServer());

        assertEquals("Invalid session server", server, session.getServer());

        assertNotNull("Callee media address wasn't created",
                session.getCalleeMediaAddress());

        assertEquals("Invalid callee media IP address",
                sessionIP,
                session.getCalleeMediaAddress().getAddress().getHostAddress());

        assertEquals("Invalid callee media port",
                sessionPort, session.getCalleeMediaAddress().getPort());

        assertEquals("Invalid AppData for 'create' event", appData,
                listener.createdAppData);
    }

    @Test
    public void processCreateSessionFailed() {
        String sessionID = "create_error_sessionid";
        Long appData = 12345L; // A long object to be used as AppData.
        String cmdCookie = "create_error_cookie";
        String fromTag = "create_error_fromtag";
        String toTag = "create_error_totag";
        InetSocketAddress srvAddr = new InetSocketAddress("127.0.0.1", 22222);
        RTPProxyServer server = new RTPProxyServer();
        server.setAddress(srvAddr);

        // Create a update command to be recovered by the callback handler via
        // the command timeout manager
        UpdateCommand updateCommand = new UpdateCommand(callbackHandler);
        updateCommand.setCallbackListener(listener);
        updateCommand.setAppData(appData);
        updateCommand.setCallID(sessionID);
        updateCommand.setCookie(cmdCookie);
        updateCommand.setFromTag(fromTag);
        updateCommand.setToTag(toTag);
        updateCommand.setServer(server);

        // Finally add the command to the timeout manager
        timeoutMngr.addPendingCommand(updateCommand);

        // Fake RTPProxy error message in the format /^E(.*)$/
        String message = "E3";

        // Burn it!
        callbackHandler.processResponse(cmdCookie, message, srvAddr);

        // Check
        assertTrue("Listener didn't received a 'create failed' event",
                listener.isCreateFail);

        assertFalse("Listener receive an invalid 'update fail' event",
                listener.isUpdateFail);

        assertFalse("Listener receive an invalid 'update' event",
                listener.isUpdate);
                
        assertFalse("Listener received an invalid 'create' event",
                listener.isCreate);

        assertFalse("Listener received an invalid 'timeout' event",
                listener.isTimeout);

        assertEquals("Invalid session ID", sessionID,
                listener.createFailedSessionID);

        assertEquals("Invalid AppData", appData,
                listener.createFailedAppData);

        assertNotNull("Invalid exception", listener.createFailedThrowable);

        log.debug("Create failed throwable:\n" +
                listener.createFailedThrowable.toString());
    }

    @Test
    public void processSessionUpdated() {
        String sessionID = "session_updated_sessionid";
        Long appData = 12345L; // A long object to be used as AppData.
        String cmdCookie = "session_updated_cookie";
        InetSocketAddress srvAddr = new InetSocketAddress("127.0.0.1", 22222);
        RTPProxyServer server = new RTPProxyServer();
        server.setAddress(srvAddr);

        // From and To tags are the inverse of the udpate command to create the
        // session
        String toTag = "session_updated_fromtag";
        String fromTag = "session_updated_totag";

        // Update command has an RTPProxy session with a not null callee media
        // address
        RTPProxySession session = new RTPProxySession();
        session.setSessionID(sessionID);
        session.setServer(server);
        InetSocketAddress calleeMediaAddr =
                new InetSocketAddress("127.0.0.1", 3000);
        session.setCalleeMediaAddress(calleeMediaAddr);

        // Create a update command to be recovered by the callback handler via
        // the command timeout manager
        UpdateCommand updateCommand = new UpdateCommand(session, callbackHandler);
        updateCommand.setCallbackListener(listener);
        updateCommand.setAppData(appData);
        updateCommand.setCallID(sessionID);
        updateCommand.setCookie(cmdCookie);
        updateCommand.setFromTag(fromTag);
        updateCommand.setToTag(toTag);
        updateCommand.setServer(server);

        // Finally add the command to the timeout manager
        timeoutMngr.addPendingCommand(updateCommand);

        // Create a fake RTPProxy response message with the session port and IP
        // address
        int sessionPort = 30002;
        String sessionIP = "127.0.0.1";
        String message = sessionPort + " " + sessionIP;

        // Burn it!
        callbackHandler.processResponse(cmdCookie, message, srvAddr);

        // Check
        assertTrue("Listener didn't receive an 'update' event",
                listener.isUpdate);

        assertFalse("Listener received an invalid 'update failed' event",
                listener.isUpdateFail);

        assertFalse("Listener received an invalid 'create' event",
                listener.isCreate);

        assertFalse("Listener received an invalid 'create fail' event",
                listener.isCreateFail);

        assertFalse("Listener received an invalid 'timeout' event",
                listener.isTimeout);

        RTPProxySession updatedSession = listener.updatedSession;
        assertNotNull("RTP session wasn't updated", updatedSession);

        assertEquals("Invalid session passed to update callback method",
                session, updatedSession);

        assertNotNull("Caller media address wasn't created",
                session.getCallerMediaAddress());

        assertEquals("Invalid caller media IP address",
                sessionIP,
                session.getCallerMediaAddress().getAddress().getHostAddress());

        assertEquals("Invalid caller media port",
                sessionPort, session.getCallerMediaAddress().getPort());

        assertEquals("Invalid AppData for 'create' event", appData,
                listener.updatedAppData);
    }

    @Test
    public void processUpdateSessionFailed() {
        String sessionID = "update_fail_sessionid";
        Long appData = 12345L; // A long object to be used as AppData.
        String cmdCookie = "update_fail_cookie";
        InetSocketAddress srvAddr = new InetSocketAddress("127.0.0.1", 22222);
        RTPProxyServer server = new RTPProxyServer();
        server.setAddress(srvAddr);

        // From and To tags are the inverse of the udpate command to create the
        // session
        String toTag = "update_fail_fromtag";
        String fromTag = "update_fail_totag";

        // Update command has an RTPProxy session with a not null callee media
        // address
        RTPProxySession session = new RTPProxySession();
        session.setSessionID(sessionID);
        session.setServer(server);
        InetSocketAddress calleeMediaAddr =
                new InetSocketAddress("127.0.0.1", 3000);
        session.setCalleeMediaAddress(calleeMediaAddr);

        // Create a update command to be recovered by the callback handler via
        // the command timeout manager
        UpdateCommand updateCommand = new UpdateCommand(session, callbackHandler);
        updateCommand.setCallbackListener(listener);
        updateCommand.setAppData(appData);
        updateCommand.setCallID(sessionID);
        updateCommand.setCookie(cmdCookie);
        updateCommand.setFromTag(fromTag);
        updateCommand.setToTag(toTag);
        updateCommand.setServer(server);

        // Finally add the command to the timeout manager
        timeoutMngr.addPendingCommand(updateCommand);

        // Fake RTPProxy error response for the update command
        String message = "E3";

        // Burn it!
        callbackHandler.processResponse(cmdCookie, message, srvAddr);

        // Check
        assertTrue("Listener didn't received an 'update failed' event",
                listener.isUpdateFail);

        assertFalse("Listener receive an invalid 'update' event",
                listener.isUpdate);

        assertFalse("Listener received an invalid 'create' event",
                listener.isCreate);

        assertFalse("Listener received an invalid 'create fail' event",
                listener.isCreateFail);

        assertFalse("Listener received an invalid 'timeout' event",
                listener.isTimeout);

        assertEquals("Invalid session passed to update failed callback method",
                session, listener.updateFailedSession);

        assertEquals("Invalid AppData", appData, listener.updateFailedAppData);

        assertNotNull("Invalid exception", listener.updateFailedThrowable);

        log.debug("Update failed throwable:\n" +
                listener.updateFailedThrowable.toString());
    }

    @Test
    public void processCommandTimeout() {
        String sessionID = "command_timeout_sessionid";
        Object appData = new Object();

        UpdateCommand command = new UpdateCommand(callbackHandler);
        command.setSessionID(sessionID);
        command.setAppData(appData);
        command.setCallbackListener(listener);

        callbackHandler.commandTimeout(command);

        // Check
        assertTrue("Listener didn't receive a 'timeout' event",
                listener.isTimeout);

        assertFalse("Listener receive an invalid 'update' event",
                listener.isUpdate);

        assertFalse("Listener received an invalid 'update failed' event",
                listener.isUpdateFail);

        assertFalse("Listener received an invalid 'create' event",
                listener.isCreate);

        assertFalse("Listener received an invalid 'fail' event",
                listener.isCreateFail);

        assertEquals("Invalid AppData", appData, listener.timeoutAppData);

        assertEquals("Invalid session ID", sessionID,
                listener.timeoutSessionID);
    }
}
