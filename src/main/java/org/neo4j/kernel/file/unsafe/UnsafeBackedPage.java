package org.neo4j.kernel.file.unsafe;

import java.nio.MappedByteBuffer;

import org.neo4j.kernel.file.Page;
import sun.misc.Unsafe;

import static org.neo4j.kernel.file.unsafe.UnsafeUtil.directByteBufferAddress;

public class UnsafeBackedPage implements Page
{

    private static final Unsafe unsafe = UnsafeUtil.unsafe();

    private final long address;
    private final MappedByteBuffer pageBuffer;

    public UnsafeBackedPage( MappedByteBuffer mapped )
    {
        address = directByteBufferAddress( mapped );
        pageBuffer = mapped;
    }

    @Override
    public void flush()
    {
        pageBuffer.force();
    }

    // WRITE


    @Override
    public void putByte( int offset, byte value )
    {
        unsafe.putByte( address + offset, value );
    }

    @Override
    public void putBytes( int offset, byte[] bytes, int arrayOffset, int length )
    {
        for(int i=0;i<length;i++)
        {
            unsafe.putByte( address + offset + i - arrayOffset, bytes[i] );
        }
    }

    @Override
    public void putLong( int offset, long value )
    {
        unsafe.putLong( address + offset, value );
    }

    @Override
    public void putInt( int offset, int value )
    {
        unsafe.putInt( address + offset, value );
    }

    @Override
    public void putShort( int offset, short value )
    {
        unsafe.putShort( address + offset, value );
    }

    // READ

    @Override
    public byte getByte( int offset )
    {
        return unsafe.getByte( address + offset );
    }

    @Override
    public void getBytes( int offset, int length, byte[] array )
    {
        for(int i=0;i<length;i++)
        {
            array[i] = unsafe.getByte( address + offset + i );
        }
    }

    @Override
    public long getLong( int offset )
    {
        return unsafe.getLong( address + offset );
    }

    @Override
    public int getInt( int offset )
    {
        return unsafe.getInt( address + offset );
    }

    @Override
    public short getShort( int offset )
    {
        return unsafe.getShort( address + offset );
    }
}
