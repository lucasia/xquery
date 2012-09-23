import module namespace functx = "http://www.functx.com" at "../../../main/xquery/util/functx-1.0.xqy";

declare function local:reverse($msg as xs:string)
as item()
{
	<result>{functx:reverse-string($msg)}</result>
};

local:reverse("backwards")