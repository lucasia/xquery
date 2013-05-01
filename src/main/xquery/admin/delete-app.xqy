xquery version "1.0-ml";

import module namespace admin = "http://marklogic.com/xdmp/admin" at "/MarkLogic/admin.xqy";

declare function local:delete-appserver-if-exists($appServerName as xs:string, $config as element(configuration)) 
as element(configuration)
{
    let $defaultGroupId := admin:group-get-id($config, "Default")
    
    return 
        if (admin:appserver-exists($config, $defaultGroupId, $appServerName))
        then admin:appserver-delete($config, admin:appserver-get-id($config, $defaultGroupId, $appServerName))
        else $config    
};

(: TODO: this currently only works with 1 forest, line 22 actually returns multiple forests :) 
declare function local:detach-and-delete-forests-and-data($dbName as xs:string, $config as element(configuration)) 
as element(configuration)
{   
    if (admin:database-exists($config, $dbName))
    then 
        let $forestIds as xs:unsignedLong* := admin:database-get-attached-forests($config, xdmp:database($dbName))
    
        let $config as element(configuration) := admin:database-detach-forest($config, xdmp:database($dbName), $forestIds[1])

        let $save as xs:unsignedLong* := admin:save-configuration-without-restart($config)
        
        (: need to force the detach to be called first :) 
        (: TODO - keeps failing here, and i'm getting tired :)
        (: let $config as element(configuration) := admin:forest-delete($config, $forestIds[1], true()) :) (: true means delete data :) 
        
        return $config
    else
        $config
};

declare function local:delete-database-and-forests-if-exist($dbName as xs:string, $config as element(configuration)) 
as element(configuration)
{   
    if (admin:database-exists($config, $dbName))
    then 
        let $config as element(configuration) := local:detach-and-delete-forests-and-data($dbName, $config)
        
        let $config as element(configuration) := admin:database-delete($config, xdmp:database($dbName))
         
        return $config
    else
        $config
};

declare function local:delete-application($appName as xs:string) 
as empty-sequence()
{    
    let $appXdbcServerName as xs:string := fn:concat($appName, "-xdbc")
    let $appHttpServerName as xs:string := fn:concat($appName, "-http")
    
    let $config := admin:get-configuration()
    let $config as element(configuration) := local:delete-appserver-if-exists($appXdbcServerName, $config)
    let $config as element(configuration) := local:delete-appserver-if-exists($appHttpServerName, $config)
    
    let $save := admin:save-configuration-without-restart($config)
    
    let $config as element(configuration) := local:delete-database-and-forests-if-exist(concat($appName, "-user-db"), $config)
    let $config as element(configuration) := local:delete-database-and-forests-if-exist(concat($appName, "-session-db"), $config)
    let $config as element(configuration) := local:delete-database-and-forests-if-exist(fn:concat($appName, "-module-db"), $config)
    let $config as element(configuration) := local:delete-database-and-forests-if-exist(fn:concat($appName, "-db"), $config)
    
    let $save := admin:save-configuration($config)
    
    return $save
};

local:delete-application("obd-dev01")
	