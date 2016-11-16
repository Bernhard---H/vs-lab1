package chatserver;

import network.NetworkException;
import network.impl.TcpServer;
import network.impl.UdpServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import terminal.impl.ServerUserServant;
import terminal.model.Session;
import util.Config;
import util.ServerResourceManager;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

public class Chatserver implements IChatserverCli, Runnable {

    private static final Log logger = LogFactory.getLog(Chatserver.class);

    private String componentName;
    private ServerResourceManager rm;

    /**
     * @param componentName the name of the component - represented in the prompt
     * @param config the configuration to use
     * @param userRequestStream the input stream to read user input from
     * @param userResponseStream the output stream to write the console output to
     */
    public Chatserver(String componentName, Config config,
                      InputStream userRequestStream, PrintStream userResponseStream) {
        this.componentName = componentName;

        Config users = new Config("user");

        // TODO
        this.rm = new ServerResourceManager(this, config, users, userRequestStream, userResponseStream);
    }

    @Override
    public void run() {
        logger.info("start thread");
        // TODO

        ServerUserServant servant = new ServerUserServant(this.rm, this.componentName);
        this.rm.getThreadManager().execute(servant);

        TcpServer tcpServer = new TcpServer(this.rm);
        this.rm.getThreadManager().execute(tcpServer);

        try {
            int port = this.rm.getConfig().getInt("udp.port");
            UdpServer udpServer = new UdpServer(port, this.rm.getConnectionManager());
            this.rm.getThreadManager().execute(udpServer);
        } catch (NetworkException e) {
            logger.error("failed to start upd server: ", e);
        }

        logger.info("closing thread");
    }

    @Override
    public String users() {
        Config userConfig = this.rm.getUsers();
        ArrayList<String> userNames = new ArrayList<>();

        for (String key : userConfig.listKeys()) {
            userNames.add(key.substring(0, key.length() - ".password".length()));
        }
        Collections.sort(userNames);

        String ret = "";
        int i = 1;
        for (String name : userNames) {
            Session session = this.rm.getConnectionManager().getSession(name);
            ret += i + ". " + name + " ";
            ret += session == null ? "offline" : "online";
            ret += "\n";
            i++;
        }
        return ret;
    }

    @Override
    public String exit() {
        // tell clients to shutdown
        this.rm.getConnectionManager().broadcast(null, "!exit");
        this.rm.closeMe();
        return "see you soon";
    }

    /**
     * @param args the first argument is the name of the {@link Chatserver}
     * component
     */
    public static void main(String[] args) {
        Chatserver chatserver = new Chatserver(args[0],
            new Config("chatserver"), System.in, System.out);
        // no idea how the test framework will start the chatserver
        Thread thread = new Thread(chatserver);
        thread.start();
    }

}
