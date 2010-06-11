package org.vtlabs.rtpproxy.command;

import java.net.InetSocketAddress;

import org.vtlabs.rtpproxy.client.RTPProxySessionImpl;
import org.vtlabs.rtpproxy.client.RTPProxySessionState;

public class RecordCommand extends CreateCommand {
    
    	private String fileName;    	
    
	public String getFileName() {
	    return fileName;
	}

	public void setFileName(String fileName) {
	    this.fileName = fileName;
	}

	/**
	 * Command format: 'cookie C[args] callid recordFileName from_tag to_tag'
	 */
	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder(getCookie());
		sb.append(" C ");
		sb.append(getSessionID());
		sb.append(" ");
		sb.append(getFileName());		
		String fromTag = getFromTag();
		sb.append(" ").append(fromTag != null ? fromTag : 0);
		String toTag = getToTag();
		sb.append(" ").append(toTag != null ? toTag : 0);		
		return sb.toString();
	}

	@Override
	public void processTimeout() {
		log.debug("Record Command Timeout");
		listener.createRecordableSessionTimeout(sessionID, server);

	}

	@Override
	public void processResponse(String message) {		
		listener.recordableSessionCreated(sessionID, appData);
	}

	@Override
	public void processError(String message) {
		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder("Session creation failed: ");
			sb.append(message);
			log.debug(sb.toString());
		}

		listener.createRecordableSessionFailed(message, message);
	}

}
