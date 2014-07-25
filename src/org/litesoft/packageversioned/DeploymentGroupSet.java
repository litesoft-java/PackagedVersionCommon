package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.iterators.*;
import org.litesoft.commonfoundation.typeutils.*;

import java.util.*;

public class DeploymentGroupSet {
    public static final String NAME = "DeploymentGroupSet";
    public static final String DEFAULT_DEPLOYMENT_GROUP_SET_FILENAME = NAME + ".txt";

    private static final ParameterDeploymentGroup VALIDATOR = new ParameterDeploymentGroup();

    private final List<String> mGroupNames = Lists.newArrayList();

    public DeploymentGroupSet( CommentableFileLoader.Results pResults ) {
        for ( DescriptiveIterator<String> zSource = pResults.iterator; zSource.hasNext(); ) {
            String zLine = zSource.next();
            if ( !acceptable( zLine ) ) {
                throw new IllegalArgumentException(
                        "Line[" + zSource + "] from '" + pResults.file.getAbsolutePath() + "' is not a valid DeploymentGroup: " + zLine );
            }
            mGroupNames.add( zLine );
        }
        if ( mGroupNames.isEmpty() ) {
            throw new IllegalArgumentException( "No DeploymentGroups found in: " + pResults.file.getAbsolutePath() );
        }
    }

    private boolean acceptable( String pLine ) {
        return VALIDATOR.acceptable( pLine );
    }

    private static DeploymentGroupSet sInstance;

    public static synchronized DeploymentGroupSet get() {
        if ( sInstance == null ) {
            sInstance = new DeploymentGroupSet( CommentableFileLoader.getFile( NAME, DEFAULT_DEPLOYMENT_GROUP_SET_FILENAME ) );
        }
        return sInstance;
    }

    public String first() {
        return mGroupNames.get( 0 );
    }
}
