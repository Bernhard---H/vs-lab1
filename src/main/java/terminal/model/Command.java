package terminal.model;

/**
 * @author Bernhard Halbartschlager
 */
public final class Command<T extends IArguments> implements CommandID {

    private String name;
    private T parameter;

    public Command(String name, T parameter) {
        assert name != null;
        assert parameter != null;
        this.name = name;
        this.parameter = parameter;
    }

    public Command(Command<SimpleArgument> old, T parameter) {
        this(old.getName(), parameter);
    }

    public String getName() {
        return name;
    }

    public T getParameter() {
        return parameter;
    }
}
