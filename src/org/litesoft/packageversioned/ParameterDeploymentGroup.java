package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.typeutils.*;

/**
 * Parameter Argument: Must be an entry from the DeploymentGroupSet file (See DeploymentGroupSet).
 */
public class ParameterDeploymentGroup extends AbstractStringParameter {
    public static final String NAME = "DeploymentGroup";

    private static final String INVALID = "MUST be all 7 Bit Alpha Numeric";

    public ParameterDeploymentGroup() {
        super( INVALID, NAME );
    }

    @Override
    public boolean acceptable( String pValue ) {
        return Strings.isAll7BitAlphaNumeric( pValue, 1 );
    }
}
