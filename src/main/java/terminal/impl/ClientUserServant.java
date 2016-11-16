package terminal.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import terminal.instruction.impl.*;
import util.ClientResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ClientUserServant extends UserServant<ClientResourceManager> {

    private static final Log logger = LogFactory.getLog(ClientUserServant.class);

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
        String print = this.prompt + "> ";
        if (user != null && !user.isEmpty()) {
            print = user + "@" + print;
        }
        this.rm.getUserResponseStream().print(print);
    }
}
