import module namespace trade = "http://lucasia.com/xquery/reference/trade" at "../../../main/xquery/reference/trade.xqy";

import module namespace xml-factory = "http://lucasia.com/xquery/reference/test/xml-factory" at "xml-factory.xqy";

import module namespace xq = "http://marklogic.com/xqunit" at "../lib/xqunit.xqy";

declare function local:functional-test() 
{
    let $ticket as xs:string := "Ticket-1"
    let $party as xs:string := "UBS"
    let $industry as xs:string := "Finance"    

    let $partyXML := <parties>{xml-factory:create-party($party, $industry)}</parties>
                    
   let $expected := <tr>
                        <td name="ticket">{$ticket}</td>
                        <td name="party">{$party}</td>
                        <td name="industry">{$industry}</td>
                    </tr>
                    
   let $actual := trade:transform(xml-factory:create-trade($ticket, $party), $partyXML/party)                 

   return xq:assert-equal("test", $actual, $expected)
};

declare function local:functional-test-missing-industry-lookup() 
{

    let $ticket as xs:string := "Ticket-1"
    let $party as xs:string := "UBS"

    let $expected := <tr>
                        <td name="ticket">{$ticket}</td>
                        <td name="party">{$party}</td>
                        <td name="industry"></td>
                    </tr>
                    
   let $actual := trade:transform(xml-factory:create-trade($ticket, $party), ())   

   return xq:assert-equal("test", $actual, $expected)
};

<results> 
{   
    local:functional-test(),
    local:functional-test-missing-industry-lookup()
}
</results>