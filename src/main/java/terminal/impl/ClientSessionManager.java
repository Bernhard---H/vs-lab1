package terminal.impl;

import terminal.SessionManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientSessionManager extends SessionManager {

    private String loggedinUser = null;


    public String getLoggedinUser() {
        return loggedinUser;
    }

    public void setLoggedinUser(String loggedinUser) {
        this.loggedinUser = loggedinUser;
    }
}
