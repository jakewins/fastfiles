package org.neo4j.kernel.store.format.dsl.type;

import java.util.TreeMap;

import com.sun.codemodel.internal.JDefinedClass;
import com.sun.codemodel.internal.JExpression;
import com.sun.codemodel.internal.JMethod;
import org.neo4j.kernel.store.format.dsl.FieldType;

import static com.sun.codemodel.internal.JExpr.lit;
import static org.neo4j.kernel.store.format.dsl.Naming.uCaseFirst;

public class CompoundField implements FieldType
{

    private final TreeMap<String, FieldType> subFields = new TreeMap<>();

    public CompoundField( Object ... alternatingSubFieldNameAndFieldType )
    {
        for(int i=0;i<alternatingSubFieldNameAndFieldType.length;i+=2)
        {
            String subFieldName = (String) alternatingSubFieldNameAndFieldType[i];
            FieldType subFieldType = (FieldType) alternatingSubFieldNameAndFieldType[i + 1];
            subFields.put( subFieldName, subFieldType );
        }
    }

    @Override
    public void contributeMethods( JDefinedClass formatClass, String fieldName, JExpression positionInPage )
    {
        int subFieldOffset = 0;
        for ( String subFieldName : subFields.keySet() )
        {
            FieldType subFieldType = subFields.get( subFieldName );
            subFieldType.contributeMethods(
                    formatClass,
                    fieldName + uCaseFirst(subFieldName),
                    positionInPage.plus( lit( subFieldOffset ) ) );

            subFieldOffset += subFieldType.size();
        }
    }

    @Override
    public void contributeFactoryArgument( JMethod factory, String fieldName, JExpression positionInPage )
    {
        int subFieldOffset = 0;
        for ( String subFieldName : subFields.keySet() )
        {
            FieldType subFieldType = subFields.get( subFieldName );
            subFieldType.contributeFactoryArgument(
                    factory,
                    fieldName + uCaseFirst(subFieldName),
                    positionInPage.plus( lit( subFieldOffset ) ) );

            subFieldOffset += subFieldType.size();
        }
    }

    @Override
    public int size()
    {
        int total = 0;
        for ( FieldType fieldType : subFields.values() )
        {
            total += fieldType.size();
        }
        return total;
    }
}
