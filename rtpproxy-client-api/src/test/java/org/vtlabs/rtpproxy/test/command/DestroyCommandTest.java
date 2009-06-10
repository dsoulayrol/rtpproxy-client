package org.vtlabs.rtpproxy.test.command;

import org.junit.Test;
import org.vtlabs.rtpproxy.command.DestroyCommand;
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

        DestroyCommand cmd = new DestroyCommand(null);
        cmd.setSessionID(sessionID);
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
