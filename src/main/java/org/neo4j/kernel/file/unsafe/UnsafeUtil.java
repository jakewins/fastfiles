package org.neo4j.kernel.file.unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

import sun.misc.Unsafe;

public class UnsafeUtil
{

    private static final Unsafe unsafe;
    private static final Method directByteBufferAddress;
    static
    {
        try
        {
            // Get unsafe
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get( null );

            // Get byte buffer address method
            ByteBuffer buf = ByteBuffer.allocateDirect( 1 );
            Method address = buf.getClass().getMethod( "address" );
            address.setAccessible( true );
            directByteBufferAddress = address;
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    public static Unsafe unsafe()
    {
        return unsafe;
    }

    public static long directByteBufferAddress( MappedByteBuffer mapped )
    {
        try
        {
            return (long) directByteBufferAddress.invoke( mapped );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

}
