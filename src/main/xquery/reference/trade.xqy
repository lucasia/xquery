module namespace trade = "http://lucasia.com/xquery/reference/trade";

import module namespace format = "http://lucasia.com/xquery/reference/formatting" at "formatting.xqy";
import module namespace party = "http://lucasia.com/xquery/reference/party" at "party.xqy";

declare function trade:transform($trades as element(trade)*, $parties as element(party)*) 
as element(tr)*
{
    for $trade in $trades

        let $elements as element()* := (trade:create-selectivity-nodes($trade), 
                                        trade:create-industry-node($trade, $parties))
    
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

declare function trade:create-industry-node($tradeXML as element(trade), $partyXML as element(party)*) 
as element()
{

 let $industry as xs:string? := party:find-industry(data($tradeXML/party), $partyXML)
 
 return <industry>{party:find-industry($tradeXML/party, $partyXML)}</industry>                 
};