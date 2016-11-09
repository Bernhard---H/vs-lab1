package terminal.instruction.impl;

import terminal.instruction.IInstruction;
import terminal.instruction.IInstructionStore;
import terminal.model.Command;
import terminal.model.IArguments;
import util.ResourceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bernhard Halbartschlager
 */
public final class InstructionStore<R extends ResourceManager> implements IInstructionStore<R> {

    private Map<String, IInstruction> instructions = new HashMap<>();

    @Override
    public <T extends IArguments> void register(IInstruction<T, R> instruction) {
        this.instructions.put(instruction.getName(), instruction);
    }

    @Override
    public <T extends IArguments> IInstruction<T, R> findInstruction(Command command) {
        // just be confident, it will work!  :D
        return this.instructions.get(command.getName());
    }

}
