/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.kernel.file;

import java.io.IOException;

public interface FileSystem
{
    public enum OpenMode
    {
        READ,
        WRITE,
        READ_AND_WRITE;
    }

    /**
     * Create a file.
     */
    void create( Path path ) throws IOException;

    File open( Path path, OpenMode mode ) throws IOException;

    boolean exists( Path path );

    void delete( Path path ) throws IOException;

    /**
     * Create a path object
     */
    Path path( String base, String ... segments );
}
