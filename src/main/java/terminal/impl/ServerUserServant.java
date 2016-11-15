package terminal.impl;

import terminal.instruction.impl.InstructionStore;
import terminal.instruction.impl.ExitServerInstruction;
import terminal.instruction.impl.UsersInstruction;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ServerUserServant extends UserServant<ServerResourceManager> {

    public ServerUserServant(ServerResourceManager rm, String prompt) {
        super(rm, prompt);

        this.store = new InstructionStore();

        this.store.register(new UsersInstruction(rm));
        this.store.register(new ExitServerInstruction(rm));
    }
}
