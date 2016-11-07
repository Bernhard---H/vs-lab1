package terminal.model;

/**
 * @author Bernhard Halbartschlager
 */
public final class PrivateMsgArg implements IArguments {

    private String receiver;
    private String message;

    public PrivateMsgArg(String receiver, String message) {
        this.receiver = receiver;
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}
