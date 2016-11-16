package network.impl;

import network.NetworkException;
import network.ServerConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.CloseMe;
import util.IO;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.List;

/**
 * @author Bernhard Halbartschlager
 */
public final class UdpServer implements Runnable, CloseMe {

    private static final Log logger = LogFactory.getLog(UdpServer.class);

    private DatagramSocket socket;
    private ServerConnectionManager connectionManager;

    public UdpServer(int port, ServerConnectionManager connectionManager) throws NetworkException {
        assert connectionManager != null;
        this.connectionManager = connectionManager;
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            throw new TcpClientException("failed to create socket", e);
        }
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        logger.info("start thread");
        try {
            socket.setSoTimeout(200);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    byte[] buf = new byte[64 * 1024];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String line = new String(packet.getData(), 0, packet.getLength(), "UTF-8");

                    if (line.startsWith("!list")){
                        List<String> users = this.connectionManager.getOnlineUsers();
                        Collections.sort(users);

                        StringBuilder response = new StringBuilder();
                        for (String user:users){
                            response.append(user).append(' ');
                        }
                        this.socket.send(IO.toUdpPacket(response.toString(), packet.getAddress(), packet.getPort()));
                    }
                } catch (SocketTimeoutException e) {
                    // check if thread has been interrupted and try again
                } catch (NetworkException e) {
                    logger.error("exception while sending repsonse", e);
                }
            }
        } catch (SocketException e) {
            // ignore: socket has been closed
        } catch (IOException e) {
            logger.info(e);
        } finally {
            this.closeMe();
        }
        logger.info("closing thread");
    }


    public synchronized void closeMe() {
        if (this.socket != null) {
            this.socket.close();
            this.socket = null;
        }
    }


}
