package terminal.impl;

import terminal.instruction.impl.InstructionStore;
import terminal.instruction.impl.ServerExitInstruction;
import terminal.instruction.impl.UsersInstruction;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ServerUserServant extends UserServant<ServerResourceManager> {

    public ServerUserServant(ServerResourceManager rm) {
        super(rm);

        this.store = new InstructionStore<>();

        this.store.register(new UsersInstruction());
        this.store.register(new ServerExitInstruction());
    }
}
