package org.litesoft.packageversioned;

import org.litesoft.commonfoundation.base.*;
import org.litesoft.commonfoundation.typeutils.*;

import java8.util.function.*;

public class VersionFile implements Supplier<String> {
    private final String mVersion;

    private VersionFile( String pVersion ) {
        mVersion = pVersion;
    }

    public static VersionFile fromVersion( String pVersion ) {
        return new VersionFile( ConstrainTo.significantOrNull( pVersion ) );
    }

    public static VersionFile fromFileLines( String[] pFileLines ) {
        return fromVersion( Strings.getFirstEntry( pFileLines ) ); // First Line
    }

    public String get() {
        return mVersion;
    }

    public String[] toFileLines() {
        return Strings.toArray( Confirm.isNotNull( "Version", mVersion ) );
    }
}
