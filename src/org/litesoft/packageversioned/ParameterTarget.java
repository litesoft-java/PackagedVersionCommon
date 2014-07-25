package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.server.file.*;

import java.util.*;

/**
 * Parameter Argument: Target ("Target") e.g. "jre"
 */
public class ParameterTarget extends AbstractStringParameter {
    public static final String NAME = "Target";

    private static final String INVALID = "MUST be all 7 Bit Alpha Numeric";

    public ParameterTarget() {
        super( INVALID, NAME );
    }

    @Override
    public boolean acceptable( String pValue ) {
        return Strings.isAll7BitAlphaNumeric( pValue, 1 );
    }

    public final List<String> getTargetZipFileNames( FilePersister pFilePersister ) {
        String[] zFiles = pFilePersister.getFiles( get(), "", ".zip" );
        List<String> zNames = Lists.newArrayList( zFiles.length );
        for ( String zFile : zFiles ) {
            zNames.add( zFile.substring( 0, zFile.length() - 4 ) );
        }
        return zNames;
    }
}
