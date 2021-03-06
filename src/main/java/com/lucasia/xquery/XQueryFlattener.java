package com.lucasia.xquery;

import net.sf.saxon.query.QueryModule;
import net.sf.saxon.s9api.XQueryExecutable;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 * User: ialucas
 */
public class XQueryFlattener {
    public static final int BUFF_LEN = 1024;


    private Writer writer;

    public XQueryFlattener(final Writer writer) {
        this.writer = writer;
    }


    public void flatten(final XQueryExecutable xQueryExecutable) throws IOException {
        final StringBuffer buffer = new StringBuffer();

        final QueryModule staticContext = xQueryExecutable.getUnderlyingCompiledQuery().getStaticContext();

        // namespaces at the top
        buffer.append(flattenNamespaces(staticContext));

        // module content
        buffer.append(flattenModules(staticContext));

        writeBuffer(buffer);
    }

    private String flattenNamespaces(final QueryModule staticContext) {
        StringBuffer buffer = new StringBuffer();

        XQuery.Predicate<String> excludeReservedPrefices = new XQuery.Predicate<String>() {
            @Override
            boolean eval(final String prefix) {
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

    private String flattenModules(final QueryModule staticContext) throws IOException {
        StringBuffer buffer = new StringBuffer();

        final XQueryFileReader fileReader = new XQueryFileReader();

        QueryModule topLevelModule = staticContext.getTopLevelModule();
        final String mainModuleSystemId = topLevelModule.getSystemId();
        assert (topLevelModule.isMainModule());

        final Set<String> moduleSystemIds = XQuery.getModuleSystemIds(staticContext);
        for (String moduleSystemId : moduleSystemIds) {
            // skip the main module for now
            if (moduleSystemId.equals(mainModuleSystemId)) {
                continue;
            }

            final String xqFileContents = fileReader.readFileExcludeNamespace(moduleSystemId);
            buffer.append(xqFileContents);
        }

        // add main module last
        final String xqFileContents = fileReader.readFileExcludeNamespace(mainModuleSystemId);
        buffer.append(xqFileContents);

        return buffer.toString();
    }

    private void writeBuffer(final StringBuffer buffer) throws IOException {
        // chunk up the buffer if we have a large module
        for (int start = 0; start < buffer.length(); start += BUFF_LEN) {

            int end = start + BUFF_LEN;
            if (end > buffer.length()) {
                end = buffer.length();
            }

            writer.write(buffer.substring(start, end));
        }
    }


}
