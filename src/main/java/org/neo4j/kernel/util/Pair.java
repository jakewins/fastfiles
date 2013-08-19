package org.neo4j.kernel.util;

public class Pair<FIRST, SECOND>
{

    private final FIRST first;
    private final SECOND second;

    public static <A,B> Pair<A,B> pair(A first, B second)
    {
        return new Pair(first, second);
    }

    public Pair(FIRST first, SECOND second)
    {

        this.first = first;
        this.second = second;
    }

    public FIRST first()
    {
        return first;
    }

    public SECOND second()
    {
        return second;
    }

}
