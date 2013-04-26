package org.neo4j.kernel.file;

import static junit.framework.Assert.assertEquals;
import static org.neo4j.kernel.file.FileSystem.OpenMode.READ;
import static org.neo4j.kernel.file.FileSystem.OpenMode.READ_AND_WRITE;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.kernel.file.unsafe.UnsafeBackedFileSystem;

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
}
