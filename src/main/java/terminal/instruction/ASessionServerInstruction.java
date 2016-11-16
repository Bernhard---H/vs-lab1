package terminal.instruction;

import terminal.model.IArguments;
import terminal.model.Session;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class ASessionServerInstruction<T extends IArguments> extends AbstractServerInstruction<T> {

    protected Session session;

    public ASessionServerInstruction(ServerResourceManager rm, Session session) {
        super(rm);
        assert session != null;
        this.session = session;
    }


}
