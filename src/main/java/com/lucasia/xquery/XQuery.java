package com.lucasia.xquery;

import net.sf.saxon.Query;
import net.sf.saxon.om.NamespaceResolver;
import net.sf.saxon.query.QueryModule;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.query.XQueryFunction;
import net.sf.saxon.query.XQueryFunctionLibrary;
import net.sf.saxon.s9api.*;

import javax.xml.namespace.QName;
import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * User: ialucas
 */
public class XQuery extends Query {

    public XQueryExecutable compileQuery(final File xqFile) throws SaxonApiException, IOException {
        final XQueryExecutable executable = new XQCompiler().compile(xqFile);

        return executable;
    }

    public XQueryExecutable compileQuery(final String xquery) throws SaxonApiException, IOException {
        final XQueryExecutable executable = new XQCompiler().compile(xquery);

        return executable;
    }


    public String execute(final File xqFile) throws SaxonApiException, IOException {
        final XQueryExecutable executable = compileQuery(xqFile);

        return execute(executable);
    }

    public String execute(final String xquery) throws SaxonApiException, IOException {
        final XQueryExecutable executable = compileQuery(xquery);

        return execute(executable);
    }

    private String execute(final XQueryExecutable executable) throws SaxonApiException, IOException {
        final XQueryEvaluator evaluator = executable.load();

        final XdmValue value = evaluator.evaluate();

        return value.toString();
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


}
