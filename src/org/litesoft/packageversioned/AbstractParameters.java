package org.litesoft.packageversioned;

import org.litesoft.server.util.*;

/**
 * Common Parameters support.
 * <p/>
 * if each Argument key starts w/ a unique letter, the 'permutations' option is active.
 * Any non-keyed values are applied in the appropriate order (excess keyed entries are noted, excess non-keyed entries are an Error)
 */
public abstract class AbstractParameters {
    protected ParameterTarget mTarget = new ParameterTarget();
    protected ParameterVersion mVersion = new ParameterVersion();

    private Object[] mToString;

    protected void prepToString( Object... pToString ) {
        mToString = pToString;
    }

    public final String getTarget() {
        return mTarget.get();
    }

    protected String getVersion() {
        return mVersion.get();
    }

    public final ParameterVersion getParameterVersion() {
        return mVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for ( Object zEntry : mToString ) {
            if ( sb.length() != 0 ) {
                sb.append( ' ' );
            }
            sb.append( zEntry.toString() );
        }
        return sb.toString();
    }

    protected void populate( Parameter<?>[] pParameters, ArgsToMap pArgs ) {
        for ( Parameter<?> zParameter : pParameters ) {
            zParameter.populateFrom( pArgs );
        }
    }

    abstract public boolean validate();

    protected boolean validate( Parameter<?>... pParameters ) {
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
