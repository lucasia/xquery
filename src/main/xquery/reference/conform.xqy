module namespace conform = "http://lucasia.com/xquery/reference/conform";

import module namespace format = "http://lucasia.com/xquery/reference/format" at "format.xqy";
import module namespace ref-data = "http://lucasia.com/xquery/reference/ref-data" at "ref-data.xqy";

declare function conform:conform($trades as element(trade)*, $parties as element(party)*) 
as element(trade)*
{
    for $trade in $trades
    
    return <trade>{
            (conform:conform-economic-terms($trade), 
            conform:conform-industry($trade, $parties))
            }
           </trade>               
};


declare function conform:conform-economic-terms($trade as element(trade)) 
as element()*
{
    (
        <ticket>{data($trade/ticket)}</ticket>,
        <party>{data($trade/party)}</party>
    )                  
};

declare function conform:conform-industry($trade as element(trade), $parties as element(party)*) 
as element()
{
 let $industry as xs:string? := ref-data:find-industry(data($trade/party), $parties)
 
 return <industry>{$industry}</industry>                 
};