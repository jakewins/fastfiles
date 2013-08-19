package org.neo4j.kernel.file;

public interface Page
{
    // Static method names used for code generation

    public static final String flush = "flush";

    public static final String putByte  = "putByte";
    public static final String putBytes = "putBytes";
    public static final String putLong  = "putLong";
    public static final String putInt   = "putInt";
    public static final String putShort = "putShort";

    public static final String getByte  = "getByte";
    public static final String getLong  = "getLong";
    public static final String getInt   = "getInt";
    public static final String getShort = "getShort";

    void flush();

    void putByte( int offset, byte value);

    void putBytes( int offset, byte[] value, int arrayOffset, int length );

    void putLong( int offset, long value );

    void putInt( int offset, int value );

    void putShort( int offset, short value );


    byte getByte( int offset );

    void getBytes( int offset, int length, byte[] array );

    long getLong( int offset );

    int getInt( int offset );

    short getShort( int offset );
}
