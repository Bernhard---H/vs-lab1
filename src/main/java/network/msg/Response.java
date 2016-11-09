package network.msg;

/**
 * @author Bernhard Halbartschlager
 */
public interface Response extends Message {

    ResponseType getType();

    String getMsg();

}
