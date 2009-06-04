/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.command;

/**
 *
 * @author mhack
 */
public class CreateSessionCommand extends Command {

    private String callID;
    private String address;
    private int port;
    private String fromTag;
    private String toTag;

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder("U ");
        sb.append(callID);
        sb.append(" ").append(address);
        sb.append(" ").append(port);
        sb.append(" ").append(fromTag);
        sb.append(" ").append(toTag);
        return sb.toString();
    }
}
