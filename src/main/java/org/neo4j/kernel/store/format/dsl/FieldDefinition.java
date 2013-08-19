package org.neo4j.kernel.store.format.dsl;

import static com.sun.codemodel.internal.JExpr.lit;

import com.sun.codemodel.internal.JDefinedClass;
import com.sun.codemodel.internal.JExpression;
import com.sun.codemodel.internal.JMethod;

public class FieldDefinition
{
    private final String name;
    private final FieldType type;

    public FieldDefinition( String name, FieldType type )
    {

        this.name = name;
        this.type = type;
    }

    public String name()
    {
        return name;
    }

    public FieldType type()
    {
        return type;
    }

    public void contributeFactoryArgument( JMethod factory, JExpression positionInPage )
    {
        type().contributeFactoryArgument(factory, name(), positionInPage);
    }

    public JExpression expandSizeOfExpression( JMethod sizeOf, JExpression sizeOfExpression )
    {
        return sizeOfExpression.plus( lit( size() ) );
    }

    public void contributeMethods( JDefinedClass formatClass, JExpression fieldPositionInPage )
    {
        type().contributeMethods(formatClass, name(), fieldPositionInPage);
    }

    public int size()
    {
        return type().size();
    }
}
