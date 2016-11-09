package terminal.impl;

import terminal.instruction.impl.*;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientUserServant extends UserServant<ClientResourceManager> {

    public ClientUserServant(ClientResourceManager rm) {
        super(rm);

        this.store = new InstructionStore<>();

        this.store.register(new LoginInstruction());
        this.store.register(new LogoutInstruction());
        this.store.register(new SendInstruction());
        this.store.register(new RegisterInstruction());
        this.store.register(new MessageInstruction());
        this.store.register(new ListInstruction());
        this.store.register(new LastMsgInstruction());
        this.store.register(new ClientExitInstruction());
    }

}
