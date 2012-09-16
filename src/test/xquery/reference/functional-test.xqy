import module namespace report = "http://lucasia.com/xquery/reference/report" at "../../../main/xquery/reference/report.xqy";

import module namespace xml-factory = "http://lucasia.com/xquery/reference/test/xml-factory" at "xml-factory.xqy";

import module namespace xq = "http://marklogic.com/xqunit" at "../lib/xqunit.xqy";

declare function local:functional-test() 
{
    let $ticket as xs:string := "1"
    let $partyName as xs:string := "UBS"
    let $industryName as xs:string := "Finance"    

    let $parties as element(parties) := <parties>{xml-factory:create-party($partyName, $industryName)}</parties>
                    
    let $expected as element(tr) := <tr>
                                        <td name="ticket">{$ticket}</td>
                                        <td name="party">{$partyName}</td>
                                        <td name="industry">{$industryName}</td>
                                    </tr>
                    
   let $actual as element(tr)* := report:gen-transaction-report(xml-factory:create-trade($ticket, $partyName), $parties/party)                 

   return xq:assert-equal("functional-test()", $actual, $expected)
};

declare function local:functional-test-missing-industry-lookup() 
{

    let $ticket as xs:string := "1"
    let $partyName as xs:string := "UBS"

    let $expected as element(tr) :=  <tr>
                                        <td name="ticket">{$ticket}</td>
                                        <td name="party">{$partyName}</td>
                                        <td name="industry"/>
                                    </tr>
                    
                    
   let $actual as element(tr)* := report:gen-transaction-report(xml-factory:create-trade($ticket, $partyName), ())   

   return xq:assert-equal("functional-test-missing-industry-lookup()", $actual, $expected)
};

declare function local:functional-test-multiple-trades() 
{
    let $trades as element(trade)+ := (xml-factory:create-trade("1", "UBS"), xml-factory:create-trade("2", "HSBC")) 

    let $expected as element(tr)+ :=
                    (<tr>
                        <td name="ticket">1</td>
                        <td name="party">UBS</td>
                        <td name="industry"></td>
                    </tr>,
                    <tr>
                        <td name="ticket">2</td>
                        <td name="party">HSBC</td>
                        <td name="industry"></td>
                    </tr>)
                    
   let $actual as element(tr)* := report:gen-transaction-report($trades, ())   

   return xq:assert-equal("functional-test-multiple-trades()", $actual, $expected)
};

<results> 
{   
    local:functional-test(),
    local:functional-test-missing-industry-lookup(),
    local:functional-test-multiple-trades()
}
</results>