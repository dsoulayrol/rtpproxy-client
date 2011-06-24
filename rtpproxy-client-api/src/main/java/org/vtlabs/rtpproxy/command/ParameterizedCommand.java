package org.vtlabs.rtpproxy.command;


/**
 * A specialized {@link Command} abstract parent for commands that can
 * accept optional arguments, like Update ({@link CreateCommand} and
 * {@link UpdateCommand}), Lookup, Start Media Playback, or Version Query.
 *
 * @author David Soulayrol <david.soulayrol@gmail.com>
 */
public abstract class ParameterizedCommand extends Command {

    /**
     * Optional arguments that can be passed to commands.
     */
    private String parameters;

    public ParameterizedCommand() {
    	super();
    	parameters = null;
    }

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
}
