package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.server.util.*;

/**
 * Parameter Argument: Bucket ("Bucket") - Bucket to interact with.
 * if there is no keyed reference AND no SystemProperty("Bucket"), THEN a non-Keyed will be expected.
 */
public class ParameterBucket extends AbstractStringParameter {
    public static final String NAME = "Bucket";

    private static final String INVALID = "MUST conform to AWS's S3 Bucket Naming Policy with the additional restriction that it MUST start with a letter";

    private String mS3Endpoint;

    public ParameterBucket() {
        super( INVALID, NAME );
    }

    @Override
    public boolean acceptable( String pValue ) {
        pValue = ConstrainTo.significantOrNull( pValue, "" );
        return (3 <= pValue.length()) && (pValue.length() <= 63)
                && Characters.isLowerCaseAsciiAlpha( pValue.charAt( 0 ) )
                && validLabels(Strings.parseChar( pValue, '.' ));
    }

    private boolean validLabels( String[] pLabels ) {
        boolean zValid = true;
        for ( String zLabel : pLabels ) {
            zValid &= validLabel( zLabel );
        }
        return zValid;
    }

    private boolean validLabel( String pLabel ) {
        if ( !pLabel.isEmpty() ) {
            int zLastCharAt = pLabel.length() - 1;
            if ( isAlphaNumeric( pLabel.charAt( 0 ) ) && isAlphaNumeric( pLabel.charAt( zLastCharAt ) ) ) {
                for ( int i = zLastCharAt; 0 < --i ; ) {
                    if (!isMidLabelAcceptable( pLabel.charAt( i ) )) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean isMidLabelAcceptable( char c ) {
        return (c == '-') || isAlphaNumeric( c );
    }

    private boolean isAlphaNumeric( char c ) {
        return Characters.isNumeric( c ) || Characters.isLowerCaseAsciiAlpha( c );
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

    @Override
    protected String convertValidated( String pValue ) {
        pValue = super.convertValidated( pValue );
        if (null != (mS3Endpoint = BucketEndpoints.get().getEndpointFor(pValue))) {
            return pValue;
        }
        throw new IllegalArgumentException( "No Endpoint defined for Bucket: " + pValue );
    }

    public String getS3Endpoint() {
        return mS3Endpoint;
    }

    @Override
    public String toString() {
        return get() + "." + getS3Endpoint();
    }
}
