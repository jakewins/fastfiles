package org.neo4j.kernel.file;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.kernel.file.unsafe.UnsafeBackedFileSystem;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.neo4j.kernel.file.FileSystem.OpenMode.READ;
import static org.neo4j.kernel.file.FileSystem.OpenMode.READ_AND_WRITE;

public class PageIT
{

    private FileSystem fs;
    private File file;
    private Path path;

    @Before
    public void setUp() throws IOException
    {
        fs = new UnsafeBackedFileSystem();
        path = fs.path( ".", "target", getClass().getSimpleName() );
        if(fs.exists(path))
        {
            fs.delete(path);
        }
        fs.create( path );
        file = fs.open( path, READ_AND_WRITE);
    }

    @After
    public void cleanup() throws IOException
    {
        file.close();
    }

    @Test
    public void shouldReadAndWriteLongs() throws Exception
    {
        // Given
        Page page = file.mmap( 0, 128 );

        // When
        page.putLong( 0, 1337l );

        // Then
        assertEquals(1337l, page.getLong( 0 ));

        // When
        page.flush();
        file.close();

        // Then data should be stored
        File reOpenedFile = fs.open( path, READ );
        page = reOpenedFile.mmap( 0, 128 );
        assertEquals(1337l, page.getLong( 0 ));
    }

    @Test
    public void shouldReadAndWriteBytes() throws Exception
    {
        // Given
        Page page = file.mmap( 0, 128 );

        // When
        page.putByte( 0, (byte)124 );
        page.putByte( 10, (byte)126 );

        // Then
        assertEquals(124, page.getByte( 0 ));
        assertEquals(126, page.getByte( 10 ));

        // When
        page.flush();
        file.close();

        // Then data should be stored
        File reOpenedFile = fs.open( path, READ );
        page = reOpenedFile.mmap( 0, 128 );

        assertEquals(124, page.getByte( 0 ));
        assertEquals(126, page.getByte( 10 ));
    }

    @Test
    public void shouldReadAndWriteByteArrays() throws Exception
    {
        // Given
        Page page = file.mmap( 0, 128 );

        // When
        page.putBytes( 0, new byte[]{1, 2, 3}, 0, 3 );

        // Then
        assertEquals(1, page.getByte( 0 ));
        assertEquals(2, page.getByte( 1 ));
        assertEquals(3, page.getByte( 2 ));

        // When
        page.flush();
        file.close();

        // Then data should be stored
        File reOpenedFile = fs.open( path, READ );
        page = reOpenedFile.mmap( 0, 128 );

        byte[] read = new byte[3];
        page.getBytes( 0, 3, read);

        assertThat( read, equalTo(new byte[]{1, 2, 3}) );
    }
}
