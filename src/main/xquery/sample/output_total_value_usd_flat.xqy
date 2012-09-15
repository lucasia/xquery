declare namespace testns = "http://lucasia.com/xquery/functions";

(: marklogic implementation :)
declare function daxns:get-all-trades() as element(trade)* {
    fn:doc()//trade
};

declare function testns:filter-by-currency($currency as xs:string, $trades as element(trade)*) as element(trade)* {
    for $trade in $trades 
        return if ($trade/@currency = $currency)
        then $trade
        else ()
};

declare function testns:total-value($trades as element(trade)*) as xs:double {
     fn:sum($trades/(@price * @shares)) (: just a simple example, should really group by currency :)
};

(: let's say we only have to report USD trades :)
let $usd := "USD"   
let $trades_usd := testns:filter-by-currency($usd, testns:get-all-trades()) 

return 
    <table>
        <th>Currency</th>
        <th>Amount</th>
        <tr>
            <td>{$usd}</td>
            <td>{testns:total-value($trades_usd)}</td>
        </tr>
    </table>