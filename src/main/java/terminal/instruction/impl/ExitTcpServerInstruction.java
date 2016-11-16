package terminal.instruction.impl;

import terminal.instruction.ASessionServerInstruction;
import terminal.model.Session;
import terminal.model.SimpleArgument;
import terminal.parser.IArgumentsParser;
import util.ServerResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public final class ExitTcpServerInstruction extends ASessionServerInstruction<SimpleArgument> {

    public ExitTcpServerInstruction(ServerResourceManager rm, Session session) {
        super(rm, session);
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public IArgumentsParser<SimpleArgument> getArgumentsParser() {
        return null;
    }

    @Override
    public String execute(SimpleArgument args) {
        this.session.closeMe();
        return "";
    }
}
