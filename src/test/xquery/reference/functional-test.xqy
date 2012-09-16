import module namespace report = "http://lucasia.com/xquery/reference/report" at "../../../main/xquery/reference/report.xqy";

import module namespace xml-factory = "http://lucasia.com/xquery/reference/test/xml-factory" at "xml-factory.xqy";

import module namespace xq = "http://marklogic.com/xqunit" at "../lib/xqunit.xqy";

declare function local:test-gen-transaction-report()
{
    let $ticket as xs:string := "1"
    let $partyName as xs:string := "UBS"
    let $industryName as xs:string := "Finance"    

    let $party as element(party-raw) := xml-factory:create-party($partyName, $industryName)
    
    let $trade1 as element(trade-raw) := xml-factory:create-trade($ticket, $partyName, "USD", 2000.15)
    let $trade2 as element(trade-raw) := xml-factory:create-trade("2", "HSBC", "JPY", 1000)

    let $expected as element(tr)+ :=
                    (<tr>
                        <td name="ticket">1</td>
                        <td name="party">UBS</td>
                        <td name="currency">USD</td>
                        <td name="amount">2000.15</td>           
                        <td name="industry">Finance</td>
                    </tr>,
                    <tr>
                        <td name="ticket">2</td>
                        <td name="party">HSBC</td>
                        <td name="currency">JPY</td>
                        <td name="amount">1000</td>   
                        <td name="industry"/>
                    </tr>)
                    
   let $actual as element(tr)* := report:gen-transaction-report(($trade1, $trade2), $party)   

   return xq:assert-equal("test-gen-transaction-report()", $actual, $expected)
};

declare function local:test-gen-industry-volume-report()
{
    let $ticket as xs:string := "1"
    let $partyName as xs:string := "UBS"
    let $industryName as xs:string := "Finance"    

    let $party as element(party-raw) := xml-factory:create-party($partyName, $industryName)
    
    let $trade1 as element(trade-raw) := xml-factory:create-trade($ticket, $partyName, "USD", 2000.15)
    let $trade2 as element(trade-raw) := xml-factory:create-trade("2", "HSBC", "JPY", 1000)
     
    let $expected as element(form-3) := <form-3>
                                            <industry name="Finance">2000.15</industry>
                                            <industry name="">1000</industry>
                                        </form-3>
                    
   let $actual as element(form-3)* := report:gen-industry-volume-report(($trade1, $trade2), $party)                 

   return xq:assert-equal("test-gen-industry-volume-report()", $actual, $expected)
};


<results> 
{   
    local:test-gen-transaction-report(),
    local:test-gen-industry-volume-report()
 }
</results>