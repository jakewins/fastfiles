package org.neo4j.kernel.file;

import java.io.IOException;

public interface File
{
    Page mmap( long offset, long size ) throws IOException;

    void close() throws IOException;
}
