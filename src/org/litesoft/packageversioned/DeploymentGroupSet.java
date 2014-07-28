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

    public String assertMember( String pDeploymentGroup ) {
        findRequired( pDeploymentGroup );
        return pDeploymentGroup;
    }

    public String first() {
        return mGroupNames.get( 0 );
    }

    public String last() {
        return mGroupNames.get( mGroupNames.size() - 1 );
    }

    public String previous( String pDeploymentGroup ) {
        int zAt = findRequired( pDeploymentGroup );
        return (zAt == 0) ? null : mGroupNames.get( zAt - 1 );
    }

    private int findRequired( String pDeploymentGroup ) {
        int zAt = mGroupNames.indexOf( pDeploymentGroup );
        if ( zAt == -1 ) {
            throw new IllegalArgumentException( "No Such Group: " + pDeploymentGroup );
        }
        return zAt;
    }

    @Override
    public String toString() {
        return Lists.convertToString( mGroupNames );
    }
}
