/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.kernel.store.format.dsl;

import com.sun.codemodel.internal.JDefinedClass;
import com.sun.codemodel.internal.JExpression;
import com.sun.codemodel.internal.JMethod;

/**
 * Field types are used to signify what type a field in an iten should be, and contain code for generating the code
 * needed to read and write to the fields.
 */
public interface FieldType
{
    void contributeMethods( JDefinedClass formatClass, String fieldName, JExpression positionInPage );

    void contributeFactoryArgument( JMethod factory, String fieldName, JExpression positionInPage );

    int size();

}
