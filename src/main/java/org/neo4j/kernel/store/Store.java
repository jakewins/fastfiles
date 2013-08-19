package org.neo4j.kernel.store;

import java.io.IOException;

import org.neo4j.kernel.file.File;
import org.neo4j.kernel.file.FileSystem;
import org.neo4j.kernel.file.Page;
import org.neo4j.kernel.file.Path;

import static java.lang.String.format;
import static org.neo4j.kernel.file.FileSystem.OpenMode.READ_AND_WRITE;

/**
 * A store provides storage of Items in Pages, where a Page is a fixed-size block of a file which contains zero or more
 * items of arbitrary size, although no item can be larger than {@link #maxItemSize()}.
 */
public class Store
{
    public static final int HEADER_NUM_ITEM_IDENTIFIERS = 0;
    public static final int HEADER_ITEM_IDENTIFIERS     = 2;

    public static final int SIZE_ITEM_IDENTIFIER = 2;

    private final Path path;
    private final FileSystem fs;
    private final StoreHeader header;

    private File file;
    private Page theOnlyPageWeHaveRightNow;

    public Store( Path path, FileSystem fs )
    {
        this.path = path;
        this.fs = fs;
        this.header = new StoreHeader(path, fs);
    }

    public void init() throws Exception
    {
        if(!fs.exists( path ))
        {
            fs.create( path );
        }
        header.init();

        file = fs.open( path, READ_AND_WRITE );
        theOnlyPageWeHaveRightNow = file.mmap( 0, 5600 );
    }

    public void start()
    {
    }

    public void stop()
    {
    }

    public void shutdown() throws IOException
    {
        theOnlyPageWeHaveRightNow.flush();
        file.close();
    }

    public Page acquirePage( long pageNumber )
    {
        return theOnlyPageWeHaveRightNow;
    }

    public void releasePage( Page page )
    {

    }

    //
    // Write operations
    //

    public ItemIdentifier allocateNewItem( short size )
    {
        assert size < maxItemSize() : format("Item larger than allowed: %d requested, %d available).", size, maxItemSize());

        // Create a page with space in it
        long pageId = header.newPageId();

        Page page = acquirePage( pageId );
        try
        {
            // Values we need to figure out
            short identifierOffset;
            short itemOffset;

            // Read page header, find free space
            short numItems = page.getShort( HEADER_NUM_ITEM_IDENTIFIERS );
            if(numItems > 0)
            {
                short lowestItemOffset = maxItemSize();
                for(short currentItemIdentifier=0; currentItemIdentifier<numItems; currentItemIdentifier++)
                {
                    // Go through each item, find lowest offset. Because items are stored at the "end" of the page,
                    // the lowest offset will tell us where we can put the next item (lowestOffset - size)
                    short currentItemOffset = page.getShort( HEADER_ITEM_IDENTIFIERS +
                                                            (SIZE_ITEM_IDENTIFIER * currentItemIdentifier) );
                    if(currentItemOffset < lowestItemOffset)
                    {
                        lowestItemOffset = currentItemOffset;
                    }
                }

                itemOffset = (short)(lowestItemOffset - size);
                identifierOffset = (short)(HEADER_ITEM_IDENTIFIERS + (SIZE_ITEM_IDENTIFIER * numItems));
            } else
            {
                // Empty page, start at the end
                itemOffset = (short)(maxItemSize() - size);
                identifierOffset = HEADER_ITEM_IDENTIFIERS;
            }

            // Record this happy event for posterity
            page.putShort( identifierOffset, itemOffset );
            page.putShort( HEADER_NUM_ITEM_IDENTIFIERS, (short) (numItems + 1) );

            return new ItemIdentifier( pageId, identifierOffset );
        } finally
        {
            releasePage( page );
        }
    }

    //
    // Read operations
    //

    public short itemOffsetInPage( Page page, ItemIdentifier item )
    {
        return page.getShort( item.identifierOffset() );
    }

    public short maxItemSize()
    {
        return 8192;
    }

    //
    // Internals
    //
}
