import module namespace trade = "http://lucasia.com/xquery/reference/trade" at "../../../main/xquery/reference/trade.xqy";

import module namespace xml-factory = "http://lucasia.com/xquery/reference/test/xml-factory" at "xml-factory.xqy";

import module namespace xq = "http://marklogic.com/xqunit" at "../lib/xqunit.xqy";

declare function local:create-selectivity-nodes-test() 
{
    let $ticket as xs:string := "1"
    let $partyName as xs:string := "UBS"

    let $expected as element()+ := (<ticket>{$ticket}</ticket>, <party>{$partyName}</party>)
                    
    let $actual as element()* := trade:create-selectivity-nodes(xml-factory:create-trade($ticket, $partyName))                 

    return xq:assert-equal("create-selectivity-nodes-test()", $actual, $expected)
};

declare function local:create-industry-node-test() 
{
    let $ticket as xs:string := "1"
    let $partyName as xs:string := "UBS"
    let $industryName as xs:string := "Finance"    

    let $party as element(party) := xml-factory:create-party($partyName, $industryName)
    
    let $trade as element(trade) := xml-factory:create-trade($ticket, $partyName)
                     

   return (
            xq:assert-equal("create-industry-node-test() ", trade:create-industry-node($trade, $party), <industry>{$industryName}</industry>),
            xq:assert-equal("create-industry-node-test() no industry", trade:create-industry-node($trade, ()), <industry/>)
          )
};


<results> 
{   
    local:create-selectivity-nodes-test(),
    local:create-industry-node-test() 
}
</results>