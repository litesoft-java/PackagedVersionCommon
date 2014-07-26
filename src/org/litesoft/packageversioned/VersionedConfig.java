package org.litesoft.packageversioned;

import org.litesoft.server.file.*;

import java.io.*;

public interface VersionedConfig {
    public static final String FALLBACK_RELATIVE_DIR = "versionedConfig/";
    public static final String[] DEFAULT_PREFIXES = {"", FALLBACK_RELATIVE_DIR};

    public static class Finder {
        private final File mFromDir;
        private final String mDefaultAncestralFilename;

        public Finder( String pFromDir, String pDefaultAncestralFilename ) {
            mFromDir = new File( pFromDir );
            mDefaultAncestralFilename = pDefaultAncestralFilename;
        }

        public File findRequired( String pExceptionReference, String... pFilePrefixes ) {
            File zFile = find( pFilePrefixes );
            if ( zFile != null ) {
                return zFile;
            }
            throw new IllegalStateException( "Unable to locate '" + pExceptionReference + "' in ancestry of: " + mFromDir.getAbsolutePath() );
        }

        public File find( String... pFilePrefixes ) {
            return DirectoryUtils.findAncestralFile( mFromDir, mDefaultAncestralFilename, pFilePrefixes );
        }

        public File findRequired() {
            return findRequired( "[" + VersionedConfig.FALLBACK_RELATIVE_DIR + "]" + mDefaultAncestralFilename, DEFAULT_PREFIXES );
        }

        public File find() {
            return find( DEFAULT_PREFIXES );
        }
    }
}
