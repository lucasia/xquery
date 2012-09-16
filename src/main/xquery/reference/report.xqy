module namespace report = "http://lucasia.com/xquery/reference/report";

import module namespace conform = "http://lucasia.com/xquery/reference/conform" at "conform.xqy";
import module namespace ref-data = "http://lucasia.com/xquery/reference/ref-data" at "ref-data.xqy";
import module namespace format = "http://lucasia.com/xquery/reference/format" at "format.xqy";

declare function report:gen-transaction-report($trades as element(trade)*, $parties as element(party)*) 
as element(tr)*
{
    let $conformed := conform:conform($trades, $parties)
    
    return format:tabular($conformed)
};
