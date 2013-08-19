package org.neo4j.kernel.util.reflection;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

public class DynamicObject
{

    private final Object delegate;
    private final Class<?> cls;

    public DynamicObject( Object delegate )
    {
        this.delegate = delegate;
        this.cls = delegate.getClass();
    }

    public <T> T call( String methodName, Object ... args )
    {
        try
        {
            Collection<Class> requiredArgs = types( args );
            for ( Method method : cls.getMethods() )
            {
                if ( method.getName().equals( methodName ) )
                {
                    Iterator<Class> argIterator = requiredArgs.iterator();
                    for ( Class<?> aClass : method.getParameterTypes() )
                    {
                        if(!argIterator.hasNext() || !aClass.isAssignableFrom( argIterator.next() ))
                        {
                            continue;
                        }
                    }

                    if(argIterator.hasNext())
                    {
                        // This method has fewer args than we needed
                        continue;
                    }

                    return (T) method.invoke( delegate, args );
                }
            }

            return (T) cls.getMethod( methodName,
                    requiredArgs.toArray( new Class[args.length])).invoke( delegate, args );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( String.format("Tried to call [%s#%s] with args [%s], but failed.",
                    cls.getSimpleName(), methodName, serializeForError( args )), e );
        }
    }

    private String serializeForError( Object[] args )
    {
        StringBuilder out = new StringBuilder(  );
        int i = 0;
        out.append( args[i].getClass().getSimpleName() + " " + args[i] );
        i++;
        for(; i<args.length;i++)
        {
            out.append(", " + args[i].getClass().getSimpleName() + " " + args[i] );
        }
        return out.toString();
    }


    private List<Class> types( Object[] args )
    {
        Class[] types = new Class[args.length];
        for(int i=0;i<args.length;i++)
        {
            types[i] = args[i].getClass();
        }
        return asList( types );
    }

}
