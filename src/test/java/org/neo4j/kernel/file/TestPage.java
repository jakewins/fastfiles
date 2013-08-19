package org.neo4j.kernel.file;

import java.nio.ByteBuffer;

public class TestPage implements Page
{
    private final ByteBuffer data;

    public TestPage( int size )
    {
        this.data = ByteBuffer.allocateDirect(size);
    }

    @Override
    public void flush()
    {
    }

    @Override
    public void putByte( int offset, byte value )
    {
        data.put( offset, value );
    }

    @Override
    public void putBytes( int offset, byte[] bytes, int arrayOffset, int length )
    {
        for(int i= arrayOffset;i<length;i++)
        {
            data.put( offset + i - arrayOffset, bytes[i] );
        }
    }

    @Override
    public void putLong( int offset, long value )
    {
        data.putLong( offset, value );
    }

    @Override
    public void putInt( int offset, int value )
    {
        data.putInt( offset, value );
    }

    @Override
    public void putShort( int offset, short value )
    {
        data.putShort( offset, value );
    }

    @Override
    public byte getByte( int offset )
    {
        return data.get( offset );
    }

    @Override
    public void getBytes( int offset, int length, byte[] array )
    {
        for(int i=0;i<length;i++)
        {
            array[i] = data.get( offset + i );
        }
    }

    @Override
    public long getLong( int offset )
    {
        return data.getLong( offset );
    }

    @Override
    public int getInt( int offset )
    {
        return data.getInt( offset );
    }

    @Override
    public short getShort( int offset )
    {
        return data.getShort( offset );
    }


}
