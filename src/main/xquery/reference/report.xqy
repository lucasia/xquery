module namespace report = "http://lucasia.com/xquery/reference/report";

import module namespace conform = "http://lucasia.com/xquery/reference/conform" at "conform.xqy";
import module namespace ref-data = "http://lucasia.com/xquery/reference/ref-data" at "ref-data.xqy";
import module namespace format = "http://lucasia.com/xquery/reference/format" at "format.xqy";

declare function report:gen-transaction-report($trades as element(trade-raw)*, $parties as element(party-raw)*) 
as element(tr)*
{
    let $conformed as element(trade)* := conform:conform($trades, $parties)
    
    return format:tabular($conformed)
};


declare function report:gen-industry-volume-report($trades as element(trade-raw)*, $parties as element(party-raw)*) 
as element(form-3)*
{   
    <form-3>{
        let $conformed as element(trade)* := conform:conform($trades, $parties)
        
        for $industry in distinct-values($conformed/industry)
        let $conformedByIndustry := $conformed[industry = $industry]

        return <industry name="{$industry}">{fn:sum($conformedByIndustry/amount)}</industry>
    }
    </form-3>
};

