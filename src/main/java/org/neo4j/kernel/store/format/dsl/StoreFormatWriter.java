package org.neo4j.kernel.store.format.dsl;

import java.io.IOException;

import org.neo4j.kernel.file.FileSystem;
import org.neo4j.kernel.file.Path;
import org.neo4j.kernel.file.unsafe.UnsafeBackedFileSystem;
import org.neo4j.kernel.util.Pair;

import static org.neo4j.kernel.file.FileUtil.writeToFile;

/**
 * Writes store formats to disk.
 */
public class StoreFormatWriter
{

    public void write( StoreFormat format ) throws IOException
    {
        FileSystem fs = new UnsafeBackedFileSystem();

        for ( Pair<String, String> filenameAndContentsPair : format.build() )
        {
            String className = filenameAndContentsPair.first();
            writeToFile( fs, pathToSaveClassAt( fs, className ), filenameAndContentsPair.second() );
        }
    }

    private static Path pathToSaveClassAt( FileSystem fs, String className )
    {
        // TODO: Obviously don't hard-code the path here.

        String[] classPathParts = className.split( "\\." );
        String[] fullPathParts = new String[classPathParts.length + 3];

        int i = 0;
        fullPathParts[i++] = "src";
        fullPathParts[i++] = "main";
        fullPathParts[i++] = "java";

        for ( int classPathPartIndex=0;classPathPartIndex<classPathParts.length;classPathPartIndex++ )
        {
            fullPathParts[i++] = classPathParts[classPathPartIndex];
        }

        fullPathParts[fullPathParts.length-1] = fullPathParts[fullPathParts.length-1] + ".java";

        return fs.path( "", fullPathParts );
    }
}
