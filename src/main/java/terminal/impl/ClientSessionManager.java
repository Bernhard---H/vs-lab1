package terminal.impl;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientSessionManager {

    private String loggedinUser = null;


    public String getLoggedinUser() {
        return loggedinUser;
    }

    public void setLoggedinUser(String loggedinUser) {
        this.loggedinUser = loggedinUser;
    }
}
