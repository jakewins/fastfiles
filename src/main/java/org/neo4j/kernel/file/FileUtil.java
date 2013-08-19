package org.neo4j.kernel.file;

import java.io.IOException;

import org.neo4j.kernel.util.UTF8;

public class FileUtil
{

    public static void writeToFile( FileSystem fs, Path path, String contents ) throws IOException
    {
        if(fs.exists( path ))
        {
            fs.delete( path );
        }
        fs.create( path );

        try(File output = fs.open( path, FileSystem.OpenMode.READ_AND_WRITE ))
        {
            byte[] encoded = UTF8.encode(contents);
            Page page = output.mmap( 0, encoded.length );
            page.putBytes( 0, encoded, 0, encoded.length );
        }
    }

}
