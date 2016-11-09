package terminal.impl;

import terminal.instruction.impl.InstructionStore;
import terminal.instruction.impl.ServerExitInstruction;

/**
 * @author Bernhard Halbartschlager
 */
public final class ServerUserServant extends UserServant {

    public ServerUserServant() {
        this.store = new InstructionStore();

        this.store.register(new ServerExitInstruction());
    }
}
