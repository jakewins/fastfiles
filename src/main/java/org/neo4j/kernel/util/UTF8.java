package org.neo4j.kernel.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.neo4j.kernel.util.functional.Function;

/**
 * Utility class for converting strings to and from UTF-8 encoded bytes.
 *
 * @author Tobias Ivarsson <tobias.ivarsson@neotechnology.com>
 */
public final class UTF8
{
    public static final Function<String, byte[]> encode = new Function<String, byte[]>()
    {
        @Override
        public byte[] apply( String s )
        {
            return encode( s );
        }
    };

    public static final Function<byte[], String> decode = new Function<byte[], String>()
    {
        @Override
        public String apply( byte[] bytes )
        {
            return decode( bytes );
        }
    };

    public static byte[] encode( String string )
    {
        try
        {
            return string.getBytes( "UTF-8" );
        }
        catch ( UnsupportedEncodingException e )
        {
            throw cantBelieveUtf8IsntAvailableInThisJvmError( e );
        }
    }

    public static String decode( byte[] bytes )
    {
        try
        {
            return new String( bytes, "UTF-8" );
        }
        catch ( UnsupportedEncodingException e )
        {
            throw cantBelieveUtf8IsntAvailableInThisJvmError( e );
        }
    }

    public static String decode( byte[] bytes, int offset, int length )
    {
        try
        {
            return new String( bytes, offset, length, "UTF-8" );
        }
        catch ( UnsupportedEncodingException e )
        {
            throw cantBelieveUtf8IsntAvailableInThisJvmError( e );
        }
    }

    public static String getDecodedStringFrom( ByteBuffer source )
    {
        // Currently only one key is supported although the data format supports multiple
        int count = source.getInt();
        byte[] data = new byte[count];
        source.get( data );
        return UTF8.decode( data );
    }

    public static void putEncodedStringInto( String text, ByteBuffer target )
    {
        byte[] data = encode( text );
        target.putInt( data.length );
        target.put( data );
    }

    public static int computeRequiredByteBufferSize( String text )
    {
        return encode( text ).length + 4;
    }

    private static Error cantBelieveUtf8IsntAvailableInThisJvmError( UnsupportedEncodingException e )
    {
        return new Error( "UTF-8 should be available on all JVMs", e );
    }

    private UTF8()
    {
        // No need to instantiate, all methods are static
    }

}

