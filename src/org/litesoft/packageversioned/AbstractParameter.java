package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.server.util.*;

import java8.util.function.*;

public abstract class AbstractParameter<T> implements Parameter<T> {
    protected final String mInvalid, mName;
    protected final String[] mNames;
    protected T mValue;

    protected AbstractParameter( String pInvalid, String... pNames ) {
        mName = Strings.combine( '/', mNames = pNames );
        mInvalid = mName + " " + pInvalid;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        toString( sb );
        return sb.toString();
    }

    protected void toString( StringBuilder sb ) {
        sb.append( mValue );
    }

    @Override
    public final String[] getNames() {
        return mNames;
    }

    @Override
    public final void set( String pValue ) {
        mValue = convertValidated( validate( pValue ) );
    }

    abstract protected T convertValidated( String pValue );

    @Override
    public final T get() {
        return mValue;
    }

    @Override
    public final boolean validate() {
        return validate( mName, mValue );
    }

    @Override
    public final String validate( String pValue ) {
        if ( acceptable( pValue = normalize( pValue ) ) ) {
            return pValue;
        }
        throw new IllegalArgumentException( mInvalid );
    }

    protected String normalize( String pValue ) {
        return Confirm.significant( mName, pValue );
    }

    @Override
    public void populateFrom( ArgsToMap pArgs ) {
        if ( !populateFromKeyed( pArgs ) ) {
            populateFromNonKeyed( pArgs );
        }
    }

    protected boolean populateFromKeyed( ArgsToMap pArgs ) {
        for ( String zName : mNames ) {
            String zValue = pArgs.getWithPermutations( zName );
            if ( zValue != null ) {
                set( zValue );
                return true;
            }
        }
        return false;
    }

    protected void populateFromNonKeyed( ArgsToMap pArgs ) {
        String zValue = pArgs.getNonKeyed( getDefaultSupplier() );
        if ( zValue != null ) {
            set( zValue );
        }
    }

    protected Supplier<String> getDefaultSupplier() {
        return null;
    }

    @Override
    public void setIfNull( Supplier<T> pSupplier ) {
        if ( mValue == null ) {
            mValue = pSupplier.get();
        }
    }

    protected final boolean validate( String pWhat, T pToCheck ) {
        if ( pToCheck != null ) {
            return true;
        }
        System.err.println( "*** " + pWhat + " Not Set ***" );
        return false;
    }
}
