package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.iterators.*;
import org.litesoft.commonfoundation.typeutils.*;

import java.util.*;

public class BucketEndpoints {
    public static final String NAME = "BucketEndpoints";
    public static final String DEFAULT_BUCKET_ENDPOINTS_FILENAME = NAME + ".txt";

    private static final ParameterBucket VALIDATOR = new ParameterBucket();

    private final Map<String, String> mEndpointByBucketName = Maps.newHashMap();

    public BucketEndpoints( CommentableFileLoader.Results pResults ) {
        for ( DescriptiveIterator<String> zSource = pResults.iterator; zSource.hasNext(); ) {
            NameValuePair zPair = parseAndValidateNextLine( pResults );
            mEndpointByBucketName.put( zPair.getName(), zPair.getValue() );
        }
        if ( mEndpointByBucketName.isEmpty() ) {
            throw new IllegalArgumentException( "No DeploymentGroups found in: " + pResults.file.getAbsolutePath() );
        }
    }

    private NameValuePair parseAndValidateNextLine( CommentableFileLoader.Results pResults ) {
        String zBucketName, zEndpoint, zLine = pResults.iterator.next();
        int zAt = zLine.indexOf( '=' );
        if ( zAt == -1 ) {
            reportError( pResults, zLine, "No '=' in line" );
        }
        if ( !VALIDATOR.acceptable( zBucketName = zLine.substring( 0, zAt ).trim() ) ) {
            reportError( pResults, zLine, "Unacceptable BucketName '" + zBucketName + "'" );
        }
        if ( !acceptableEndpoint( zEndpoint = zLine.substring( zAt + 1 ).trim() ) ) {
            reportError( pResults, zLine, "Unacceptable Endpoint '" + zEndpoint + "'" );
        }
        return new NameValuePair( zBucketName, zEndpoint );
    }

    /**
     * Example Endpoints: s3.amazonaws.com (IAD) all others s3[-us-west-2].amazonaws.com except CN: s3-cn-north-1.amazonaws.com.cn
     */
    private boolean acceptableEndpoint( String zEndpoint ) {
        return zEndpoint.startsWith( "s3" ) && acceptableLabels( zEndpoint );
    }

    private boolean acceptableLabels( String pEndpoint ) {
        String[] zLabels = Strings.parseChar( pEndpoint, '.' );
        return zLabels.length == (pEndpoint.endsWith( ".amazonaws.com" ) ? 3 : 4);
    }

    private void reportError( CommentableFileLoader.Results pResults, String pLine, String pDetails ) {
        throw new IllegalArgumentException(
                "Line[" + pResults.iterator + "] from '" + pResults.file.getAbsolutePath() + "' " + pDetails + ": " + pLine );
    }

    private static BucketEndpoints sInstance;

    public static synchronized BucketEndpoints get() {
        if ( sInstance == null ) {
            sInstance = new BucketEndpoints( CommentableFileLoader.getFile( NAME, DEFAULT_BUCKET_ENDPOINTS_FILENAME ) );
        }
        return sInstance;
    }

    public String getEndpointFor( String pBucketName ) {
        return mEndpointByBucketName.get( pBucketName );
    }
}
