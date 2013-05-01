xquery version "1.0-ml";

import module namespace admin = "http://marklogic.com/xdmp/admin" at "/MarkLogic/admin.xqy";

let $stage := xs:integer(xdmp:get-request-field("stage", "1"))
let $config := admin:get-configuration()
let $defaultGroupId := admin:group-get-id($config, "Default")

let $appName := "obd-dev03"

let $appDbName := fn:concat($appName, "-db")
let $appForestName := fn:concat($appName, "-forest-001")
let $appXdbcServerName := fn:concat($appName, "-xdbc")
let $appXdbcServerPort := 8113
let $appHttpServerName := fn:concat($appName, "-http")
let $appHttpServerPort := 8123

let $appModulesDbName := fn:concat($appName,"-module-db")
let $appModulesForestName := fn:concat($appName,"-module-forest-001")

let $userDbName := fn:concat($appName, "-user-db")
let $userForestName := fn:concat($appName, "-user-forest-001")

let $sessionDbName := fn:concat($appName, "-session-db")
let $sessionForestName := fn:concat($appName,"-session-forest-001")

let $securityDbName := "Security" (: won't create this one :)
let $schemasDbName := "Schemas" (: won't create this one :)

return
	(: create forests, databases (including Users and Sessions) and app servers :)
	let $config := admin:forest-create($config, $appForestName, xdmp:host(), ())
	let $config := admin:forest-create($config, $appModulesForestName, xdmp:host(), ())	
    let $config := admin:forest-create($config, $userForestName, xdmp:host(), ())
	let $config := admin:forest-create($config, $sessionForestName, xdmp:host(), ())
	
	let $config := admin:database-create($config, $appDbName, xdmp:database($securityDbName), xdmp:database($schemasDbName))
	let $config := admin:database-create($config, $appModulesDbName, xdmp:database($securityDbName), xdmp:database($schemasDbName))	
	let $config := admin:database-create($config, $userDbName, xdmp:database($securityDbName), xdmp:database($schemasDbName))
	let $config := admin:database-create($config, $sessionDbName, xdmp:database($securityDbName), xdmp:database($schemasDbName))
	let $save := admin:save-configuration-without-restart($config)
	
	let $config := admin:database-attach-forest($config, xdmp:database($appDbName), xdmp:forest($appForestName))
	let $config := admin:database-attach-forest($config, xdmp:database($appModulesDbName), xdmp:forest($appModulesForestName))	
    let $config := admin:database-attach-forest($config, xdmp:database($userDbName), xdmp:forest($userForestName))
	let $config := admin:database-attach-forest($config, xdmp:database($sessionDbName), xdmp:forest($sessionForestName))
	let $save := admin:save-configuration-without-restart($config)

    (: create app servers :)
	let $config := admin:http-server-create($config, $defaultGroupId, $appHttpServerName, "/", $appXdbcServerPort, xdmp:database($appModulesDbName), xdmp:database($appDbName))
	let $config := admin:xdbc-server-create($config, $defaultGroupId, $appXdbcServerName, "/", $appHttpServerPort, xdmp:database($appModulesDbName), xdmp:database($appDbName))

	let $config := admin:appserver-set-collation($config, admin:appserver-get-id($config, $defaultGroupId, $appHttpServerName), "http://marklogic.com/collation/codepoint")
	let $config := admin:appserver-set-collation($config, admin:appserver-get-id($config, $defaultGroupId, $appXdbcServerName), "http://marklogic.com/collation/codepoint")

	let $save := admin:save-configuration($config)
	return ()