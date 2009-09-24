package org.vtlabs.rtpproxy.integration;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.UUID;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.vtlabs.rtpproxy.client.RTPProxyClient;
import org.vtlabs.rtpproxy.client.RTPProxySessionState;
import org.vtlabs.rtpproxy.config.RTPProxyClientConfig;
import org.vtlabs.rtpproxy.config.RTPProxyClientConfigurator;
import org.vtlabs.rtpproxy.client.RTPProxyClientListener;
import org.vtlabs.rtpproxy.client.RTPProxySession;
import org.vtlabs.rtpproxy.exception.RTPProxyClientException;
import org.vtlabs.rtpproxy.test.BaseTest;
import static org.junit.Assert.*;

/**
 *
 * @author mhack
 */
public class RTPProxyClientTest extends BaseTest
        implements RTPProxyClientListener {

    private RTPProxyClient client;
    private RTPProxyClientConfig config;
    private RTPProxySession session;
    private boolean wasCreateTimeout;
    private boolean wasUpdateTimeout;
    private boolean wasDestroyTimeout;
    private boolean wasCreateFailed;
    private boolean wasUpdateFailed;
    private boolean wasDestroyFailed;
    private boolean wasDestroyed;

    private static Logger log = Logger.getLogger(RTPProxyClientTest.class);

    /**
     * Test the creation and subsequent destruction of a session using a
     * RTPProxy server bound to localhost:22222.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void createSession() throws Exception {
        config = RTPProxyClientConfigurator.load("127.0.0.1:22222");
        client = new RTPProxyClient(config);
        String sessionID = UUID.randomUUID().toString();
        Object appData = new Object();

        log.info("Creating new session. SessionID = " + sessionID);
        synchronized (this) {
            client.createSession(sessionID, appData, this);
            // We'll be notified when some callback method was called.
            wait();
        }

        assertNotNull("Session wasn't created", session);

        assertNotNull("Callee media address wasn't created",
                session.getCalleeMediaAddress());

        assertNotNull("Caller media address wasn't created",
                session.getCallerMediaAddress());
        

        assertEquals("Invalid session state", RTPProxySessionState.CREATED,
        		session.getState());
        
        log.info("Destroying session " + session);
        synchronized (this) {
            client.destroySession(session, appData, this);
            wait();
        }

        assertTrue("Listener didn't receive destroy callback", wasDestroyed);
        
        assertEquals("Invalid session state", RTPProxySessionState.DESTROYED,
        		session.getState());

        client.terminate();
    }

    /**
     * Test the creation timeout callback trying to create a session using an
     * server that isn't running (remember to don't start rtpproxy in this
     * port ;)
     *
     * @throws java.lang.Exception
     */
    @Test
    public void createSessionTimeout() throws Exception {
        config = RTPProxyClientConfigurator.load("127.0.0.1:22200");
        client = new RTPProxyClient(config);
        String sessionID = UUID.randomUUID().toString();
        Object appData = new Object();

        log.info("Creating new session. SessionID = " + sessionID);
        synchronized (this) {
            client.createSession(sessionID, appData, this);
            wait();
        }

        client.terminate();

        assertTrue("Listener didn't receive timeout callback",
                wasCreateTimeout);
    }

    //
    // Callback methods implemented from RTPProxyClientListener
    //
    public void sessionCreated(RTPProxySession session, Object appData) {
        log.debug("Session created: " + session);
        this.session = session;

        try {
            log.info("Updating session " + session);
            client.updateSession(session, appData, this);

        } catch (RTPProxyClientException e) {
            log.error("Error trying to update session.", e);
        }
    }

    public void sessionUpdated(RTPProxySession session, Object appData) {
        log.debug("Session creation completed: " + session);
        wakeup();
    }

    public void createSessionTimeout(String sessionID, Object appData) {
        log.debug("Session creation timeout: " + sessionID);
        wasCreateTimeout = true;
        wakeup();
    }

    public void updateSessionTimeout(RTPProxySession session, Object appData) {
        log.debug("Session update timeout: " + session);
        wasUpdateTimeout = true;
        wakeup();
    }

    public void createSessionFailed(String sessionID, Object appData,
            Throwable t) {
        log.debug("Session creation failed: " + sessionID, t);
        wasCreateFailed = true;
        wakeup();
    }

    public void updateSessionFailed(RTPProxySession session, Object appData,
            Throwable t) {
        log.debug("Session update failed: " + session, t);
        wasUpdateFailed = true;
        wakeup();
    }

    private void wakeup() {
        synchronized (this) {
            notify();
        }
    }

    public void sessionDestroyed(RTPProxySession session, Object appData) {
        wasDestroyed = true;
        wakeup();
    }

    public void destroySessionTimeout(RTPProxySession session, Object appData) {
        wasDestroyTimeout = true;
        wakeup();
    }

    public void destroySessionFailed(RTPProxySession session, Object appData, Throwable t) {
        wasDestroyFailed = true;
        wakeup();
    }
}
