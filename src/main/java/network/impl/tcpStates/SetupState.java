package network.impl.tcpStates;

import network.NetworkException;
import network.impl.TcpSetupException;
import util.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.MissingResourceException;

/**
 * @author Bernhard Halbartschlager
 */
public final class SetupState implements State {

    /**
     * cache config for all instances of the tcp server
     */
    private static Config config = null;

    private int tcpPort;

    private ServerSocket serverSocket = null;


    private void loadConfig() throws NetworkException {
        try {
            if (config == null) {
                // load
                config = new Config("chatserver.properties");
            }

            this.tcpPort = config.getInt("tcp.port");
        } catch (MissingResourceException e) {
            // new Config() failed   ||  could not find "tcp.port"
            throw new TcpSetupException("Failed to load config", e);
        } catch (NumberFormatException e) {
            // failed to read port
            throw new TcpSetupException("illegal format for tcp.port in config", e);
        }
    }

    private void createSocket() throws NetworkException {
        try {
            this.serverSocket = new ServerSocket(this.tcpPort);
            // enables thread to be interrupted
            // 500ms ... half a second
            this.serverSocket.setSoTimeout(500);
        } catch (IOException e) {
            throw new TcpSetupException("creating the tcp socket failed", e);
        }
    }

    @Override
    public State run() throws NetworkException {
        this.loadConfig();
        this.createSocket();
        return new WaitForClientState(this.serverSocket);
    }

    /**
     * must be callable multiple times and must not throw exceptions
     */
    @Override
    public void close() {
        if (this.serverSocket != null) {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.serverSocket = null;
            }
        }
    }
}
