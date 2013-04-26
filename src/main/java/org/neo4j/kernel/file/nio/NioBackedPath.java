package org.neo4j.kernel.file.nio;

import org.neo4j.kernel.file.Path;

public class NioBackedPath implements Path
{
    private final java.nio.file.Path nioPath;

    public NioBackedPath( java.nio.file.Path nioPath )
    {
        this.nioPath = nioPath;
    }

    public java.nio.file.Path getNioPath()
    {
        return nioPath;
    }
}
