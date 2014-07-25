package org.litesoft.packageversioned;

public abstract class AbstractStringParameter extends AbstractParameter<String> {
    protected AbstractStringParameter( String pInvalid, String... pNames ) {
        super( pInvalid, pNames );
    }

    @Override
    protected String convertValidated( String pValue ) {
        return pValue;
    }
}
