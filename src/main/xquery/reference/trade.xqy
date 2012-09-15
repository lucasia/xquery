module namespace trade = "http://lucasia.com/xquery/reference/trade";

import module namespace format = "http://lucasia.com/xquery/reference/formatting" at "formatting.xqy";
import module namespace party = "http://lucasia.com/xquery/reference/party" at "party.xqy";

declare function trade:transform($tradeXML as element(trade), $partyXML as element(party)*) 
as element(tr)
{

    let $industry := party:find-industry(data($tradeXML/party), $partyXML)
    
    let $elements as element()* := (trade:create-selectivity-nodes($tradeXML), party:create-industry-node($industry))
    
    return format:tabular-row($elements)
                    
};


declare function trade:create-selectivity-nodes($tradeXML as element(trade)) 
as element()*
{
    (
        <ticket>{data($tradeXML/ticket)}</ticket>,
        <party>{data($tradeXML/party)}</party>
    )
                    
};