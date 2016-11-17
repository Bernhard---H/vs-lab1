package client;

import network.NetworkException;
import network.impl.PrivateTcpServer;
import network.impl.TcpClient;
import network.model.Address;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import terminal.exceptions.ParseException;
import terminal.impl.ClientSessionManager;
import terminal.impl.ClientUserServant;
import terminal.parser.impl.IpPortParser;
import util.BlockingQueueTimeoutException;
import util.ClientResourceManager;
import util.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;


public class Client implements IClientCli, Runnable {

    private static final Log logger = LogFactory.getLog(Client.class);

    private String componentName;
    private ClientResourceManager rm;

    /**
     * @param componentName the name of the component - represented in the prompt
     * @param config the configuration to use
     * @param userRequestStream the input stream to read user input from
     * @param userResponseStream the output stream to write the console output to
     */
    public Client(String componentName, Config config, InputStream userRequestStream, PrintStream userResponseStream) {
        assert userRequestStream != null;
        assert userResponseStream != null;
        this.componentName = componentName;

        this.rm = new ClientResourceManager(this, new ClientSessionManager(), config, userRequestStream, userResponseStream);
    }

    @Override
    public void run() {
        logger.info("start thread");

        ClientUserServant servant = new ClientUserServant(this.rm, this.componentName);

        this.rm.getThreadManager().execute(servant);

        logger.info("closing thread");
    }

    @Override
    public String login(String username, String password) {
        String response = this.sendToServer("!login " + username + " " + password);
        if (!response.startsWith("ERROR")) {
            this.rm.getSessionManager().setLoggedinUser(username);
        }
        return response;
    }

    @Override
    public String logout() {
        if (this.rm.getSessionManager().getLoggedinUser() == null) {
            return "ERROR unauthorized operation: please log in first";
        }
        String response = this.sendToServer("!logout");
        if (!response.startsWith("ERROR")) {
            this.rm.getSessionManager().setLoggedinUser(null);
        }
        return response;
    }


    @Override
    public String send(String message) {
        if (this.rm.getSessionManager().getLoggedinUser() == null) {
            return "ERROR unauthorized operation: please log in first";
        }
        return this.sendToServer("!send " + message);
    }

    @Override
    public String list() {
        try {
            this.rm.getConnectionManager().getUdpConnection().print("!list");
            String response = this.rm.getConnectionManager().getUdpConnection().read(5, TimeUnit.SECONDS);
            StringBuilder ret = new StringBuilder();
            for (String user : response.split(" ")) {
                ret.append(user).append('\n');
            }
            return ret.toString();
        } catch (NetworkException e) {
            return "ERROR: " + e.getMessage();
        } catch (BlockingQueueTimeoutException e) {
            return "ERROR: request ran into a timeout: " + e.getMessage();
        } catch (InterruptedException e) {
            // ignore and shutdown
        }
        return "";
    }

    @Override
    public String msg(String username, String message) {
        if (this.rm.getSessionManager().getLoggedinUser() == null) {
            return "ERROR unauthorized operation: please log in first";
        }
        String lookup = this.lookup(username);
        if (lookup.startsWith("ERROR")) {
            return lookup;
        }
        Address address;
        try {
            address = (new IpPortParser()).parse(lookup).getAddress();
        } catch (ParseException e) {
            return "ERROR: couldn't parse response of implicit lookup: " + e.getMessage();
        }

        TcpClient client;
        try {
            client = new TcpClient(address);
            this.rm.getThreadManager().execute(client);
        } catch (NetworkException e) {
            return "ERROR: failed to establish connection to other client";
        }
        client.print(this.rm.getSessionManager().getLoggedinUser() + " (private): " + message + "\n");
        try {
            String response = client.read(30, TimeUnit.SECONDS);
            client.closeMe();
            return response;
        } catch (BlockingQueueTimeoutException e) {
            logger.error("timeout while waiting for server to respond", e);
            return "ERROR: timeout - server didn't respond";
        } catch (InterruptedException e) {
            // ignore and shutdown
        }
        return "";
    }

    @Override
    public String lookup(String username) {
        if (this.rm.getSessionManager().getLoggedinUser() == null) {
            return "ERROR unauthorized operation: please log in first";
        }
        return this.sendToServer("!lookup " + username);
    }

    @Override
    public String register(String privateAddress) {
        try {
            IpPortParser parser = new IpPortParser();
            return this.registerAddress(parser.parse(privateAddress).getAddress());
        } catch (ParseException e) {
            return "ERROR: unknown host";
        }
    }

    public String registerAddress(Address privateAddress) {
        if (this.rm.getSessionManager().getLoggedinUser() == null) {
            return "ERROR unauthorized operation: please log in first";
        }
        try {
            PrivateTcpServer tcpServer = new PrivateTcpServer(privateAddress.getPort(), this.rm);
            this.rm.getThreadManager().execute(tcpServer);
        } catch (NetworkException e) {
            logger.error("failed to start tcp server: ", e);
        }

        // register with server
        return this.sendToServer("!register " + privateAddress.format());
    }


    @Override
    public String lastMsg() {
        if (this.rm.getSessionManager().getLoggedinUser() == null) {
            return "ERROR unauthorized operation: please log in first";
        }
        String msg = this.rm.getLastPublicMessage();
        if (msg == null) {
            return "ERROR: No message received!";
        }
        return msg;
    }

    @Override
    public String exit() {
        if (this.rm.getSessionManager().getLoggedinUser() != null) {
            // not sure if this is required
            this.logout();
        }
        this.rm.closeMe();
        return "see you soon";
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


    private String sendToServer(String msg) {
        try {
            this.rm.getConnectionManager().getTcpConnection().print(msg);
            return this.rm.getConnectionManager().getTcpConnection().read(5, TimeUnit.SECONDS);
        } catch (NetworkException e) {
            logger.error("network exception while sending message to server", e);
            return "ERROR: " + e.getMessage();
        } catch (BlockingQueueTimeoutException e) {
            logger.error("timeout while waiting for server to respond", e);
            return "ERROR: timeout - server didn't respond";
        } catch (InterruptedException e) {
            // ignore and shutdown
        }
        return "";
    }

}
