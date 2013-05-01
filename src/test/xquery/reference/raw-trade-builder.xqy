declare function local:base()  
as element(scotch)
{
    <scotch>
        <brand></brand>
        <mode></mode>
    </scotch>
};

declare function local:with-brand($brand as xs:string, $scotch as element(scotch)) 
as element(scotch)
{
    <scotch>
        {local:remove-elements-deep($scotch/*, "brand")}
        <brand>{$brand}</brand>
    </scotch>
};

declare function local:with-mode($preparation as xs:string, $scotch as element(scotch))
as element(scotch)
{
    <scotch>
        {local:remove-elements-deep($scotch/*, "mode")}
        <mode>{$preparation}</mode>
    </scotch>
};

(:~
 : Removes descendant elements from an XML node, based on name 
 :
 : @author  Priscilla Walmsley, Datypic 
 : @version 1.0 
 : @see     http://www.xqueryfunctions.com/xq/functx_remove-elements-deep.html 
 : @param   $nodes root(s) to start from 
 : @param   $names the names of the elements to remove 
 :) 
declare function local:remove-elements-deep 
  ( $nodes as node()* ,
    $names as xs:string* )  as node()* {
       
   for $node in $nodes
   return
     if ($node instance of element())
     then if (name($node) = $names)
          then ()
          else element { node-name($node)}
                { $node/@*,
                  local:remove-elements-deep($node/node(), $names)}
     else if ($node instance of document-node())
     then local:remove-elements-deep($node/node(), $names)
     else $node
 } ;

let $base as element(scotch) := local:base()

let $brand as element(scotch) := local:with-brand("Dewers", $base)

let $mode as element(scotch) := local:with-mode("OnTheRocks", $brand)

(: return $mode :)

return local:with-mode("OnTheRocks", 
                                    local:with-brand("Dewers", 
                                                                local:base()))
                                                                
                                                                




