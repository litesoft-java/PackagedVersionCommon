package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.server.file.*;
import org.litesoft.server.util.*;

import java.io.*;

/**
 * Parameter Argument: LocalVerDir ("LocalVerDir") - (where the "versioned" "target" dirs will live)
 * normally provided by a SystemProperty("LocalVerDir") but defaulting to "../versioned".
 * Note: The LocalVerDir NEVER comes from the nonKeyed entries!
 */
public class ParameterLocalVerDir extends AbstractFileParameter {
    public static final String NAME = "LocalVerDir";

    private static String createInvalid( boolean pMustExist ) {
        return "MUST be an existing" + (pMustExist ? " " : " (or creatable) ") + "local directory";
    }

    private final boolean mMustExist;

    private ParameterLocalVerDir( boolean pMustExist ) {
        super( createInvalid( pMustExist ), NAME );
        mMustExist = pMustExist;
    }

    public static ParameterLocalVerDir existing() {
        return new ParameterLocalVerDir( true );
    }

    public static ParameterLocalVerDir existingOrCreatable() {
        return new ParameterLocalVerDir( false );
    }

    @Override
    protected File convertValidated( String pValue ) {
        File zDirectory = super.convertValidated( pValue );
        return mMustExist ?
               DirectoryUtils.assertExists( NAME, zDirectory ) :
               DirectoryUtils.ensureExistsAndMutable( NAME + " - Not Mutable: ", zDirectory );
    }

    @Override
    protected void populateFromNonKeyed( ArgsToMap pArgs ) {
        set( ConstrainTo.significantOrNull( System.getProperty( NAME ), "../versioned" ) );
    }
}
