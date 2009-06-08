/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vtlabs.rtpproxy.client.NoServerAvailableException;
import org.vtlabs.rtpproxy.client.RTPProxyClient;
import org.vtlabs.rtpproxy.client.RTPProxyClientConfig;
import org.vtlabs.rtpproxy.client.RTPProxyClientConfigException;
import org.vtlabs.rtpproxy.client.RTPProxyClientConfigurator;
import org.vtlabs.rtpproxy.client.RTPProxyClientListener;
import org.vtlabs.rtpproxy.client.RTPProxySession;
import static org.junit.Assert.*;

/**
 *
 * @author mhack
 */
public class RTPProxyClientTest {
    private RTPProxyClientConfig config;
    private RTPProxyClient client;
    private Listener listener;
    private static Logger log;

    @BeforeClass
    public static void classInit() {
        BasicConfigurator.configure();
        log = LoggerFactory.getLogger(RTPProxyClientTest.class);
    }

    @Before
    public void init() throws RTPProxyClientConfigException, IOException {
        config = RTPProxyClientConfigurator.load("127.0.0.1:22222");
        client = new RTPProxyClient(config);
        listener = new Listener();
    }

    @Test
    public void createSession() throws NoServerAvailableException, 
            InterruptedException {

        String sessionID = UUID.randomUUID().toString();
        Object appData = new Object();

        client.createSession(sessionID, appData, listener);
        Thread.sleep(2000);

        RTPProxySession session = listener.getSession();
        assertNotNull("Session wasn't created", session);

        InetSocketAddress calleeMediaAddr = session.getCalleeMediaAddress();
        assertNotNull("Callee media addresss wasn't created", calleeMediaAddr);
        log.debug("Session created. Callee media address: " + calleeMediaAddr);

        client.updateSession(session, appData, listener);
        Thread.sleep(2000);

        InetSocketAddress callerMediaAddr = session.getCallerMediaAddress();
        assertNotNull("Caller media addresss wasn't created", callerMediaAddr);
        log.debug("Session updated. Caller media address: " + callerMediaAddr);
    }

    private class Listener implements RTPProxyClientListener {
        private RTPProxySession session;
        private Object appData;

        public void createSessionTimeout(String sessionID, Object appData) {
            log.error("Session creation timeout. SessionID = " + sessionID);
        }

        public void createSessionFailed(String sessionID, Object appData,
                Throwable t) {
            log.error("Session creation failed. SessionID = " + sessionID, t);
        }

        public void updateSessionFailed(RTPProxySession session, Object appData,
                Throwable t) {
            log.error("Session update failed. " + session, t);
        }

        public void sessionCreated(RTPProxySession session, Object appData) {
            log.debug("Session created " + session);
            this.session = session;
            this.appData = appData;
        }

        public void sessionUpdated(RTPProxySession session, Object appData) {
            log.debug("Session updated. " + session);
        }

        public RTPProxySession getSession() {
            return session;
        }

        public Object getAppData() {
            return appData;
        }
    }
}
