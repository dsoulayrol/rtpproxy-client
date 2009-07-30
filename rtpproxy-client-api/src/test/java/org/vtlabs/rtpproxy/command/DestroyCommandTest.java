package org.vtlabs.rtpproxy.command;

import org.junit.Test;
import org.vtlabs.rtpproxy.client.RTPProxySessionImpl;
import static org.junit.Assert.*;

/**
 *
 * @author mhack
 */
public class DestroyCommandTest {

    @Test
    public void getMessage() {
        String sessionID = "get_message_sessionid";
        String fromTag = "from_tag";
        String toTag = "to_tag";

        RTPProxySessionImpl session = new RTPProxySessionImpl();
        session.setSessionID(sessionID);

        DestroyCommand cmd = new DestroyCommand(session);
        cmd.setFromTag(fromTag);
        cmd.setToTag(toTag);

        // expected message
        StringBuilder sbMsg = new StringBuilder(cmd.getCookie());
        sbMsg.append(" D ").append(sessionID);
        sbMsg.append(" ").append(fromTag);
        sbMsg.append(" ").append(toTag);

        // command message
        String cmdMsg = cmd.getMessage();

        assertEquals("Invalid command message", sbMsg.toString(), cmdMsg);
    }
}
