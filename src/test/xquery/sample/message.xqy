module namespace msg = "http://lucasia.com/xquery/message";

declare function msg:message($msg as xs:string)
as item()
{
		<foo>$msg</foo>
};
