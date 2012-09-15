declare namespace testns = "http://lucasia.com/xquery/functions";

import module namespace daxns = "http://lucasia.com/xquery/data-acccess" at "../data-access/message-data-access-marklogic.xqy";

import module namespace blns = "http://lucasia.com/xquery/functions/business-logic" at "message-business-logic.xqy";

let $trades := daxns:get-all-trades()

(: let's say we only have to report USD trades :)
let $usd := "USD"   
let $trades_usd := blns:filter-by-currency($usd, $trades) 

return 
    <table>
        <th>Currency</th>
        <th>Amount</th>
        <tr>
            <td>{$usd}</td>
            <td>{blns:total-value($trades_usd)}</td>
        </tr>
    </table>