xquery version "1.0-ml";

import module namespace admin = "http://marklogic.com/xdmp/admin" at "/MarkLogic/admin.xqy";

let $stage := xs:integer(xdmp:get-request-field("stage", "1"))
let $config := admin:get-configuration()
let $defaultGroupId := admin:group-get-id($config, "Default")
return
then
	(: Remove the Docs server, Documents database and forest :)
	let $config := admin:appserver-delete($config, admin:appserver-get-id($config, $defaultGroupId, "Docs"))
	let $save := admin:save-configuration-without-restart($config)

	let $config := admin:database-delete($config, xdmp:database("Documents"))
	let $save := admin:save-configuration-without-restart($config)

	let $config := admin:forest-delete($config, admin:forest-get-id($config, "Documents"), true())
	let $save := admin:save-configuration-without-restart($config)
	return ()
