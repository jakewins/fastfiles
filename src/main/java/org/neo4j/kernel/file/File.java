package org.neo4j.kernel.file;

import java.io.Closeable;
import java.io.IOException;

public interface File extends Closeable
{
    Page mmap( long offset, long size ) throws IOException;

    void close() throws IOException;
}
