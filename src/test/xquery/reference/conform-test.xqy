import module namespace conform = "http://lucasia.com/xquery/reference/conform" at "../../../main/xquery/reference/conform.xqy";

import module namespace xml-factory = "http://lucasia.com/xquery/reference/test/xml-factory" at "xml-factory.xqy";

import module namespace xq = "http://marklogic.com/xqunit" at "../lib/xqunit.xqy";

declare function local:test-conform-economic-terms() 
{
    let $ticket as xs:string := "1"
    let $partyName as xs:string := "UBS"
    let $currency as xs:string := "JPY"
    let $amount as xs:decimal :=  123.45

    let $trade as element(trade-raw) := xml-factory:create-trade($ticket, $partyName, $currency, $amount)
    
    let $expected as element()+ := (<ticket>{$ticket}</ticket>, <party>{$partyName}</party>, 
                                    <currency>{$currency}</currency>, <amount>{$amount}</amount>)
                    
    let $actual as element()* := conform:conform-economic-terms($trade)                 

    return xq:assert-equal("create-selectivity-nodes-test()", $actual, $expected)
};

declare function local:test-conform-industry() 
{
    let $ticket as xs:string := "1"
    let $partyName as xs:string := "UBS"
    let $industryName as xs:string := "Finance"    

    let $party as element(party-raw) := xml-factory:create-party($partyName, $industryName)
    
    let $trade as element(trade-raw) := xml-factory:create-trade($ticket, $partyName)
                     

   return (
            xq:assert-equal("create-industry-node-test() ", conform:conform-industry($trade, $party), <industry>{$industryName}</industry>),
            xq:assert-equal("create-industry-node-test() no industry", conform:conform-industry($trade, ()), <industry/>)
          )
};


<results> 
{   
    local:test-conform-economic-terms(),
    local:test-conform-industry() 
}
</results>