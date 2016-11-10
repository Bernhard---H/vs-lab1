package client;

import terminal.impl.ClientSessionManager;
import terminal.impl.ClientUserServant;
import util.ClientResourceManager;
import util.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class Client implements IClientCli, Runnable {

    private String componentName;
    private Config config;
    private ClientResourceManager rm;

    /**
     * @param componentName the name of the component - represented in the prompt
     * @param config the configuration to use
     * @param userRequestStream the input stream to read user input from
     * @param userResponseStream the output stream to write the console output to
     */
    public Client(String componentName, Config config,
                  InputStream userRequestStream, PrintStream userResponseStream) {
        assert userRequestStream != null;
        assert userResponseStream != null;
        this.componentName = componentName;
        this.config = config;
        //this.userRequestStream = userRequestStream;
        //this.userResponseStream = userResponseStream;

        // TODO
        this.rm = new ClientResourceManager(this, new ClientSessionManager(), userRequestStream, userResponseStream);
    }

    @Override
    public void run() {
        // TODO
        ClientUserServant servant = new ClientUserServant(this.rm, this.componentName);

    }

    @Override
    public String login(String username, String password) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String logout() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String send(String message) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String list() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String msg(String username, String message) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String lookup(String username) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String register(String privateAddress) {
        try {
            InetAddress address = InetAddress.getByName(privateAddress);
            return this.registerAddress(address);
        } catch (UnknownHostException e) {
            return "ERROR: unknown host";
        }
    }

    public String registerAddress(InetAddress privateAddress) {
        // todo: implement
        return null;
    }


    @Override
    public String lastMsg() {
        return this.rm.getLastPublicMessage();
    }

    @Override
    public String exit() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param args the first argument is the name of the {@link Client} component
     */
    public static void main(String[] args) {
        Client client = new Client(args[0], new Config("client"), System.in, System.out);
        // no idea how the test framework will start the client
        Thread thread = new Thread(client);
        thread.start();
    }

    // --- Commands needed for Lab 2. Please note that you do not have to
    // implement them for the first submission. ---

    @Override
    public String authenticate(String username) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

}
