package chatserver;

import terminal.impl.ServerSessionManager;
import util.Config;
import util.ServerResourceManager;

import java.io.InputStream;
import java.io.PrintStream;

public class Chatserver implements IChatserverCli, Runnable {

	private String componentName;
	private Config config;
	private InputStream userRequestStream;
	private PrintStream userResponseStream;
    private ServerResourceManager rm;

	/**
	 * @param componentName
	 *            the name of the component - represented in the prompt
	 * @param config
	 *            the configuration to use
	 * @param userRequestStream
	 *            the input stream to read user input from
	 * @param userResponseStream
	 *            the output stream to write the console output to
	 */
	public Chatserver(String componentName, Config config,
			InputStream userRequestStream, PrintStream userResponseStream) {
		this.componentName = componentName;
		this.config = config;
		this.userRequestStream = userRequestStream;
		this.userResponseStream = userResponseStream;

		// TODO
        this.rm = new ServerResourceManager(this, new ServerSessionManager());
	}

	@Override
	public void run() {
		// TODO
	}

	@Override
	public String users() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exit() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 *            the first argument is the name of the {@link Chatserver}
	 *            component
	 */
	public static void main(String[] args) {
		Chatserver chatserver = new Chatserver(args[0],
				new Config("chatserver"), System.in, System.out);
		// no idea how the test framework will start the chatserver
		Thread thread = new Thread(chatserver);
		thread.start();
	}

}
