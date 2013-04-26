package org.neo4j.kernel.store;

public class ItemIdentifier
{
    private final long page;
    private final short identifierOffset;

    public ItemIdentifier( long page, short identifierOffset )
    {
        this.page = page;
        this.identifierOffset = identifierOffset;
    }

    public long pageNumber()
    {
        return page;
    }

    public short identifierOffset()
    {
        return identifierOffset;
    }

    @Override
    public String toString()
    {
        return String.format( "ItemIdentifier[page=%d, item=%s]", page, identifierOffset );
    }
}
