module namespace blns = "http://lucasia.com/xquery/sample/business-logic";

declare function blns:filter-by-currency($currency as xs:string, $trades as element(trade)*) as element(trade)* {
    for $trade in $trades 
        return if ($trade/@currency = $currency)
        then $trade
        else ()
};

declare function blns:total-value($trades as element(trade)*) as xs:double {
	fn:sum(
		for $trade in $trades 
			return ($trade/(@shares * @price))
	)
};

