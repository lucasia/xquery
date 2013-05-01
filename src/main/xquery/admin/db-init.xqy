xquery version "1.0-ml";

import module namespace admin = "http://marklogic.com/xdmp/admin" at "/MarkLogic/admin.xqy";

(: creates a forest and database and then attaches the two 
   assumes the default Security and Schemas databases
:)
declare function local:create-forest-database-pair($name as xs:string, $dbName as xs:string, $config as element(configuration)) 
as element(configuration)
{
    let $forestName as xs:string := concat($name, "-forest-001")

    let $config as element(configuration) := admin:forest-create($config, $forestName, xdmp:host(), ())    
	let $config as element(configuration) := admin:database-create($config, $dbName, xdmp:database("Security"), xdmp:database("Schemas"))
    let $save as empty-sequence() := admin:save-configuration-without-restart($config)

    let $config as element(configuration) := admin:database-attach-forest($config, xdmp:database($dbName), xdmp:forest($forestName))
    let $save as empty-sequence() := admin:save-configuration-without-restart($config)
    
    return $config
};

declare function local:create-forest-database-pair($name as xs:string, $config as element(configuration)) 
as element(configuration)
{
    local:create-forest-database-pair($name, concat($name, "-db"), $config)    
};

let $appName as xs:string := "obd-dev07"
let $appXdbcServerPort as xs:int := 8117

let $appDbName as xs:string := concat($appName, "-db")
let $moduleName as xs:string := fn:concat($appName, "-module")
let $moduleDbName as xs:string := fn:concat($moduleName, "-db")

let $appXdbcServerName as xs:string := fn:concat($appName, "-xdbc")
let $appHttpServerName as xs:string := fn:concat($appName, "-http")
let $appHttpServerPort as xs:int := $appXdbcServerPort+100

let $config := admin:get-configuration()
let $defaultGroupId := admin:group-get-id($config, "Default")

return
	let $config as element(configuration) := local:create-forest-database-pair($appName, $appDbName, $config)
	let $config as element(configuration) := local:create-forest-database-pair($moduleName, $moduleDbName, $config)
	let $config as element(configuration) := local:create-forest-database-pair(concat($appName, "-user"), $config)
	let $config as element(configuration) := local:create-forest-database-pair(concat($appName, "-session"), $config)

	let $config as element(configuration) := admin:http-server-create($config, $defaultGroupId, $appHttpServerName, "/", $appXdbcServerPort, xdmp:database($moduleDbName), xdmp:database($appDbName))
	let $config as element(configuration) := admin:xdbc-server-create($config, $defaultGroupId, $appXdbcServerName, "/", $appHttpServerPort, xdmp:database($moduleDbName), xdmp:database($appDbName))

	let $config as element(configuration) := admin:appserver-set-collation($config, admin:appserver-get-id($config, $defaultGroupId, $appHttpServerName), "http://marklogic.com/collation/codepoint")
	let $config as element(configuration) := admin:appserver-set-collation($config, admin:appserver-get-id($config, $defaultGroupId, $appXdbcServerName), "http://marklogic.com/collation/codepoint")

	let $save as empty-sequence() := admin:save-configuration($config)
	return $save