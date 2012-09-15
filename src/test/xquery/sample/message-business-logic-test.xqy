import module namespace blns = "http://lucasia.com/xquery/sample/business-logic" at "../../../main/xquery/sample/message-business-logic.xqy";

import module namespace daxns = "http://lucasia.com/xquery/data-acccess" at "message-data-access-file.xqy";

import module namespace xq = "http://marklogic.com/xqunit" at "../lib/xqunit.xqy";

declare function local:test-filter-by-currency() {

  let $trades := daxns:get-all-trades()
  
  let $usd := "USD"
  let $trades_usd := blns:filter-by-currency($usd, $trades) 
  
  let $expected  as element(trade)* := (
	  <trade ticket="1" ticker="ACME" shares="1" price="1.00" currency="USD" trade-date="2012-April-01"/>,
	  <trade ticket="3" ticker="JOE" shares="1" price="1.00" currency="USD" trade-date="2012-April-01"/>,
	  <trade ticket="4" ticker="BAR" shares="2" price="2.00" currency="USD" trade-date="2012-April-01"/>,
	  <trade ticket="5" ticker="BAR" shares="1" price="1.00" currency="USD" trade-date="2012-April-01"/>)

  return xq:assert-equal("test-filter-by-currency()", $trades_usd, $expected)

};

declare function local:test-total-value() {

  let $trades_usd  as element(trade)* := (
	  <trade ticket="1" ticker="ACME" shares="1" price="1.00" currency="USD" trade-date="2012-April-01"/>, 
	  <trade ticket="3" ticker="JOE" shares="1" price="1.00" currency="USD" trade-date="2012-April-01"/>,
	  <trade ticket="4" ticker="BAR" shares="2" price="2.00" currency="USD" trade-date="2012-April-01"/>,
	  <trade ticket="5" ticker="BAR" shares="1" price="1.00" currency="USD" trade-date="2012-April-01"/>)

 let $actual := blns:total-value($trades_usd)

 return xq:assert-equal("local:test-total-value()", $actual, 7) 

};

<results>
{local:test-filter-by-currency(), local:test-total-value()}
</results>