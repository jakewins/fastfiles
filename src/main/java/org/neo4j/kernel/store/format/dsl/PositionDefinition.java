package org.neo4j.kernel.store.format.dsl;

class PositionDefinition
{
    private final int offset;

    public PositionDefinition( int offset )
    {
        this.offset = offset;
    }

    public int offset()
    {
        return offset;
    }
}
