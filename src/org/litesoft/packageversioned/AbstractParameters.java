package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.server.file.*;
import org.litesoft.server.util.*;

import java8.util.function.*;

import java.io.*;
import java.util.*;

/**
 * Common Parameters support.
 * <p/>
 * Some of the supported Arguments are (Keys for the Arguments):
 * - Target ("Target") e.g. "jre"
 * - DeploymentGroup ("DeploymentGroup") e.g. "Alpha"
 * - - - if there is no keyed reference AND no default from a file (depends on the App), THEN a non-Keyed will be expected.
 * - Version ("Version"), &
 * - Bucket ("BucketURL")
 * - - - Bucket URL to interact with - if there is no keyed reference AND no SystemProperty("BucketURL"), THEN a non-Keyed will be expected.
 * - LocalVerDir ("LocalVerDir")
 * - - - (where the "versioned" "target" dirs will live) normally provided by a SystemProperty("LocalVerDir") but defaulting to "../versioned".
 * <p/>
 * if each Argument key starts w/ a unique letter, the 'permutations' option is active.
 * Any non-keyed values are applied in the order above (excess keyed entries are noted, excess non-keyed entries are an Error)
 */
public abstract class AbstractParameters {

    public static final String TARGET = "Target";
    public static final String DEPLOYMENT_GROUP = "DeploymentGroup";
    public static final String VERSION = "Version";
    public static final String BUCKET_URL = "BucketURL";
    public static final String LOCAL_VER_DIR = "LocalVerDir";

    protected String mTarget;
    protected String mDeploymentGroup;
    protected String mVersion;
    protected String mBucketURL;
    protected File mLocalVerDir;

    public String getTarget() {
        return mTarget;
    }

    public String getDeploymentGroup() {
        return mDeploymentGroup;
    }

    public String getVersion() {
        return mVersion;
    }

    protected String getBucketURL() {
        return mBucketURL;
    }

    protected File getLocalVerDir() {
        return mLocalVerDir;
    }

    abstract public boolean validate();

    protected boolean validateTarget() {
        return validate( TARGET, mTarget );
    }

    protected boolean validateDeploymentGroup() {
        return validate( DEPLOYMENT_GROUP, mDeploymentGroup );
    }

    protected boolean validateLocalVerDir() {
        return validate( LOCAL_VER_DIR, mLocalVerDir );
    }

    protected boolean validateVersion() {
        return validate( VERSION, mVersion );
    }

    protected boolean validateBucketUrl() {
        return validate( BUCKET_URL, mBucketURL );
    }

    protected boolean validate( String pWhat, Object pToCheck ) {
        if ( pToCheck != null ) {
            return true;
        }
        System.err.println( "*** " + pWhat + " Not Set ***" );
        return false;
    }

    protected static <T extends AbstractParameters> T finish( ArgsToMap pArgs, T pParameters ) {
        pArgs.assertNoRemainingNonKeyed();
        pArgs.reportRemainingKeyedKeys( "Ignoring", System.out );
        return pParameters;
    }

    protected <R extends AbstractParameters> R setLocalVerDir( String pLocalVerDir ) {
        pLocalVerDir = Confirm.significant( LOCAL_VER_DIR, pLocalVerDir );
        File zLocalVerDir = new File( pLocalVerDir );
        mLocalVerDir = validateLocalVerDir( zLocalVerDir );
        return Cast.it( this );
    }

    protected File validateLocalVerDir( File pLocalVerDir ) {
        return DirectoryUtils.assertExists( LOCAL_VER_DIR, pLocalVerDir );
    }

    protected <R extends AbstractParameters> R setBucketURL( String pBucketURL ) {
        mBucketURL = pBucketURL;
        return Cast.it( this );
    }

    protected <R extends AbstractParameters> R setVersionOptionally( String pVersion ) {
        if ( pVersion != null ) {
            setVersion( pVersion );
        }
        return Cast.it( this );
    }

