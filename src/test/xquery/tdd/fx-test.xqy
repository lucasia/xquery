xquery version "1.0";

import module namespace xq = "http://marklogic.com/xqunit" at "xqunit.xqy";
import module namespace fx = "http://lucasia.com/fx" at "fx.xqy";

declare function local:test-find-fx-rate()
{
    let $fromCcy as xs:string := "USD"
    let $fxRate as xs:double := xs:double(0.62)    
    let $fx as element(fx-items) := 
                              <fx-items>
                                  <fx-item>
                                    <fx-rate>0</fx-rate>
                                  </fx-item>
                                  <fx-item>
                                    <from-ccy>{$fromCcy}</from-ccy>                                  
                                    <fx-rate>{$fxRate}</fx-rate>
                                  </fx-item>
                              </fx-items>

    let $foundFXRate as xs:double := fx:find-fx-rate($fromCcy, $fx)

    return
        xq:assert-equal("test find fx rate", $foundFXRate, $fxRate)
           
           
};

local:test-find-fx-rate()