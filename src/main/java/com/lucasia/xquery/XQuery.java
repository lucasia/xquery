package com.lucasia.xquery;

import net.sf.saxon.Configuration;
import net.sf.saxon.Query;
import net.sf.saxon.om.NamespaceResolver;
import net.sf.saxon.query.*;
import net.sf.saxon.s9api.*;
import net.sf.saxon.trans.XPathException;

import javax.xml.namespace.QName;
import java.io.*;
import java.util.*;

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

    private static class XQCompiler extends XQueryCompiler {

        private XQCompiler() {
            super(new Processor(false));
        }
    }

    public String execute(final File file) throws Exception {
        final XQueryExecutable executable = new XQCompiler().compile(file);

        return execute(executable);
    }

    public String execute(final String xquery) throws Exception {
        final XQueryExecutable executable = new XQCompiler().compile(xquery);

        return execute(executable);
    }

    private String execute(final XQueryExecutable executable) throws Exception {
        final XQueryEvaluator evaluator = executable.load();

        final XdmValue value = evaluator.evaluate();

        return value.toString();
    }


    public XQueryExpression compileQuery(String xQueryPath) throws XPathException, IOException {
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
