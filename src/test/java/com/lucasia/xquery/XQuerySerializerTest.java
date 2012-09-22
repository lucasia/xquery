package com.lucasia.xquery;

import junit.framework.Assert;
import net.sf.saxon.query.XQueryExpression;
import org.junit.Test;

import java.io.StringWriter;

public class XQuerySerializerTest {

    @Test
    public void testSerialize() throws Exception {
        XQueryExpression expression = new XQuery().compileQuery("src/test/xquery/sample/hello-world.xqy");

        StringWriter writer = new StringWriter();

        new XQuerySerializer(writer).serialize(expression);

        Assert.assertEquals("declare function local:message($msg as xs:string)\n" +
                "as item()\n" +
                "{\n" +
                "\t$msg\n" +
                "};\n" +
                "\n" +
                "local:message(\"hello-world\")", writer.getBuffer().toString());
    }


}
