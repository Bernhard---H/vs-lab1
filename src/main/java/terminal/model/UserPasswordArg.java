package terminal.model;

/**
 * @author Bernhard Halbartschlager
 */
public final class UserPasswordArg implements IArguments {

    private String username;
    private String password;

    public UserPasswordArg(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
