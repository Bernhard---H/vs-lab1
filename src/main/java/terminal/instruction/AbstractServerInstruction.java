package terminal.instruction;

import terminal.model.IArguments;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class AbstractServerInstruction<T extends IArguments> implements IInstruction<T> {

    protected ServerResourceManager rm;

    public AbstractServerInstruction(ServerResourceManager rm) {
        this.rm = rm;
    }


}
