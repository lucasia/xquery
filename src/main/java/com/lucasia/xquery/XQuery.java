package com.lucasia.xquery;

import net.sf.saxon.Configuration;
import net.sf.saxon.Query;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
        // execute(xQueryPath); // todo: dependency on execute in compile, investigate and remove
        // StaticQueryContext staticQueryContext = getConfiguration().getDefaultStaticQueryContext();

        StaticQueryContext staticQueryContext = new Configuration().getDefaultStaticQueryContext();

        XQueryExpression expression = compileQuery(staticQueryContext, xQueryPath, false);

        return expression;
    }

}
