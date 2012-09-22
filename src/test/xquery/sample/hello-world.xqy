import module namespace msg = "http://lucasia.com/xquery/message" at "message.xqy";

declare function local:print($msg as xs:string)
as item()
{
	<result>{msg:message($msg)}</result>
};

local:print("hello world!")