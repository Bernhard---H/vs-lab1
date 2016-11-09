package terminal.instruction;

import terminal.model.Command;
import terminal.model.IArguments;
import util.ResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public interface IInstructionStore<R extends ResourceManager> {


    <T extends IArguments> void register(IInstruction<T, R> instruction);


    <T extends IArguments> IInstruction<T, R> findInstruction(Command command);

}
