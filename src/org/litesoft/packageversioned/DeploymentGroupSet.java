package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.server.file.*;

import java.io.*;
import java.util.*;

public class DeploymentGroupSet {
    public static final String NAME = "DeploymentGroupSet";
    public static final String DEFAULT_DEPLOYMENT_GROUP_SET_FILENAME = NAME + ".txt";

    private static final ParameterDeploymentGroup VALIDATOR = new ParameterDeploymentGroup();

    private final List<String> mGroupNames = Lists.newArrayList();

    public DeploymentGroupSet( String pFileName, String[] pFileLines ) {
        for ( int i = 0; i < pFileLines.length; i++ ) {
            String zLine = pFileLines[i] + "//"; // Comment
            if ( !(zLine = zLine.substring( 0, zLine.indexOf( "//" ) ).trim()).isEmpty() ) {
                if ( VALIDATOR.acceptable( zLine ) ) {
                    mGroupNames.add( zLine );
                } else {
                    throw new IllegalArgumentException( "Line[" + i + "] from '" + pFileName + "' is not a valid DeploymentGroup: " + zLine );
                }
            }
        }
        if ( mGroupNames.isEmpty() ) {
            throw new IllegalArgumentException( "No DeploymentGroups found in: " + pFileName );
        }
    }

    public static DeploymentGroupSet get() {
        File zFile = find( System.getProperty( NAME ) );
        return new DeploymentGroupSet( zFile.getAbsolutePath(), FileUtils.loadTextFile( zFile ) );
    }

    protected static File find( String pDeploymentGroupSetFileReference ) {
        if ( pDeploymentGroupSetFileReference != null ) {
            return FileUtils.assertFileExists( new File( pDeploymentGroupSetFileReference ) );
        }
        File zFromDir = new File( FileUtils.currentWorkingDirectory() );
        File zFile = DirectoryUtils.findAncestralFile( zFromDir, DEFAULT_DEPLOYMENT_GROUP_SET_FILENAME );
        if ( zFile == null ) {
            throw new IllegalStateException( "Unable to locate '" + DEFAULT_DEPLOYMENT_GROUP_SET_FILENAME + "' in ancestry of: " + zFromDir.getAbsolutePath() );
        }
        return zFile;
    }

    public String first() {
        return mGroupNames.get( 0 );
    }
}
