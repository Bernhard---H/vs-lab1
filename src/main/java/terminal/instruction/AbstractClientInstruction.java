package terminal.instruction;

import terminal.model.IArguments;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class AbstractClientInstruction<T extends IArguments> implements IInstruction<T> {

    protected ClientResourceManager rm;

    public AbstractClientInstruction(ClientResourceManager rm) {
        this.rm = rm;
    }


}
