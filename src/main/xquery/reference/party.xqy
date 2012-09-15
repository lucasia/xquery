module namespace party = "http://lucasia.com/xquery/reference/party";

declare function party:find-industry($party as xs:string, $partyXML as element(party)*) 
as xs:string?
{
   data($partyXML[name=$party]/industry)
};

