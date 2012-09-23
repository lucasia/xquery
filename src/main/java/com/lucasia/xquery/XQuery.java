package com.lucasia.xquery;

import net.sf.saxon.Configuration;
import net.sf.saxon.Query;
import net.sf.saxon.om.NamespaceResolver;
import net.sf.saxon.query.*;

import javax.xml.namespace.QName;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * User: ialucas
 */
public class XQuery extends Query {

    public abstract static class Predicate<T> {
        abstract boolean eval(T t);
    }

    public static class AlwaysTrue<T> extends Predicate<T> {
        @Override
        boolean eval(T t) {
            return true;
        }
    }

    public void execute(final String xqFilePath, final PrintStream outputStream) throws Exception {

        final PrintStream origSystemOut = System.out;

        try {
            System.setOut(outputStream);

            doQuery(new String[]{xqFilePath}, "java net.sf.saxon.Query");

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

            doQuery(new String[]{"-q:-"}, "java net.sf.saxon.Query");

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


    public static Set<QName> getNamespaces(QueryModule staticContext, Predicate<String> predicate) {
        NamespaceResolver resolver = staticContext.getNamespaceResolver();

        Iterator<String> prefixIterator = resolver.iteratePrefixes();
        Set<QName> namespaces = new HashSet<QName>();

        while (prefixIterator.hasNext()) {
            String prefix = prefixIterator.next();
            String uri = resolver.getURIForPrefix(prefix, false);

            if (predicate.eval(prefix)) {
                namespaces.add(new QName(uri, "", prefix));
            }
        }

        return namespaces;
    }

    /**
     * return the Unique set of Module  URIs
     * These correspond either to Library or Main Modules
     */
    public static Set<String> getModuleSystemIds(QueryModule staticContext) {
        Set<String> systemIds = new HashSet<String>();

        // module content
        XQueryFunctionLibrary functionLibrary = staticContext.getGlobalFunctionLibrary();

        Iterator<XQueryFunction> iterator = functionLibrary.getFunctionDefinitions();

        while (iterator.hasNext()) {
            final XQueryFunction queryFunction = iterator.next();

            final String systemId = queryFunction.getSystemId();
            systemIds.add(systemId);
        }

        return systemIds;
    }


}
