package network.impl;

import network.NetClient;
import network.NetworkException;
import network.model.Address;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.BlockingQueueTimeoutException;
import util.CloseableBlockingQueue;
import util.IO;
import util.impl.MyCloseableBlockingQueue;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Bernhard Halbartschlager
 */
public final class UdpClient implements NetClient {

    private static final Log logger = LogFactory.getLog(UdpClient.class);

    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private CloseableBlockingQueue<String> msgQueue = new MyCloseableBlockingQueue<>();

    public UdpClient(Address address) throws NetworkException {
        assert address != null;
        try {
            this.address = InetAddress.getByName(address.getHost());
            this.port = address.getPort();
            this.socket = new DatagramSocket();
        } catch (SocketException | UnknownHostException e) {
            throw new TcpClientException("failed to create socket", e);
        }
    }

    @Override
    public void print(String msg) throws NetworkException {
        if (this.socket == null) {
            throw new IllegalStateException("Connection has not been started jet or has been closed");
        }

        try {
            this.socket.send(IO.toUdpPacket(msg, this.address, this.port));
        } catch (IOException e) {
            throw new UdpClientException("failed to send msg", e);
        }
    }

    @Override
    public String read() throws InterruptedException {
        return this.msgQueue.poll();
    }

    public String read(long timeout, TimeUnit unit) throws InterruptedException, BlockingQueueTimeoutException {
        return this.msgQueue.poll(timeout, unit);
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
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String line = IO.interruptableReadln(socket);
                    this.msgQueue.put(line);
                }
            } catch (InterruptedException e) {
                // ingore and exit
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

    @Override
    public synchronized boolean isClosed() {
        return this.socket == null;
    }

    @Override
    public synchronized void closeMe() {
        if (this.socket != null) {
            this.socket.close();
            this.socket = null;
        }
        this.msgQueue.closeMe();
    }


}
