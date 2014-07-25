package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.server.file.*;

import java8.util.function.*;

import java.io.*;

/**
 * Parameter Argument: LocalVerDir ("LocalVerDir") - (where the "versioned" "target" dirs will live)
 * normally provided by a SystemProperty("LocalVerDir") but defaulting to "../versioned".
 */
public class ParameterLocalVerDir extends AbstractFileParameter {
    public static final String NAME = "LocalVerDir";
    public static final String INVALID = "MUST be an existing (or creatable) local directory";

    public ParameterLocalVerDir() {
        super( INVALID, NAME );
    }

    @Override
    public boolean acceptable( String pValue ) {
        return Characters.is7BitAlphaNumeric( (ConstrainTo.notNull( pValue ) + "-").charAt( 0 ) );
    }

    @Override
    protected File validateAndConvert( String pValue ) {
        return DirectoryUtils.ensureExistsAndMutable( NAME + " - Not Mutable: ", super.validateAndConvert( pValue ) );
    }

    @Override
    protected Supplier<String> getDefaultSupplier() {
        return new Supplier<String>() {
            @Override
            public String get() {
                return ConstrainTo.significantOrNull( System.getProperty( NAME ), "../versioned" );
            }
        };
    }
}
