package terminal.impl;

import terminal.instruction.impl.*;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientUserServant extends UserServant<ClientResourceManager> {

    public ClientUserServant(ClientResourceManager rm, String prompt) {
        super(rm, prompt);

        this.store = new InstructionStore<>();

        this.store.register(new LoginInstruction());
        this.store.register(new LogoutInstruction());
        this.store.register(new SendInstruction());
        this.store.register(new RegisterInstruction());
        this.store.register(new LookupInstruction());
        this.store.register(new MessageInstruction());
        this.store.register(new ListInstruction());
        this.store.register(new LastMsgInstruction());
        this.store.register(new ExitClientInstruction());
    }

    @Override
    protected void printPrompt() {
        String user = this.rm.getClientSessionManager().getLoggedinUser();
        String print = "";
        if (user != null && !user.isEmpty()) {
            print = user + "@";
        }
        this.rm.getUserResponseStream().print(print + this.prompt + "> ");
    }
}
