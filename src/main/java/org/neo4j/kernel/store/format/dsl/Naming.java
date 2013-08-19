package org.neo4j.kernel.store.format.dsl;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class Naming
{
    private static final Set<String> JAVA_KEYWORDS = new HashSet<>(asList("abstract","continue","for","new","switch",
            "assert","default","goto","package","synchronized","boolean","do","if","private","this","break","double",
            "implements","protected","throw","byte","else","import","public","throws","case","enum","instanceof",
            "return","transient","catch","extends","int","short","try","char","final","interface","static","void",
            "class","finally","long","strictfp","volatile","const","float","native","super","while"));

    public static String uCaseFirst( String str )
    {
        return Character.toUpperCase( str.charAt( 0 ) ) + str.substring( 1 );
    }

    public static void assertValidFieldName( String name )
    {
        if(name.length() == 0)
        {
            throw new RuntimeException("Invalid field name: Empty field names are not allowed.");
        }

        if(JAVA_KEYWORDS.contains( name ))
        {
            throw new RuntimeException( String.format("Invalid field name: '%s' is a reserved Java keyword.", name) );
        }
    }
}
