package org.litesoft.packageversioned;

import org.litesoft.server.file.*;
import org.litesoft.server.util.*;

import java.io.*;

/**
 * Common Parameters support.
 * <p/>
 * Some of the supported Arguments are (Keys for the Arguments):
 * - DeploymentGroup ("DeploymentGroup") e.g. "Alpha" (must be from the DeploymentGroupSet)
 * -
 * <p/>
 * if each Argument key starts w/ a unique letter, the 'permutations' option is active.
 * Any non-keyed values are applied in the appropriate order (excess keyed entries are noted, excess non-keyed entries are an Error)
 * <p/>
 * DeploymentGroupSet (e.g. "Alpha") is a special "parameter" that is a file that lists the DeploymentGroup(s) in promotion order (one per line)
 * which means that the first line is inherently the Publish to DeploymentGroup.  The location (& Name) of this "DeploymentGroupSet" file can be
 * specified with either a Keyed parameter or a SystemProperty("DeploymentGroupSet").  If the location (& Name) of this "DeploymentGroupSet" file
 * is NOT specified, then a file named "DeploymentGroupSet.txt" will be hunted for.  File hunting is simply looking in the current directory and
 * then its ancestry (parent, grandparent, ...) until it is found (allows for specialization thru working-directory as sub-dirs).
 */
public abstract class AbstractParameters {
    protected void populate( Parameter<?>[] pParameters, ArgsToMap pArgs ) {
        for ( Parameter<?> zParameter : pParameters ) {
            zParameter.populateFrom( pArgs );
        }
    }

    abstract public boolean validate();

    public boolean validate( Parameter<?>[] pParameters ) {
        boolean zValid = true;
        for ( Parameter<?> zParameter : pParameters ) {
            zValid &= zParameter.validate();
        }
        return zValid;
    }

    protected final boolean validate( String pWhat, Object pToCheck ) {
        if ( pToCheck != null ) {
            return true;
        }
        System.err.println( "*** " + pWhat + " Not Set ***" );
        return false;
    }

    protected static <T extends AbstractParameters> T finish( ArgsToMap pArgs, T pParameters ) {
        pArgs.assertNoRemainingNonKeyed();
        pArgs.reportRemainingKeyedKeys( "Ignoring", System.out );
        return pParameters;
    }
}
