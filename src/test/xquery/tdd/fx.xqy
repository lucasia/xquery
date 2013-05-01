xquery version "1.0";

module namespace fx = "http://lucasia.com/fx";

declare function fx:find-fx-rate($fromCcy as xs:string, 
                                 $fx as element(fx-items))
as xs:double
{
    for $fxItem in $fx/fx-item
        where $fxItem/from-ccy eq $fromCcy
        return xs:double($fxItem/fx-rate)
};
