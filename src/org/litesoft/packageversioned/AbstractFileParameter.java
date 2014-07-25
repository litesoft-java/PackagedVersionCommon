package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.base.*;

import java.io.*;

public abstract class AbstractFileParameter extends AbstractParameter<File> {
    protected AbstractFileParameter( String pInvalid, String... pNames ) {
        super( pInvalid, pNames );
    }

    @Override
    public final void set( String pValue ) {
        mValue = validateAndConvert( pValue );
    }

    @Override
    public boolean acceptable( String pValue ) {
        return (null != ConstrainTo.significantOrNull( pValue ));
    }

    protected File validateAndConvert( String pValue ) {
        return new File( validate( pValue ) );
    }
}
