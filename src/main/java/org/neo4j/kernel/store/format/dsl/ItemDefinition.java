package org.neo4j.kernel.store.format.dsl;

import static com.sun.codemodel.internal.JExpr.lit;
import static com.sun.codemodel.internal.JMod.FINAL;
import static com.sun.codemodel.internal.JMod.PUBLIC;
import static com.sun.codemodel.internal.JMod.STATIC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.codemodel.internal.JClassAlreadyExistsException;
import com.sun.codemodel.internal.JDefinedClass;
import com.sun.codemodel.internal.JExpression;
import com.sun.codemodel.internal.JMethod;
import org.neo4j.kernel.file.Page;

public class ItemDefinition
{

    private final List<FieldDefinition> fields = new ArrayList();
    private final Map<String, PositionDefinition> fieldPositions = new HashMap();
    private final String name;

    public ItemDefinition( String name )
    {
        this.name = name;
    }

    public ItemDefinition field( String name, FieldType type )
    {
        Naming.assertValidFieldName(name);
        fields.add( new FieldDefinition( name, type ));
        return this;
    }

    public void contributeToClass( JDefinedClass formatClass ) throws JClassAlreadyExistsException
    {
        JDefinedClass itemClass = formatClass._class( PUBLIC ^ STATIC ^ FINAL, name );

        decideFieldPositions();

        JMethod factory = itemClass.method( PUBLIC ^ FINAL, void.class, "create" );
        factory.param( FINAL, Page.class, "page" );
        factory.param( FINAL, int.class, "itemOffset" );

        JMethod sizeOf  = itemClass.method( PUBLIC ^ FINAL, int.class,  "sizeOf" );
        JExpression sizeOfExpression = lit( 0 );

        for ( FieldDefinition field : fields )
        {
            JExpression positionInPage = lit( fieldPositions.get( field.name() ).offset() );

            field.contributeFactoryArgument( factory, positionInPage );
            sizeOfExpression = field.expandSizeOfExpression( sizeOf, sizeOfExpression );

            field.contributeMethods( itemClass, positionInPage );
        }

        sizeOf.body()._return( sizeOfExpression );

    }

    private void decideFieldPositions()
    {
        int currentOffset = 0;
        for ( FieldDefinition field : fields )
        {
            fieldPositions.put( field.name(), new PositionDefinition(currentOffset) );
            currentOffset += field.size();
        }
    }

}
