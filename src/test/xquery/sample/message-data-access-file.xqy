module namespace daxns = "http://lucasia.com/xquery/data-acccess";

(: impl will likely differ depending on XML repository vendor :)
declare function daxns:get-all-trades() as element(trade)* {
    fn:doc("messages/messages.xml")//trade
};