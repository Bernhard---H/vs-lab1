package network.impl;

import network.NetServer;
import network.impl.tcpStates.InitState;
import network.impl.tcpStates.State;

/**
 * @author Bernhard Halbartschlager
 */
public final class TcpServer implements NetServer {


    private State currentState = new InitState();


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


            while (!Thread.currentThread().isInterrupted() && this.currentState != null) {
                // run the action of the current state
                this.currentState = this.currentState.run();





            }

        } catch (Exception e) {
            // make sure exception doesn't get swallowed
            e.printStackTrace();
        } finally {
            this._close();
        }
    }




    @Override
    public void close() throws Exception {
        this._close();
    }

    /**
     * idempotent version of this.close()
     */
    private void _close() {
        if (this.currentState != null) {
            this.currentState.close();
            this.currentState = null;
        }
    }

}
