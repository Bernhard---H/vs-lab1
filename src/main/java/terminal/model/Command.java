package terminal.model;

/**
 * @author Bernhard Halbartschlager
 */
public final class Command {

    private String name;
    private String parameter;


    public Command(String name, String parameter) {
        assert name != null;
        assert parameter != null;
        this.name = name;
        this.parameter = parameter;
    }

    public String getName() {
        return name;
    }

    public String getParameter() {
        return parameter;
    }

}
