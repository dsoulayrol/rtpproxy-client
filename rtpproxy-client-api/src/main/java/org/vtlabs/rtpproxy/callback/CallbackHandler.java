package org.vtlabs.rtpproxy.callback;

import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vtlabs.rtpproxy.command.Command;
import org.vtlabs.rtpproxy.timeout.TimeoutManager;
import org.vtlabs.rtpproxy.udp.DatagramListener;

/**
 * Handles RTPProxy server response events and forward these events to underling
 * commands.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class CallbackHandler implements DatagramListener {

    private TimeoutManager commandManager;
    private Logger log = LoggerFactory.getLogger(CallbackHandler.class);
    private static Pattern errorPattern = Pattern.compile("^E(.*)$");
    private Matcher errorMatcher;

    public CallbackHandler(TimeoutManager commandManager) {
        this.commandManager = commandManager;
        setupErrorMatcher();
    }

    /**
     * Callback method for received datagram messages (DatagramListener).
     *
     * @param Command cookie
     * @param Response message
     * @param Datagram source address
     */
    public void processResponse(String cookie, String message,
            InetSocketAddress srcAddr) {

        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Processing reponse message ");
            sb.append("\'").append(cookie);
            sb.append(" ").append(message).append("\'");
            sb.append(" received from ").append(srcAddr);
            log.debug(sb.toString());
        }

        Command command = commandManager.removeCommand(cookie);

        if (notError(message)) {
            command.processResponse(message);
            
        } else {
            command.processError(message);
        }
    }

    public boolean notError(String message) {
        return !errorMatcher.reset(message).matches();
    }

    private void setupErrorMatcher() {
        errorMatcher = errorPattern.matcher("");
    }
}
