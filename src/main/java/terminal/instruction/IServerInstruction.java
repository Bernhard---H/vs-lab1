package terminal.instruction;

import terminal.model.IArguments;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public interface IServerInstruction<T extends IArguments> extends IInstruction<T, ServerResourceManager> {
}
