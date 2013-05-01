xquery version "1.0";

module namespace xqunit = "http://marklogic.com/xqunit";

(: Copyright 2002-2009 Mark Logic Corporation.  All Rights Reserved. :)

(: Commonly-used functions for unit testing. :)

declare function xqunit:assert-equal($name, $actual as item()*, $expected as item()*) {
    if (fn:deep-equal($expected, $actual)) then 
        <pass test="{$name}"/>
    else 
        <fail test="{$name}">
             <expected>{$expected}</expected>
             <actual>{$actual}</actual>
        </fail>
};
