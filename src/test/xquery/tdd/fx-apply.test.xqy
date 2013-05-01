xquery version "1.0";

import module namespace xq = "http://marklogic.com/xqunit" at "xqunit.xqy";
import module namespace fx = "http://lucasia.com/fx" at "fx-apply.xqy";

declare function local:test-apply-fx()
{
    (: set up :)
    let $fromCcy as xs:string := "USD"
    let $fromAmount as xs:decimal := 10
    let $fxRate as xs:double := xs:double(0.62)
    
    let $fx as element(fx) :=  <fx>
                                   <from-ccy>{$fromCcy}</from-ccy>
                                   <rate>{$fxRate}</rate>
                               </fx>                                         

    (: execute :)
    let $toAmount as xs:double := fx:apply-fx($fromCcy, $fromAmount, $fx)

    (: assertions :)
    return 
        xq:assert-equal("test apply fx", $toAmount, ($fromAmount * $fxRate))
   
    (: tear-down not required - no side effects ! :)
};

local:test-apply-fx()