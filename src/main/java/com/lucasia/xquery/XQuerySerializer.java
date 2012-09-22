package com.lucasia.xquery;

import net.sf.saxon.expr.Expression;
import net.sf.saxon.expr.StaticContext;
import net.sf.saxon.expr.instruct.FixedElement;
import net.sf.saxon.expr.instruct.UserFunctionParameter;
import net.sf.saxon.om.NamespaceBinding;
import net.sf.saxon.om.NamespaceResolver;
import net.sf.saxon.om.NodeName;
import net.sf.saxon.query.QueryModule;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.query.XQueryFunction;
import net.sf.saxon.query.XQueryFunctionLibrary;
import net.sf.saxon.trans.XPathException;

import javax.xml.namespace.QName;
import java.io.Writer;
import java.util.*;

/**
 * User: ialucas
 */
public class XQuerySerializer {

    private Writer writer;

    public XQuerySerializer(Writer writer) {
        this.writer = writer;
    }

    public void serialize(XQueryExpression expression) throws Exception {
        final StringBuffer buffer = new StringBuffer();

        final QueryModule staticContext = expression.getStaticContext();

        // namespaces at the top
        for (QName namespace : getNamespaces(staticContext)) {
            buffer.append("declare namespace ");
            buffer.append(namespace.getLocalPart()).append(" = ");
            buffer.append("\"").append(namespace.getNamespaceURI()).append("\";");
            buffer.append("\n");
        }
        buffer.append("\n");

        // module content
        XQueryFunctionLibrary functionLibrary = staticContext.getGlobalFunctionLibrary();

        List<String> modules = serialize(functionLibrary.getFunctionDefinitions());
        for (String module : modules) {
            buffer.append(module);
        }

        // main method
        buffer.append("\n");
        buffer.append(expression.getExpression().toString());

        writer.write(buffer.toString());
    }

    private Set<QName> getNamespaces(QueryModule staticContext) {
        NamespaceResolver resolver = staticContext.getNamespaceResolver();

        Iterator<String> prefixIterator = resolver.iteratePrefixes();
        Set<QName> namespaces = new HashSet<QName>();

        while(prefixIterator.hasNext()) {
            String localPart = prefixIterator.next();
            String uri = resolver.getURIForPrefix(localPart, false);

            // not allowed to specify xml prefix
            if (!"xml".equals(localPart)) {
                namespaces.add(new QName(uri, localPart));
            }
        }

        return namespaces;
    }

    public List<String> serialize(Iterator<XQueryFunction> funcDefinitionIter) throws XPathException {
        final List<String> modules = new ArrayList<String>();


        while (funcDefinitionIter.hasNext()) {
            XQueryFunction queryFunction = funcDefinitionIter.next();

            final String module = serialize(queryFunction);

            modules.add(module);
        }


        return modules;
    }


    private String serialize(XQueryFunction queryFunction) throws XPathException {
        StringBuffer buffer = new StringBuffer();

        // header
        buffer.append("declare function").append(" ");
        buffer.append(queryFunction.getDisplayName());

        // param
        buffer.append("(");
        for (int i = 0; i < queryFunction.getParameterDefinitions().length; i++) {
            UserFunctionParameter parameter = queryFunction.getParameterDefinitions()[i];
            if (i > 0) {
                buffer.append(", ");
            }

            buffer.append(serialize(parameter));

        }
        buffer.append(")");
        buffer.append("\n");

        // return type
        buffer.append("as ").append(queryFunction.getResultType());
        buffer.append("\n");

        // body
        buffer.append("{").append("\n");
        Expression body = queryFunction.getBody();
//         CharSequence s = body.evaluateAsString(null);  // TODO: wtf

        buffer.append("\t").append(body).append("\n");
        buffer.append("};");
        buffer.append("\n");

        return buffer.toString();
    }

    private String serialize(UserFunctionParameter parameter) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("$").append(parameter.getVariableQName()).append(" as ");
        buffer.append(parameter.getRequiredType());

        return buffer.toString();

    }
}
