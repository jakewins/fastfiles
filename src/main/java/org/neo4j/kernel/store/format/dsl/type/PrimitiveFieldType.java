package org.neo4j.kernel.store.format.dsl.type;

import com.sun.codemodel.internal.JDefinedClass;
import com.sun.codemodel.internal.JExpression;
import com.sun.codemodel.internal.JMethod;
import com.sun.codemodel.internal.JVar;
import org.neo4j.kernel.file.Page;
import org.neo4j.kernel.store.format.dsl.FieldType;

import static com.sun.codemodel.internal.JMod.FINAL;
import static com.sun.codemodel.internal.JMod.PUBLIC;
import static org.neo4j.kernel.store.format.dsl.Naming.uCaseFirst;

/**
 * Base class for primitive field types that map directly to simple java types and to the basic methods in
 * {@link Page}.
 */
public class PrimitiveFieldType implements FieldType
{

    private final int size;
    private final Class<?> valueClass;
    private final String getMethod;
    private final String putMethod;

    public PrimitiveFieldType( int size, Class<?> valueClass, String getMethod, String putMethod )
    {
        this.size = size;
        this.valueClass = valueClass;
        this.getMethod = getMethod;
        this.putMethod = putMethod;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public void contributeMethods( JDefinedClass formatClass, String fieldName, JExpression positionInPage )
    {
        contributeSetter( formatClass, fieldName, positionInPage );
        contributeGetter( formatClass, fieldName, positionInPage );
    }

    @Override
    public void contributeFactoryArgument( JMethod factory, String fieldName, JExpression positionInPage )
    {
        factory.param( FINAL, long.class, fieldName );
    }

    private void contributeGetter( JDefinedClass formatClass, String fieldName, JExpression
            fieldPositionInPage )
    {
        JMethod getter = formatClass.method(PUBLIC ^ FINAL, valueClass, "get" + uCaseFirst( fieldName ));

        JVar page       = getter.param( Page.class, "page" );
        JVar itemOffset = getter.param( int.class,  "itemOffset" );

        JExpression pageOffset = itemOffset.plus( fieldPositionInPage );

        getter.body()._return( page.invoke( getMethod ).arg( pageOffset ) );
    }

    private void contributeSetter( JDefinedClass formatClass, String fieldName, JExpression
            fieldPositionInPage )
    {
        JMethod setter = formatClass.method(PUBLIC ^ FINAL, void.class, "set" + uCaseFirst(fieldName));

        JVar page       = setter.param( Page.class, "page" );
        JVar itemOffset = setter.param( int.class,  "itemOffset" );
        JVar value      = setter.param( valueClass, "value" );

        JExpression pageOffset = itemOffset.plus( fieldPositionInPage );

        setter.body().add( page.invoke( putMethod ).arg( pageOffset ).arg( value ));
    }

}
