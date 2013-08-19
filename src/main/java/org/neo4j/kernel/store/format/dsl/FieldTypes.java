package org.neo4j.kernel.store.format.dsl;

import org.neo4j.kernel.file.Page;
import org.neo4j.kernel.store.format.dsl.type.CompoundField;
import org.neo4j.kernel.store.format.dsl.type.PrimitiveFieldType;
import org.neo4j.kernel.store.format.dsl.type.RepeatingFixedSizeField;

/**
 * A collection of built-in field types.
 */
public class FieldTypes
{
    private static final FieldType BYTE = new PrimitiveFieldType( 1, byte.class, Page.getByte, Page.putByte );

    private static final FieldType FIXED_SIGNED_INT16 = new PrimitiveFieldType( 2, short.class, Page.getShort, Page.putShort);
    private static final FieldType FIXED_SIGNED_INT32 = new PrimitiveFieldType( 4, int.class,   Page.getInt,   Page.putInt);
    private static final FieldType FIXED_SIGNED_INT64 = new PrimitiveFieldType( 8, long.class,  Page.getLong,  Page.putLong);

    public static FieldType single_byte() { return BYTE; }
    public static FieldType fixed_signed_int16() { return FIXED_SIGNED_INT16; }
    public static FieldType fixed_signed_int32() { return FIXED_SIGNED_INT32; }
    public static FieldType fixed_signed_int64() { return FIXED_SIGNED_INT64; }

    public static FieldType compound_field( Object ... alternatingSubFieldNameAndFieldType )
    {
        return new CompoundField( alternatingSubFieldNameAndFieldType );
    }

    public static FieldType repeat( FieldType fieldToRepeat )
    {
        return new RepeatingFixedSizeField( fieldToRepeat );
    }
}
