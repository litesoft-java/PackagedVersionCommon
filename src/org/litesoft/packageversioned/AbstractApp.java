package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.console.*;
import org.litesoft.commonfoundation.indent.*;

public abstract class AbstractApp<Parameters extends AbstractParameters> {
    protected static final IndentableWriter CONSOLE = new ConsoleIndentableWriter( "    ", ConsoleSOUT.INSTANCE );

    protected final Parameters mParameters;
    private final String mAction;

    protected AbstractApp( String pAction, Parameters pParameters ) {
        mAction = pAction;
        if ( !(mParameters = pParameters).validate() ) {
            System.exit( 1 );
        }
    }

    public void run() {
        CONSOLE.indent();
        CONSOLE.printLn( mAction, " ", mParameters );
        CONSOLE.indent();

        process();

        CONSOLE.outdent();
        CONSOLE.outdent();
        CONSOLE.printLn( "Done!" );
    }

    abstract protected void process();

    protected String createPath( String pFileName ) {
        return "versioned/" + mParameters.getTarget() + "/" + pFileName;
    }

    protected String getTarget() {
        return mParameters.getTarget();
    }

    protected String getVersion() {
        return mParameters.getVersion();
    }
}
