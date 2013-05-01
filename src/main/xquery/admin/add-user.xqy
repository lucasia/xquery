xquery version "1.0-ml";

import module namespace admin = "http://marklogic.com/xdmp/admin" at "/MarkLogic/admin.xqy";

let $mmPublicUser := xdmp:eval('
		import module namespace sec = "http://marklogic.com/xdmp/security" at "/MarkLogic/security.xqy"
		sec:uid-for-name("mm-public")
	',(),  <options xmlns="xdmp:eval"><database>{ xdmp:security-database() }</database></options>)
let $config := admin:appserver-set-default-user($config, admin:appserver-get-id($config, $defaultGroupId, $appHttpServerName), $mmPublicUser)
let $save := admin:save-configuration($config)
return ()