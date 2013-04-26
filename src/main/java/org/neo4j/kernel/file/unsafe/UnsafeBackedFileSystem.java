package org.neo4j.kernel.file.unsafe;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import org.neo4j.kernel.file.File;
import org.neo4j.kernel.file.FileSystem;
import org.neo4j.kernel.file.Path;
import org.neo4j.kernel.file.nio.NioBackedPath;

public class UnsafeBackedFileSystem implements FileSystem
{

    private final java.nio.file.FileSystem actualFilesystem;

    public UnsafeBackedFileSystem()
    {
        actualFilesystem = FileSystems.getDefault();
    }

    @Override
    public void create( Path path ) throws IOException
    {
        Files.createFile( nioPath( path ) );
    }

    @Override
    public File open( Path path, OpenMode mode ) throws IOException
    {
        assertIsNioPath(path);
        return new UnsafeBackedFile( (NioBackedPath)path, mode );
    }

    @Override
    public boolean exists( Path path )
    {
        return Files.exists( nioPath( path ) );
    }

    @Override
    public void delete( Path path ) throws IOException
    {
        Files.delete(nioPath( path ));
    }

    @Override
    public Path path( String first, String... more )
    {
        return new NioBackedPath(actualFilesystem.getPath( first, more ));
    }

    private java.nio.file.Path nioPath( Path path )
    {
        assertIsNioPath( path );
        return ((NioBackedPath)path).getNioPath();
    }

    private void assertIsNioPath( Path path )
    {
        if(!(path instanceof NioBackedPath))
        {
            throw new UnsupportedOperationException( "You need to use paths returned by this filesystem!" );
        }
    }
}
