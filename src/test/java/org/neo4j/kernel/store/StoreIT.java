package org.neo4j.kernel.store;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.kernel.file.FileSystem;
import org.neo4j.kernel.file.Page;
import org.neo4j.kernel.file.unsafe.UnsafeBackedFileSystem;
import org.neo4j.kernel.util.BytePrinter;

import static java.nio.channels.FileChannel.MapMode.READ_WRITE;
import static java.nio.file.Files.newByteChannel;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

public class StoreIT
{

    private Store store;
    private FileSystem fs = new UnsafeBackedFileSystem();

    @Before
    public void setUp() throws Exception
    {
        org.neo4j.kernel.file.Path storePath = fs.path( ".", "myStore" );
        if(fs.exists( storePath ))
        {
            fs.delete( storePath );
        }
        store = new Store( storePath, fs);
        store.init();
        store.start();
    }

    @After
    public void tearDown() throws Exception
    {
        store.stop();
        store.shutdown();
    }

    @Test
    public void testFollowingBasicPointersInACircle() throws Exception
    {
        ItemIdentifier firstNode = createCircleOfNodes();

        short currentIdOffset = firstNode.identifierOffset();

        long iterations = 10000000000l;
        long currentIteration = iterations;

        Page page = store.acquirePage( firstNode.pageNumber() );
        try
        {
            long start = System.currentTimeMillis();
            while( currentIteration--> 0)
            {
                    // Find where to look
                    short itemOffset = page.getShort( currentIdOffset );
                    // Get the id of the next node
                    currentIdOffset = page.getShort( itemOffset + 8 );
            }
            long delta = System.currentTimeMillis() - start;

            System.out.println((iterations/(delta*1.0)) + " hops/ms");
            System.out.println(Math.round(iterations/(delta*1.0) * 1000) + " hops/s");
        } finally
        {
            store.releasePage( page );
        }
    }

    private ItemIdentifier createCircleOfNodes()
    {
        // Pointer to previous node
        ItemIdentifier firstNode = store.allocateNewItem( (short) 10 );
        ItemIdentifier previousNode = firstNode;

        int numberOfNodes = 5;
        while(numberOfNodes --> 0)
        {
            previousNode = createNode( previousNode );
        }

        // Close the circle
        createRelationship( firstNode, previousNode );

        return firstNode;
    }

    private ItemIdentifier createNode( ItemIdentifier previousNode )
    {
        // Allocate a new item
        ItemIdentifier item = store.allocateNewItem( (short) 10 );
        createRelationship( item, previousNode );
        return item;
    }

    private void createRelationship( ItemIdentifier from, ItemIdentifier to )
    {
        // Write some data to our item
        Page page = store.acquirePage( from.pageNumber() );
        try
        {
            short itemOffset = store.itemOffsetInPage( page, from );
            // Node page number
            page.putLong(  itemOffset,     to.pageNumber() );
            // Node item id
            page.putShort( itemOffset + 8, to.identifierOffset() );
        }
        finally
        {
            store.releasePage( page );
        }
    }


    private void dump( Path path ) throws IOException
    {
        FileChannel file;
        file = (FileChannel) newByteChannel( path, READ,
                WRITE );
        MappedByteBuffer buffer = file.map( READ_WRITE, 0, file.size() );
        BytePrinter.print( buffer, System.out );
        file.close();
    }

}
