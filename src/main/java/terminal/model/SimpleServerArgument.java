package terminal.model;

/**
 * @author Bernhard Halbartschlager
 */
public final class SimpleServerArgument implements IServerArguments {

    private String argument;
    private Session session;

    public SimpleServerArgument(String argument) {
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public void setSession(Session session) {
        assert session != null;
        this.session = session;
    }
}
