package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.typeutils.*;

/**
 * Parameter Argument: TODO: XXX
 */
public class ParameterDeploymentGroup extends AbstractStringParameter {
    public static final String NAME = "DeploymentGroup";
    public static final String INVALID = "MUST be all 7 Bit Alpha Numeric";

    public ParameterDeploymentGroup() {
        super( INVALID, NAME );
    }

    @Override
    public boolean acceptable( String pValue ) {
        return Strings.isAll7BitAlphaNumeric( pValue, 1 );
    }
}
