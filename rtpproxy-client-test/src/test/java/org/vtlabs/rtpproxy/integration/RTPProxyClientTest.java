package org.vtlabs.rtpproxy.integration;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vtlabs.rtpproxy.client.RTPProxyClient;
import org.vtlabs.rtpproxy.client.RTPProxyClientListener;
import org.vtlabs.rtpproxy.client.RTPProxySession;
import org.vtlabs.rtpproxy.client.RTPProxySessionState;
import org.vtlabs.rtpproxy.config.RTPProxyClientConfig;
import org.vtlabs.rtpproxy.config.RTPProxyClientConfigurator;
import org.vtlabs.rtpproxy.exception.RTPProxyClientException;

/**
 * 
 * @author mhack
 */
public class RTPProxyClientTest implements RTPProxyClientListener {

	private static Logger log;
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
	private boolean wasSessionRecordableTimeout;
	private boolean wasSessionRecordableCreate;
	private boolean wasSessionRecordableFailed;
	private static Properties environment;
	private static String rtpProxyIp;
	private static String rtpProxyPort;
	private static String rtpProxyInvalidPort;
	private static String rtpProxyDirectoryTemp;
	private static String rtpProxyDirectoryFiles;
	private static String fromTagFileName;
	private static String toTagFileName;

	@BeforeClass
	public static void initClass() {
		String configFile = "conf/log4j.properties";
		System.setProperty("log4j.configuration", "file:" + configFile);
		log = LoggerFactory.getLogger(RTPProxyClientTest.class);

		/*
		 * Insertion of "conf/env.properties" in this project. This file
		 * contains informations about the RTPProxy machine (directories,
		 * filenames, ip, etc.)
		 * 
		 * @author Rodrigo Ribeiro
		 */

		File configurations = new File("conf/env.properties");
		environment = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(configurations);
			environment.load(fis);
			fis.close();
		} catch (IOException ex) {
			log.info("The file 'env.properties' doesn't exist in directory '/conf' of this project.");
		}
		rtpProxyIp = environment.getProperty("rtpproxy.ip");
		rtpProxyPort = environment.getProperty("rtpproxy.port");
		rtpProxyInvalidPort = environment.getProperty("rtpproxy.port.invalid");
		rtpProxyDirectoryTemp = environment.getProperty("rtpproxy.directory.temp");
		rtpProxyDirectoryFiles = environment.getProperty("rtpproxy.directory.files");
		fromTagFileName = environment.getProperty("fromTagFileName");
		toTagFileName = environment.getProperty("toTagFileName");
	}

	/**
	 * Test the creation and subsequent destruction of a session using a
	 * RTPProxy server bound to localhost:22222.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void createSession() throws Exception {
		config = RTPProxyClientConfigurator.load(rtpProxyIp + ":"+ rtpProxyPort);
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

		assertNotNull("Callee media address wasn't created", session.getCalleeMediaAddress());

		assertEquals("Invalid session state", RTPProxySessionState.CREATED,session.getState());

		log.info("Destroying session " + session);
		synchronized (this) {
			client.destroySession(session, appData, this);
			wait();
		}

		assertTrue("Listener didn't receive destroy callback", wasDestroyed);

		assertEquals("Invalid session state", RTPProxySessionState.DESTROYED,session.getState());

		client.terminate();
	}

	@Test
	public void createRecordableSession() throws Exception {
		config = RTPProxyClientConfigurator.load(rtpProxyIp + ":"+ rtpProxyPort);
		client = new RTPProxyClient(config);
		String sessionID = UUID.randomUUID().toString();
		Object appData = new Object();

		log.info("Creating new session. Session ID = " + sessionID);
		synchronized (this) {
			client.createSession(sessionID, appData, this);
			wait();
		}

		log.info("Update the session. SessionID = " + session.getSessionID());
		synchronized (this) {
			client.updateSession(session, appData, this);
			wait();
		}

		log.info("Record the session. SessionID = " + session.getSessionID()
				+ " isOwner = " + true);
		synchronized (this) {
			client.recordSession(sessionID, appData, this, fromTagFileName,true);
			wait();
		}

		log.info("Record the session. SessionID = " + session.getSessionID()
				+ " isOwner = " + false);
		synchronized (this) {
			client.recordSession(sessionID, appData, this, toTagFileName,false);
			wait();
		}

		log.info("Destroying session " + session);
		synchronized (this) {
			client.destroySession(session, appData, this);
			wait();
		}

		assertTrue("Listener didn't receive destroy callback", wasDestroyed);

		assertEquals("Invalid session state", RTPProxySessionState.DESTROYED,session.getState());

		client.terminate();

	}

	/**
	 * This method validates if the files are present on Record Files directory
	 * 
	 * @author Rodrigo Ribeiro
	 */
	@Test
	public void validateRecordableFiles() {

		File fromTagfile = new File(rtpProxyDirectoryFiles + fromTagFileName+ ".rtp");
		File toTagFile = new File(rtpProxyDirectoryFiles + toTagFileName+ ".rtp");
		File tempDir = new File(rtpProxyDirectoryTemp);

		log.info("File " + fromTagFileName + ".rtp exists on directory files: "+ fromTagfile.exists());
		log.info("File " + toTagFileName + ".rtp exists on directory files: "+ toTagFile.exists());

		assertTrue("File of FromTag doesn't exist", fromTagfile.exists());
		assertTrue("File of ToTag doesn't exist", toTagFile.exists());
		assertTrue("Files still exist in the temp directory",tempDir.list().length == 0);
	}

	/**
	 * Test the creation timeout callback trying to create a session using an
	 * server that isn't running (remember to don't start rtpproxy in this port
	 * ;)
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void createSessionTimeout() throws Exception {
		config = RTPProxyClientConfigurator.load(rtpProxyIp + ":"+ rtpProxyInvalidPort);
		client = new RTPProxyClient(config);
		String sessionID = UUID.randomUUID().toString();
		Object appData = new Object();

		log.info("Creating new session. SessionID = " + sessionID);
		synchronized (this) {
			client.createSession(sessionID, appData, this);
			wait();
		}

		client.terminate();

		assertTrue("Listener didn't receive timeout callback", wasCreateTimeout);
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

	public void destroySessionFailed(RTPProxySession session, Object appData,
			Throwable t) {
		wasDestroyFailed = true;
		wakeup();
	}

	public void recordableSessionCreated(String sessionID, Object appData) {
		wasSessionRecordableCreate = true;
		wakeup();

	}

	public void createRecordableSessionFailed(String sessionID, Object appData) {
		wasSessionRecordableFailed = true;
		wakeup();
	}

	public void createRecordableSessionTimeout(String sessionID, Object appData) {
		wasSessionRecordableTimeout = true;
		wakeup();
	}
}
