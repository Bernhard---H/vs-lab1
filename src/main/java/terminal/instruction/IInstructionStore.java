package terminal.instruction;

import terminal.model.Command;
import terminal.model.IArguments;

/**
 * @author Bernhard Halbartschlager
 */
public interface IInstructionStore {


    <T extends IArguments> void register(IInstruction<T> instruction);


    <T extends IArguments> IInstruction<T> findInstruction(Command command);

}
