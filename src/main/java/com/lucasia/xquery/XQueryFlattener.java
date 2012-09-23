package com.lucasia.xquery;

import net.sf.saxon.query.QueryModule;
import net.sf.saxon.query.XQueryExpression;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 * User: ialucas
 */
public class XQueryFlattener {

    private Writer writer;

    public XQueryFlattener(Writer writer) {
        this.writer = writer;
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

    private String flattenNamespaces(QueryModule staticContext) {
        StringBuffer buffer = new StringBuffer();

        XQuery.Predicate<String> excludeReservedPrefices = new XQuery.Predicate<String>() {
            @Override
            boolean eval(String prefix) {
                return !"xml".equals(prefix);
            }
        };

        Set<QName> namespaces = XQuery.getNamespaces(staticContext, excludeReservedPrefices);
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

        final XQueryFileReader fileReader = new XQueryFileReader();

        final String mainModuleSystemId = staticContext.getTopLevelModule().getSystemId();

        final Set<String> moduleSystemIds = XQuery.getModuleSystemIds(staticContext);
        for (String moduleSystemId : moduleSystemIds) {
            // skip the main module for now
            if (moduleSystemId.equals(mainModuleSystemId)) continue;

            final String xqFileContents = fileReader.readFileExcludeNamespace(moduleSystemId);
            buffer.append(xqFileContents);
        }

        // add main module last
        final String xqFileContents = fileReader.readFileExcludeNamespace(mainModuleSystemId);
        buffer.append(xqFileContents);

        return buffer.toString();
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


}
