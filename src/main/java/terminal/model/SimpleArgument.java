package terminal.model;

/**
 * @author Bernhard Halbartschlager
 */
public final class SimpleArgument implements IArguments {

    private String argument;


    public SimpleArgument(String argument) {
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }
}
