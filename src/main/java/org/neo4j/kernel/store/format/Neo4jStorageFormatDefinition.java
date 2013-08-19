package org.neo4j.kernel.store.format;

import java.io.IOException;

import org.neo4j.kernel.store.format.dsl.FieldType;
import org.neo4j.kernel.store.format.dsl.StoreFormat;
import org.neo4j.kernel.store.format.dsl.StoreFormatWriter;

import static org.neo4j.kernel.store.format.dsl.FieldTypes.compound_field;
import static org.neo4j.kernel.store.format.dsl.FieldTypes.fixed_signed_int16;
import static org.neo4j.kernel.store.format.dsl.FieldTypes.fixed_signed_int32;

public class Neo4jStorageFormatDefinition
{

    private static final FieldType TRANSACTION_ID = fixed_signed_int32();

    /**
     * Assuming an average item size of about 40 bytes, 32 + 16 bits give us
     *  ~ 280 000 000 000 000 addressable items, or
     *  ~ 1 280 TB of addressable space
     *
     * We could've gone with 32 + 8, but then we're down at 5 Tb addressable space,
     * instead, we should look into specialized item types that use page-local addresses
     * as much as possible. We can also look into more complex item pointers that can
     * vary in size.
     */
    private static final FieldType ITEM_POINTER   =
            compound_field(
                "page",   fixed_signed_int32(),
                "itemNo", fixed_signed_int16());

    public StoreFormat defineFormat()
    {
        StoreFormat format = new StoreFormat("org.neo4j.kernel.store.format.Neo4jStorageFormat");

        format.defineItem( "NodeItem" )
            .field( "createdTxId",       TRANSACTION_ID )
            .field( "removedTxId",       TRANSACTION_ID )
            .field( "firstRelationship", ITEM_POINTER);

        format.defineItem( "RelationshipItem" )
            .field( "createdTxId",              TRANSACTION_ID )
            .field( "removedTxId",              TRANSACTION_ID )
            .field( "fromNode",                 ITEM_POINTER)
            .field( "toNode",                   ITEM_POINTER)
            .field( "nextFromNodeRelationship", ITEM_POINTER)
            .field( "nextToNodeRelationship",   ITEM_POINTER);

        return format;
    }

    public static void main(String ... args) throws IOException
    {
        new StoreFormatWriter().write(new Neo4jStorageFormatDefinition().defineFormat());
    }

}
