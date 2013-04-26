package org.neo4j.kernel.file.unsafe;

import static org.neo4j.kernel.file.unsafe.UnsafeUtil.directByteBufferAddress;

import java.nio.MappedByteBuffer;

import org.neo4j.kernel.file.Page;
import sun.misc.Unsafe;

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
    public void putLong( int offset, long value )
    {
        unsafe.putLong( address + offset, value );
    }

    @Override
    public void putShort( int offset, short value )
    {
        unsafe.putShort( address + offset, value );
    }

    // READ

    @Override
    public long getLong( int offset )
    {
        return unsafe.getLong( address + offset );
    }

    @Override
    public short getShort( int offset )
    {
        return unsafe.getShort( address + offset );
    }
}
