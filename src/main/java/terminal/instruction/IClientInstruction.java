package terminal.instruction;

import terminal.model.IArguments;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public interface IClientInstruction<T extends IArguments> extends IInstruction<T, ClientResourceManager> {
}
