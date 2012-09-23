package com.lucasia.xquery;

import net.sf.saxon.om.NamespaceResolver;
import net.sf.saxon.query.QueryModule;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.query.XQueryFunction;
import net.sf.saxon.query.XQueryFunctionLibrary;

import javax.xml.namespace.QName;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * User: ialucas
 */
public class XQueryFlattener {

    private Writer writer;

    public XQueryFlattener(Writer writer) {
        this.writer = writer;
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


    public void flatten(XQueryExpression expression) throws Exception {
        final StringBuffer buffer = new StringBuffer();

        final QueryModule staticContext = expression.getStaticContext();

        // namespaces at the top
        buffer.append(flattenNamespaces(staticContext));

        // module content
        buffer.append(flattenModules(staticContext));

        writeBuffer(buffer);
    }

    private void writeBuffer(StringBuffer buffer) throws IOException {
        // chunk up the buffer if we have a large module
        for (int start = 0; start < buffer.length(); start += 1024) {

            int end = start + 1024;
            if (end > buffer.length()) {
                end = buffer.length();
            }

            writer.write(buffer.substring(start, end));
        }
    }

    private String flattenNamespaces(QueryModule staticContext) {
        StringBuffer buffer = new StringBuffer();

        Predicate<String> excludeReservedPrefices = new Predicate<String>() {
            @Override
            boolean eval(String prefix) {
                return !"xml".equals(prefix);
            }
        };

        Set<QName> namespaces = getNamespaces(staticContext, excludeReservedPrefices);
        for (QName namespace : namespaces) {
            buffer.append("declare namespace ");
            buffer.append(namespace.getPrefix()).append(" = ");
            buffer.append("\"").append(namespace.getNamespaceURI()).append("\";");
            buffer.append("\n");
        }

        return buffer.toString();
    }

    private String flattenModules(QueryModule staticContext) throws IOException {
        StringBuffer buffer = new StringBuffer();

        final String mainModuleSystemId = staticContext.getTopLevelModule().getSystemId();

        final Set<String> moduleSystemIds = getModuleSystemIds(staticContext);
        for (String moduleSystemId : moduleSystemIds) {
            // skip the main module for now
            if (moduleSystemId.equals(mainModuleSystemId)) continue;

            final String xqFileContents = readFileExcludeNamespace(moduleSystemId);
            buffer.append(xqFileContents);
        }

        // add main module last
        final String xqFileContents = readFileExcludeNamespace(mainModuleSystemId);
        buffer.append(xqFileContents);

        return buffer.toString();
    }


    private Set<QName> getNamespaces(QueryModule staticContext, Predicate<String> predicate) {
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
    private Set<String> getModuleSystemIds(QueryModule staticContext) {
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


    public static String readFile(final String filePath) throws IOException {
        return readFile(filePath, new AlwaysTrue<String>());
    }

    /**
     * exclude the namespace imports and definitions, taking care of those elsewhere
     */
    public static String readFileExcludeNamespace(final String filePath) throws IOException {
        final Predicate<String> excludeNamespaceLine = new Predicate<String>() {

            @Override
            boolean eval(final String line) {

                if (!line.contains("namespace")) return true;

                String str = line.replace(" ", "");

                // using startsWith in case these words appear in the code elsewhere
                // e.g. strings, comments, xml tags
                return !(str.startsWith("modulenamespace") ||
                         str.startsWith("importmodulenamespace") ||
                         str.startsWith("declarenamespace"));
            }
        };

        return readFile(filePath, excludeNamespaceLine);
    }

    public static String readFile(String filePath, final Predicate<String> shouldAppend) throws IOException {
        filePath = filePath.replace("file:", "");

        final FileReader fileReader = new FileReader(filePath);

        final BufferedReader bufferedReader = new BufferedReader(fileReader);

        final StringBuffer buffer = new StringBuffer();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (shouldAppend.eval(line)) {
                buffer.append(line).append("\n");
            }
        }

        fileReader.close();

        return buffer.toString();
    }
}
