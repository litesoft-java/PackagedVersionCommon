package org.litesoft.packageversioned;

import org.litesoft.server.util.*;

import java8.util.function.*;

public interface Parameter<T> extends Supplier<T> {

    String[] getNames();

    void set( String pValue );

    boolean validate();

    String validate( String pValue );

    boolean acceptable( String pValue );

    void populateFrom( ArgsToMap pArgs );

    void setIfNull(Supplier<T> pSupplier);
}
