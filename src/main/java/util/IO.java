package util;

import network.NetworkException;
import network.impl.UdpLimitExceededException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Bernhard Halbartschlager
 */
public final class IO {

    private static final Log logger = LogFactory.getLog(IO.class);

    public static String interruptableReadln(InputStream inputStream, Scanner scanner) throws InterruptedException, IOException {
        while (!Thread.currentThread().isInterrupted()) {
            if (inputStream.available() > 0) {
                try {
                    // try to read a line
                    // this is probably a blocking operation
                    String line = scanner.nextLine();
                    if (!line.trim().isEmpty()) {
                        // ignore input if only whitespace
                        return line;
                    }
                } catch (NoSuchElementException e) {
                    // not a (full) line available jet
                    Thread.sleep(500);
                }
            } else {
                Thread.sleep(500);
            }
        }
        throw new InterruptedException();
    }

    public static String interruptableReadln(DatagramSocket socket) throws InterruptedException, IOException {
        socket.setSoTimeout(500);
        while (!Thread.currentThread().isInterrupted()) {
            try {
                byte[] buf = new byte[64 * 1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                return new String(packet.getData(), 0, packet.getLength(), "UTF-8");
            } catch (SocketTimeoutException e) {
                // check if thread has been interrupted and try again
            }
        }
        throw new InterruptedException();
    }

    public static Scanner toScanner(InputStream inputStream) {
        return new Scanner(new BufferedReader(new InputStreamReader(inputStream)));
    }

    public static DatagramPacket toUdpPacket(String msg, InetAddress address, int port) throws NetworkException {
        byte[] buf = new byte[0];
        try {
            buf = msg.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            assert false;
            e.printStackTrace();
        }
        if (buf.length > 63 * 1024) {
            // max safe size is:  64KiB - headers
            // source: http://stackoverflow.com/a/9235558
            throw new UdpLimitExceededException("maximum msg safe size of datagram packet is 63 kb");
        }

        return new DatagramPacket(buf, buf.length, address, port);
    }

}
