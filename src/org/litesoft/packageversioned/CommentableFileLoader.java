package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.iterators.*;
import org.litesoft.server.file.*;

import java.io.*;

public class CommentableFileLoader {
    public static class Results {
        public final File file;
        public final DescriptiveIterator<String> iterator;

        private Results( File pFile, DescriptiveIterator<String> pIterator ) {
            file = pFile;
            iterator = pIterator;
        }
    }

    public static Results getFile( String pPropertyName, String pDefaultAncestralFilename ) {
        File zFile = find( System.getProperty( pPropertyName ), pDefaultAncestralFilename );
        return new Results( zFile,
                            new DropCommentsTextLinesOnlyFilteringIterator(
                                    new DescriptiveArrayIterator<String>( "", FileUtils.loadTextFile( zFile ) ),
                                    "//" ).dropInline() );
    }

    protected static File find( String pSpecifiedFileReference, String pDefaultAncestralFilename ) {
        if ( pSpecifiedFileReference != null ) {
            return FileUtils.assertFileExists( new File( pSpecifiedFileReference ) );
        }
        File zFromDir = new File( FileUtils.currentWorkingDirectory() );
        File zFile = DirectoryUtils.findAncestralFile( zFromDir, pDefaultAncestralFilename );
        if ( zFile == null ) {
            throw new IllegalStateException( "Unable to locate '" + pDefaultAncestralFilename + "' in ancestry of: " + zFromDir.getAbsolutePath() );
        }
        return zFile;
    }
}
