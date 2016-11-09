package terminal.impl;

import terminal.Servant;
import util.ResourceManager;

/**
 * @author Bernhard Halbartschlager
 */
public abstract class UserServant<R extends ResourceManager> extends Servant<R> {


    public UserServant(R rm) {
        super(rm);
    }

    @Override
    public void println(String msg) {

    }



}
