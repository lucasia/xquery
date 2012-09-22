package com.lucasia.xquery;

import net.sf.saxon.Configuration;
import net.sf.saxon.Query;
import net.sf.saxon.query.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;

/**
 * User: ialucas
 */
public class XQuery extends Query {

    public String execute(String xQueryPath) throws Exception {

        final PrintStream origSystemOut = System.out;

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            System.setOut(new PrintStream(stream));

            doQuery(new String[]{xQueryPath}, "");

            stream.flush();

            return stream.toString();

        } finally {
            System.setOut(origSystemOut);
        }

    }


    public XQueryExpression compileQuery(String xQueryPath) throws Exception {
        execute(xQueryPath); // todo: dependency on execute in compile, investigate and remove

        StaticQueryContext staticQueryContext = new Configuration().newStaticQueryContext();

        XQueryExpression expression = compileQuery(staticQueryContext, xQueryPath, true);

        return expression;
    }

}
