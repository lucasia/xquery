module namespace xml-factory = "http://lucasia.com/xquery/reference/test/xml-factory";

declare function xml-factory:create-trade($ticket as xs:string, $partyName as xs:string)
as element(trade)
{
    <trade>
        <ticket>{$ticket}</ticket>
        <party>{$partyName}</party>
    </trade>
};

declare function xml-factory:create-party($partyName as xs:string, $industryName as xs:string)
as element(party)
{
   <party>
       <name>{$partyName}</name>
       <industry>{$industryName}</industry>
   </party>
};
