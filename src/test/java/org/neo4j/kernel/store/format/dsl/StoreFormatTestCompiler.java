package org.neo4j.kernel.store.format.dsl;

import java.io.ByteArrayInputStream;
import java.util.Collection;

import org.codehaus.janino.SimpleCompiler;
import org.neo4j.kernel.util.Pair;
import org.neo4j.kernel.util.reflection.DynamicObject;

public class StoreFormatTestCompiler
{

    public static DynamicObject instantiateItem( StoreFormat format, String itemName )
    {
        SimpleCompiler compiler = new SimpleCompiler(  );
        StringBuilder classesAsStringsForErrorHandling = new StringBuilder();

        Collection<Pair<String,String>> files = format.build();
        for ( Pair<String, String> pair : files )
        {
            try
            {
                classesAsStringsForErrorHandling.append( "--------------  " + pair.first() + "  ------------" );
                classesAsStringsForErrorHandling.append( pair.second() );
                classesAsStringsForErrorHandling.append( "\n\n" );
                compiler.cook( pair.first(), new ByteArrayInputStream( pair.second().getBytes( "UTF-8" )));
            } catch(Exception e )
            {
                System.out.println("ERROR: Failed to compile '" + pair.first() + "'. Dumping file contents:");
                System.out.println( pair.second() );
                throw new RuntimeException( e );
            }
        }
        try
        {
            Object instance = compiler.getClassLoader().loadClass( format.name() + "$" + itemName ).newInstance();
            return new DynamicObject(instance);
        }
        catch(Exception e)
        {
            System.out.println("ERROR: Failed to instantiate store format class '" + itemName + "', dumping generated code:");
            System.out.println(classesAsStringsForErrorHandling.toString());
            throw new RuntimeException( e );
        }
    }

}
