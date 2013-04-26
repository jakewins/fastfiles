package org.neo4j.kernel.store;

import org.neo4j.kernel.file.FileSystem;
import org.neo4j.kernel.file.Path;

public class StoreHeader
{

    public StoreHeader( Path path, FileSystem fs )
    {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void init()
    {

    }

    public long newPageId()
    {
        return 0l;
    }
}
