package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.commonfoundation.typeutils.gregorian.*;

/**
 * Parameter Argument: Version ("Version") - if the version is "!" (an Exclamation Point) then use Timestamps.createVersionFromNow()
 */
public class ParameterVersion extends AbstractStringParameter {
    public static final String NAME = "Version";

    private static final String INVALID = "MUST be all 7 Bit Alpha Numeric";

    public ParameterVersion() {
        super( INVALID, NAME );
    }

    @Override
    protected String normalize( String pValue ) {
        pValue = super.normalize( pValue );
        return "!".equals( pValue ) ? Timestamps.createVersionFromNow() : pValue;
    }

    @Override
    public boolean acceptable( String pValue ) {
        return Strings.isAll7BitAlphaNumeric( pValue, 1 );
    }
}
