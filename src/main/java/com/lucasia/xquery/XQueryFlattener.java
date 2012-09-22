package com.lucasia.xquery;

import net.sf.saxon.om.NamespaceResolver;
import net.sf.saxon.query.QueryModule;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.query.XQueryFunction;
import net.sf.saxon.query.XQueryFunctionLibrary;
import net.sf.saxon.trans.XPathException;

import javax.xml.namespace.QName;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * User: ialucas
 */
public class XQueryFlattener {

    private Writer writer;

    public XQueryFlattener(Writer writer) {
        this.writer = writer;
    }


    public abstract static class Predicate {
        abstract boolean include(String str);
    }


    public void flatten(XQueryExpression expression) throws Exception {
        final StringBuffer buffer = new StringBuffer();

        final QueryModule staticContext = expression.getStaticContext();

        // namespaces at the top
        for (QName namespace : getNamespaces(staticContext)) {
            buffer.append("declare namespace ");
            buffer.append(namespace.getPrefix()).append(" = ");
            buffer.append("\"").append(namespace.getNamespaceURI()).append("\";");
            buffer.append("\n");
        }

        // module content
        XQueryFunctionLibrary functionLibrary = staticContext.getGlobalFunctionLibrary();

        List<String> modules = flatten(functionLibrary.getFunctionDefinitions());
        for (String module : modules) {
            buffer.append(module);
        }

        writer.write(buffer.toString());
    }

    private Set<QName> getNamespaces(QueryModule staticContext) {
        NamespaceResolver resolver = staticContext.getNamespaceResolver();

        Iterator<String> prefixIterator = resolver.iteratePrefixes();
        Set<QName> namespaces = new HashSet<QName>();

        while (prefixIterator.hasNext()) {
            String prefix = prefixIterator.next();
            String uri = resolver.getURIForPrefix(prefix, false);

            // not allowed to specify xml prefix
            if (!"xml".equals(prefix)) {
                namespaces.add(new QName(uri, "", prefix));
            }
        }

        return namespaces;
    }

    public List<String> flatten(Iterator<XQueryFunction> iterator) throws XPathException, IOException {
        final List<String> modules = new ArrayList<String>();

        while (iterator.hasNext()) {
            final XQueryFunction queryFunction = iterator.next();

            final String fileName = queryFunction.getSystemId().replace("file:", "");

            final String xqFile = readFile(fileName, new Predicate() {
                @Override
                boolean include(String str) {
                    return !str.contains("namespace"); // exclude the namespace imports, took care of those already
                }
            });

            modules.add(xqFile);
        }

        return modules;
    }

    public static String readFile(final String filePath) throws IOException {
        return readFile(filePath, new Predicate() {
            @Override
            boolean include(String str) {
                return true;
            }
        });
    }

    public static String readFile(final String filePath, final Predicate predicate) throws IOException {

        final FileReader fileReader = new FileReader(filePath);

        final BufferedReader bufferedReader = new BufferedReader(fileReader);

        final StringBuffer buffer = new StringBuffer();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (predicate.include(line)) {
                buffer.append(line).append("\n");
            }
        }

        fileReader.close();

        return buffer.toString();
    }
}
