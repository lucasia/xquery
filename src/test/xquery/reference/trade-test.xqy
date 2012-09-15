import module namespace trade = "http://lucasia.com/xquery/reference/trade" at "../../../main/xquery/reference/trade.xqy";

import module namespace xq = "http://marklogic.com/xqunit" at "../lib/xqunit.xqy";

declare function local:functional-test() 
{

    let $tradeXML := <trade>
                        <ticket>1</ticket>
                        <party>UBS</party>
                    </trade>

    let $partyXML := <parties> 
                       <party>
                           <name>UBS</name>
                           <industry>Finance</industry>
                       </party>
                     </parties>
                    
   let $expected := <tr>
                        <td name="ticket">1</td>
                        <td name="party">UBS</td>
                        <td name="industry">Finance</td>
                    </tr>
                    
   let $actual := trade:transform($tradeXML, $partyXML/party)                 

   return xq:assert-equal("test", $actual, $expected)

};


<results>
{local:functional-test()}
</results>