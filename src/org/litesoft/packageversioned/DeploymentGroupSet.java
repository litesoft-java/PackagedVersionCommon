package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.server.file.*;

import java.io.*;
import java.util.*;

public class DeploymentGroupSet {
    public static final String NAME = "DeploymentGroupSet";
    public static final String DEFAULT_DEPLOYMENT_GROUP_SET_FILENAME = NAME+".txt";

    private final List<String> mGroupNames = Lists.newArrayList();

    public DeploymentGroupSet(String[] pFileLines) {
        for ( String zLine : pFileLines ) {
            if (null != (zLine = ConstrainTo.significantOrNull( zLine ))) {
                if (!zLine.startsWith( "//" )) {
                    // TODO: Validate...
                }
            }
        }
    }

    public static DeploymentGroupSet get() {
        File zFile = find( System.getProperty( NAME ) );
        return new DeploymentGroupSet( FileUtils.loadTextFile( zFile ) );
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
        return "Alpha"; // TODO: XXX
    }
}
