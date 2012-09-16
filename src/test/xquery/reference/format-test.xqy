import module namespace format = "http://lucasia.com/xquery/reference/format" at "../../../main/xquery/reference/format.xqy";

import module namespace xml-factory = "http://lucasia.com/xquery/reference/test/xml-factory" at "xml-factory.xqy";

import module namespace xq = "http://marklogic.com/xqunit" at "../lib/xqunit.xqy";

declare function local:test-tabular-row() 
{
    let $input := (<first>joe</first>, <last>smith</last>)
    
    let $actual := format:tabular-row($input)
    
    let $expected := <tr><td name="first">joe</td><td name="last">smith</td></tr>

    return xq:assert-equal("create-selectivity-nodes-test()", $actual, $expected)
};

declare function local:test-tabular() 
{
    let $input := <person><first>joe</first><last>smith</last></person>
    
    let $actual := format:tabular($input)
    
    let $expected := <tr><td name="first">joe</td><td name="last">smith</td></tr>

    return xq:assert-equal("create-selectivity-nodes-test()", $actual, $expected)
};

<results> 
{   
    local:test-tabular-row(),
    local:test-tabular()
}
</results>