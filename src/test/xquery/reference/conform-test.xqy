import module namespace conform = "http://lucasia.com/xquery/reference/conform" at "../../../main/xquery/reference/conform.xqy";

import module namespace xml-factory = "http://lucasia.com/xquery/reference/test/xml-factory" at "xml-factory.xqy";

import module namespace xq = "http://marklogic.com/xqunit" at "../lib/xqunit.xqy";

declare function local:conform-economic-terms-test() 
{
    let $ticket as xs:string := "1"
    let $partyName as xs:string := "UBS"

    let $expected as element()+ := (<ticket>{$ticket}</ticket>, <party>{$partyName}</party>)
                    
    let $actual as element()* := conform:conform-economic-terms(xml-factory:create-trade($ticket, $partyName))                 

    return xq:assert-equal("create-selectivity-nodes-test()", $actual, $expected)
};

declare function local:conform-industry-test() 
{
    let $ticket as xs:string := "1"
    let $partyName as xs:string := "UBS"
    let $industryName as xs:string := "Finance"    

    let $party as element(party) := xml-factory:create-party($partyName, $industryName)
    
    let $trade as element(trade) := xml-factory:create-trade($ticket, $partyName)
                     

   return (
            xq:assert-equal("create-industry-node-test() ", conform:conform-industry($trade, $party), <industry>{$industryName}</industry>),
            xq:assert-equal("create-industry-node-test() no industry", conform:conform-industry($trade, ()), <industry/>)
          )
};


<results> 
{   
    local:conform-economic-terms-test() ,
    local:conform-industry-test() 
}
</results>