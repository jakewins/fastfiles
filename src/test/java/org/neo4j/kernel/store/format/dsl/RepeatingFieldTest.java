package org.neo4j.kernel.store.format.dsl;

import org.junit.Test;
import org.neo4j.kernel.file.Page;
import org.neo4j.kernel.file.TestPage;
import org.neo4j.kernel.util.reflection.DynamicObject;

import static org.junit.Assert.*;
import static org.neo4j.kernel.store.format.dsl.FieldTypes.fixed_signed_int64;
import static org.neo4j.kernel.store.format.dsl.StoreFormatTestCompiler.instantiateItem;

public class RepeatingFieldTest
{
    public static final int ITEM_OFFSET = 0;
    private Page testPage = new TestPage( 120 );;

    @Test
    public void shouldHandleUnlimitedRepeatingField() throws Exception
    {
        // Given
        StoreFormat format = new StoreFormat("org.neo4j.TestStoreFormat");

        // When
        format.defineItem( "MyItem" )
                .field( "repeatField", fixed_signed_int64() )
        ;

        // Then I should be able to compile it
        DynamicObject myItem = instantiateItem( format, "MyItem" );

        // Then sizeOf
        assertEquals( 8 + 4 + 2 + 1, myItem.call( "sizeOf" ) );

        // Then set and get
        myItem.call( "setALong", testPage, ITEM_OFFSET, Long.MAX_VALUE - 10 );
        assertEquals( Long.MAX_VALUE - 10, myItem.call( "getALong", testPage, ITEM_OFFSET ) );

        myItem.call( "setAnInt", testPage, ITEM_OFFSET, Integer.MAX_VALUE - 10 );
        assertEquals( Integer.MAX_VALUE - 10, myItem.call( "getAnInt", testPage, ITEM_OFFSET ) );

        myItem.call( "setAShort", testPage, ITEM_OFFSET, Short.MAX_VALUE );
        assertEquals( Short.MAX_VALUE, myItem.call( "getAShort", testPage, ITEM_OFFSET ) );
    }

}
