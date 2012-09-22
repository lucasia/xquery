package com.lucasia.xquery;

import net.sf.saxon.Configuration;
import net.sf.saxon.Query;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * User: ialucas
 */
public class XQuery extends Query {

    public void execute(final String xqFilePath, final PrintStream outputStream) throws Exception {

        final PrintStream origSystemOut = System.out;

        try {
            System.setOut(outputStream);

            doQuery(new String[] {xqFilePath}, "java net.sf.saxon.Query");

            outputStream.flush();

        } finally {
            System.setOut(origSystemOut);

            outputStream.close();
        }
    }

    public void execute(final InputStream inputStream, final PrintStream outputStream) throws Exception {
        final PrintStream origSystemOut = System.out;
        final InputStream origSystemIn = System.in;

        try {
            System.setIn(inputStream);
            System.setOut(outputStream);

            doQuery(new String [] {"-q:-"}, "java net.sf.saxon.Query");

            outputStream.flush();

        } finally {
            System.setIn(origSystemIn);
            System.setOut(origSystemOut);

            inputStream.close();
            outputStream.close();
        }
    }


    public XQueryExpression compileQuery(String xQueryPath) throws Exception {
        final StaticQueryContext staticQueryContext = new Configuration().getDefaultStaticQueryContext();

        XQueryExpression expression = compileQuery(staticQueryContext, xQueryPath, false);

        return expression;
    }

}
