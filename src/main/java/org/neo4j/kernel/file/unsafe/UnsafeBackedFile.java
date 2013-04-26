package org.neo4j.kernel.file.unsafe;

import static java.nio.file.Files.newByteChannel;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.channels.FileChannel;

import org.neo4j.kernel.file.File;
import org.neo4j.kernel.file.FileSystem;
import org.neo4j.kernel.file.Page;
import org.neo4j.kernel.file.nio.NioBackedPath;

public class UnsafeBackedFile implements File
{

    private final FileChannel channel;
    private final FileChannel.MapMode mapMode;

    public UnsafeBackedFile( NioBackedPath path, FileSystem.OpenMode mode ) throws IOException
    {
        switch ( mode )
        {
            case READ:
                channel = (FileChannel) newByteChannel( path.getNioPath(), READ );
                mapMode = FileChannel.MapMode.READ_ONLY;
                break;
            case WRITE:
                channel = (FileChannel) newByteChannel( path.getNioPath(), WRITE );
                mapMode = FileChannel.MapMode.READ_WRITE;
                break;
            case READ_AND_WRITE:
                channel = (FileChannel) newByteChannel( path.getNioPath(), READ, WRITE );
                mapMode = FileChannel.MapMode.READ_WRITE;
                break;
            default:
                throw new IllegalArgumentException( "Unknown open mode: " + mode );
        }
    }

    @Override
    public Page mmap( long position, long size ) throws IOException
    {
        return new UnsafeBackedPage(channel.map( mapMode, position, size ));
    }

    @Override
    public void close() throws IOException
    {
        channel.close();
    }
}
