package terminal.impl;

import terminal.instruction.impl.*;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientUserServant extends UserServant<ClientResourceManager> {

    public ClientUserServant(ClientResourceManager rm, String prompt) {
        super(rm, prompt);

        this.store = new InstructionStore();

        this.store.register(new LoginInstruction(rm));
        this.store.register(new LogoutInstruction(rm));
        this.store.register(new SendInstruction(rm));
        this.store.register(new RegisterInstruction(rm));
        this.store.register(new LookupInstruction(rm));
        this.store.register(new MessageInstruction(rm));
        this.store.register(new ListInstruction(rm));
        this.store.register(new LastMsgInstruction(rm));
        this.store.register(new ExitClientInstruction(rm));
    }

    @Override
    protected void printPrompt() {
        String user = this.rm.getSessionManager().getLoggedinUser();
        String print = "";
        if (user != null && !user.isEmpty()) {
            print = user + "@";
        }
        this.rm.getUserResponseStream().print(print + this.prompt + "> ");
    }
}