    protected <R extends AbstractParameters> R setTargetOptionally( String pTarget ) {
        if ( pTarget != null ) {
            setTarget( pTarget );
        }
        return Cast.it( this );
    }

    protected void setVersion( String pVersion ) {
        mVersion = validateTargetOrVersionOrDeploymentGroup( VERSION, pVersion );
    }

    protected void setDeploymentGroup( String pDeploymentGroup ) {
        mDeploymentGroup = validateTargetOrVersionOrDeploymentGroup( DEPLOYMENT_GROUP, pDeploymentGroup );
    }

    protected void setTarget( String pTarget ) {
        mTarget = validateTargetOrVersionOrDeploymentGroup( TARGET, pTarget );
    }

    protected String validateTargetOrVersionOrDeploymentGroup( String pWhat, String pValue ) {
        pValue = Confirm.significant( pWhat, pValue );
        for ( int i = 0; i < pValue.length(); i++ ) {
            if ( !Characters.is7BitAlphaNumeric( pValue.charAt( i ) ) ) {
                throw IllegalArgument.exception( pWhat, "Not all 7 Bit Alpha Numeric" );
            }
        }
        return pValue;
    }

    /**
     * 1st Keyed, then Non-Keyed
     */
    protected static String getFrom( ArgsToMap pArgs, String pArgKey ) {
        String zValue = pArgs.getWithPermutations( pArgKey );
        return (zValue != null) ? zValue : pArgs.getNonKeyed();
    }

    /**
     * 1st (ArgKey) Keyed, 2nd (AltKey) Keyed, then Non-Keyed
     */
    protected static String getFrom( ArgsToMap pArgs, String pArgKey, String pAltKey ) {
        String zValue = pArgs.getWithPermutations( pArgKey );
        return (zValue != null) ? zValue : getFrom( pArgs, pAltKey );
    }

    /**
     * 1st Keyed, 2nd Non-Keyed, then default
     */
    protected static String getFrom( ArgsToMap pArgs, String pArgKey, Supplier<String> pDefaultSupplier ) {
        String zValue = pArgs.getWithPermutations( pArgKey );
        return (zValue != null) ? zValue : pArgs.getNonKeyed( pDefaultSupplier );
    }

    /**
     * 1st Keyed, then Non-Keyed
     */
    protected static String getTargetFrom( ArgsToMap pArgs ) {
        return getFrom( pArgs, TARGET );
    }

    /**
     * 1st Keyed, then Non-Keyed
     */
    protected static String getVersionFrom( ArgsToMap pArgs ) {
        return getFrom( pArgs, VERSION );
    }

    /**
     * 1st Keyed, 2nd Non-Keyed, 3rd System Property, then default("../versioned")
     */
    protected static String getLocalVerDirFrom( ArgsToMap pArgs ) {
        return getFrom( pArgs, LOCAL_VER_DIR, new Supplier<String>() {
            @Override
            public String get() {
                return ConstrainTo.significantOrNull( System.getProperty( LOCAL_VER_DIR ), "../versioned" );
            }
        } );
    }

    /**
     * 1st Keyed, 2nd System Property, then Non-Keyed
     */
    protected static String getBucketURLFrom( ArgsToMap pArgs ) {
        String zValue = pArgs.getWithPermutations( BUCKET_URL );
        if ( zValue == null ) {
            if ( null == (zValue = ConstrainTo.significantOrNull( System.getProperty( BUCKET_URL ) )) ) {
                zValue = pArgs.getNonKeyed();
            }
        }
        return zValue;
    }

    protected List<String> getTargetZipFileNames( FilePersister pFilePersister ) {
        String[] zFiles = pFilePersister.getFiles( getTarget(), "", ".zip" );
        List<String> zNames = Lists.newArrayList( zFiles.length );
        for ( String zFile : zFiles ) {
            zNames.add( zFile.substring( 0, zFile.length() - 4 ) );
        }
        return zNames;
    }
}
