module namespace xml-factory = "http://lucasia.com/xquery/reference/test/xml-factory";

declare function xml-factory:create-trade($ticket as xs:string, $partyName as xs:string)
as element(trade-raw)
{
    xml-factory:create-trade($ticket, $partyName, "USD", 1000)    
};

declare function xml-factory:create-trade($ticket as xs:string, $partyName as xs:string, 
                                          $currency as xs:string, $amount as xs:double)
as element(trade-raw)
{
    <trade-raw>
        <ticket>{$ticket}</ticket>
        <party>{$partyName}</party>
        <currency>{$currency}</currency>
        <amount>{$amount}</amount>
    </trade-raw>
};

declare function xml-factory:create-party($partyName as xs:string, $industryName as xs:string)
as element(party-raw)
{
   <party-raw>
       <name>{$partyName}</name>
       <industry>{$industryName}</industry>
   </party-raw>
};
