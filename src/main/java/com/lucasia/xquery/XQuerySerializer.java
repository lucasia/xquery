package com.lucasia.xquery;

import net.sf.saxon.expr.instruct.UserFunctionParameter;
import net.sf.saxon.query.QueryModule;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.query.XQueryFunction;
import net.sf.saxon.query.XQueryFunctionLibrary;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: ialucas
 */
public class XQuerySerializer {

    private Writer writer;

    public XQuerySerializer(Writer writer) {
        this.writer = writer;
    }

    public void serialize(XQueryExpression expression) throws Exception {
        QueryModule staticContext = expression.getStaticContext();

        XQueryFunctionLibrary functionLibrary = staticContext.getGlobalFunctionLibrary();

        StringBuffer buffer = new StringBuffer();

        // functions
        List<String> functions = serialize(functionLibrary.getFunctionDefinitions());
        for (String function : functions) {
            buffer.append(function);
        }

        // main method
        buffer.append("\n");
        buffer.append(expression.getExpression().toString());

        writer.write(buffer.toString());
    }

    public List<String> serialize(Iterator<XQueryFunction> funcDefinitionIter) {
        final List<String> functions = new ArrayList<String>();


        while (funcDefinitionIter.hasNext()) {
            XQueryFunction queryFunction = funcDefinitionIter.next();

            final String serializedFunction = serialize(queryFunction);

            functions.add(serializedFunction);
        }


        return functions;
    }


    private String serialize(XQueryFunction queryFunction) {

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
        buffer.append("\t").append(queryFunction.getBody()).append("\n");
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
