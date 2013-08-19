/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.kernel.util.functional;

public interface Function<ARGS,RETURN>
{

    RETURN apply(ARGS arguments);

}
