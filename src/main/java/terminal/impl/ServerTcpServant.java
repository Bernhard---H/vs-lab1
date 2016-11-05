package terminal.impl;

import terminal.Servant;
import terminal.ServantException;
import terminal.exceptions.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Bernhard Halbartschlager
 */
public final class ServerTcpServant extends Servant implements Runnable {

    private Socket socket;

    private PrintWriter out;


    private Scanner scanner;


    public ServerTcpServant(Socket socket) throws ServantException {
        this.socket = socket;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.scanner = new Scanner(new BufferedReader(new InputStreamReader(socket.getInputStream())));
        } catch (IOException e) {
            throw new ServantConnectionException("streams setup failed", e);
        }
    }

    @Override
    public void println(String msg) {
        out.println(msg);
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
        try {
            while (this.scanner.hasNextLine()) {
                String line = this.scanner.nextLine();
                this.runInput(line);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // do nothing
        }
    }

    @Override
    public void close() {
        super.close();
        if (this.scanner != null) {
            this.scanner.close();
            this.scanner = null;
        }
        if (this.out != null) {
            this.out.close();
            this.out = null;
        }
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.socket = null;
        }
    }


}
