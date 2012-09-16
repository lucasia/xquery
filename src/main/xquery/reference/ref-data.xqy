module namespace ref-data = "http://lucasia.com/xquery/reference/ref-data";

declare function ref-data:find-industry($party as xs:string, $parties as element(party-raw)*) 
as xs:string?
{
   data($parties[name=$party]/industry)
};

