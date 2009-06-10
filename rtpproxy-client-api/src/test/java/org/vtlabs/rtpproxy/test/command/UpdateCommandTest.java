package org.vtlabs.rtpproxy.test.command;

import java.net.InetSocketAddress;
import org.junit.Test;
import org.vtlabs.rtpproxy.command.UpdateCommand;
import org.vtlabs.rtpproxy.test.BaseTest;
import static org.junit.Assert.*;

/**
 *
 * @author mhack
 */
public class UpdateCommandTest extends BaseTest {

    /**
     * Test the creation of command message with all fields set.
     */
    @Test
    public void getMessageFull() {
        String sessionID = "get_message_sessionid";
        String fromTag = "from_tag";
        String toTag = "to_tag";
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 4100);

        UpdateCommand updateCmd = new UpdateCommand(null);
        updateCmd.setSessionID(sessionID);
        updateCmd.setFromTag(fromTag);
        updateCmd.setToTag(toTag);
        updateCmd.setAddress(address);

        // expected message
        StringBuilder message = new StringBuilder(updateCmd.getCookie());
        message.append(" U ").append(sessionID);
        message.append(" ").append(address.getAddress().getHostAddress());
        message.append(" ").append(address.getPort());
        message.append(" ").append(fromTag);
        message.append(" ").append(toTag);

        // message created by the command
        String cmdMessage = updateCmd.getMessage();

        assertEquals("Invalid command message", message.toString(), cmdMessage);
    }

    /**
     * Test the creation of command message with the fields used to create a new
     * session (address and port not set).
     */
    @Test
    public void getMessageCreate() {
        String sessionID = "get_message_sessionid";
        String fromTag = "from_tag";
        String toTag = "to_tag";

        UpdateCommand updateCmd = new UpdateCommand(null);
        updateCmd.setSessionID(sessionID);
        updateCmd.setFromTag(fromTag);
        updateCmd.setToTag(toTag);

        // expected message
        StringBuilder message = new StringBuilder(updateCmd.getCookie());
        message.append(" U ").append(sessionID);
        message.append(" ").append(0);
        message.append(" ").append(0);
        message.append(" ").append(fromTag);
        message.append(" ").append(toTag);

        // message created by the command
        String cmdMessage = updateCmd.getMessage();

        assertEquals("Invalid command message", message.toString(), cmdMessage);
    }
}
