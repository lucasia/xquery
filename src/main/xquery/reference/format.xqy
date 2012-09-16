module namespace format = "http://lucasia.com/xquery/reference/format";

(: flattens all children under the element :)
declare function format:tabular($elements as element()*) 
as element(tr)*
{
    for $element in $elements
        return format:tabular-row($element/*)           
};

declare function format:tabular-row($elements as element()*) 
as element(tr)
{
    <tr>{
        for $element in $elements
        return <td name="{fn:name($element)}">{fn:data($element)}</td>
        }
   </tr>              
};
