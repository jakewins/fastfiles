package org.neo4j.kernel.store.format.dsl.type;

import com.sun.codemodel.internal.JDefinedClass;
import com.sun.codemodel.internal.JExpression;
import com.sun.codemodel.internal.JMethod;
import org.neo4j.kernel.store.format.dsl.FieldType;

/**
 * A field that contains zero or more fixed size fields, stores the data by first storing the number of fields,
 * to allow calculating the size of the field, and then uses offsets to access members.
 *
 * Note that this will generate methods that may overwrite other fields if not used correctly.
 */
public class RepeatingFixedSizeField implements FieldType
{
    private FieldType fieldToRepeat;

    public RepeatingFixedSizeField( FieldType fieldToRepeat )
    {
        this.fieldToRepeat = fieldToRepeat;
    }

    @Override
    public void contributeMethods( JDefinedClass formatClass, String fieldName, JExpression positionInPage )
    {
    }

    @Override
    public void contributeFactoryArgument( JMethod factory, String fieldName, JExpression positionInPage )
    {
    }

    @Override
    public int size()
    {
        return 0;
    }
}
