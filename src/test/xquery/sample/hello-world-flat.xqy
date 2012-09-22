declare namespace err = "http://www.w3.org/2005/xqt-errors";
declare namespace msg = "http://lucasia.com/xquery/message";
declare namespace local = "http://www.w3.org/2005/xquery-local-functions";
declare namespace saxon = "http://saxon.sf.net/";
declare namespace xsi = "http://www.w3.org/2001/XMLSchema-instance";
declare namespace fn = "http://www.w3.org/2005/xpath-functions";
declare namespace xs = "http://www.w3.org/2001/XMLSchema";

declare function msg:message($msg as xs:string)
as item()
{
	<foo>{$msg}</foo>
};
declare function local:print($msg as xs:string)
as item()
{
	<result>{msg:message($msg)}</result>
};

local:print("hello world!")
