package org.vtlabs.rtpproxy.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vtlabs.rtpproxy.command.Command;
import org.vtlabs.rtpproxy.message.ResponseMessage;
import org.vtlabs.rtpproxy.timeout.TimeoutManager;
import org.vtlabs.rtpproxy.udp.DatagramListener;

/**
 * Handles RTPProxy server response events and forward these events to underling
 * commands.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class CallbackHandler implements DatagramListener {

    private TimeoutManager timeoutManager;
    private Logger log = LoggerFactory.getLogger(CallbackHandler.class);

    public CallbackHandler(TimeoutManager commandManager) {
        this.timeoutManager = commandManager;
    }

    /**
     * Callback method for received datagram messages (DatagramListener).
     *
     * @param ResponseMessage
     */
    public void processResponse(ResponseMessage message) {

        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Processing reponse message ");
            sb.append(message);
            log.debug(sb.toString());
        }

        Command command = timeoutManager.removeCommand(message.getCookie());

        if (!message.isError()) {
            command.processResponse(message.getMessageLine());
            
        } else {
            command.processError(message.getMessageLine());
        }
    }
}
