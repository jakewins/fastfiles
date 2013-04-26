package org.neo4j.kernel.file;

public interface Page
{
    void flush();

    void putLong( int offset, long value );

    void putShort( int offset, short value );

    long getLong( int offset );

    short getShort( int offset );
}
