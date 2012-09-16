module namespace ref-data = "http://lucasia.com/xquery/reference/ref-data";

declare function ref-data:find-industry($party as xs:string, $partyXML as element(party)*) 
as xs:string?
{
   data($partyXML[name=$party]/industry)
};

