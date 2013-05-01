xquery version "1.0";

module namespace fx = "http://lucasia.com/fx";

(: just enough code to pass the test :)
declare function fx:apply-fx($fromCcy as xs:string, $fromAmount as xs:decimal, $fx as element(fx))
as xs:double
{
    $fromAmount * $fx/rate
};
