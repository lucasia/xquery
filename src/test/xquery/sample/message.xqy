module namespace msg = "http://lucasia.com/xquery/sample/business-logic";

declare function local:message($msg as xs:string)
as item()
{
		$msg
};

local:message("hello-world")