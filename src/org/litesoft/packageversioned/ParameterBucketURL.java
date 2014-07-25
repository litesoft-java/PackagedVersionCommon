package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.server.util.*;

/**
 * Parameter Argument: Bucket ("BucketURL") - Bucket URL to interact with.
 * if there is no keyed reference AND no SystemProperty("BucketURL"), THEN a non-Keyed will be expected.
 */
public class ParameterBucketURL extends AbstractStringParameter {
    public static final String NAME = "BucketURL";
    public static final String INVALID = "MUST start with a 7 Bit Alpha Numeric character";

    public ParameterBucketURL() {
        super( INVALID, NAME );
    }

    @Override
    public boolean acceptable( String pValue ) {
        return Characters.is7BitAlphaNumeric( (ConstrainTo.notNull( pValue ) + "-").charAt( 0 ) );
    }

    @Override
    protected void populateFromNonKeyed( ArgsToMap pArgs ) {
        String zValue = ConstrainTo.significantOrNull( System.getProperty( NAME ) );
        if ( zValue != null ) {
            set( zValue );
        } else {
            super.populateFromNonKeyed( pArgs );
        }
    }
}
