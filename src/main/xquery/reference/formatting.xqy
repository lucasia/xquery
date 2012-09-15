module namespace format = "http://lucasia.com/xquery/reference/formatting";

declare function format:tabular-row($elements as element()*) 
as element(tr)
{
    <tr>{
        for $element in $elements
        return <td name="{fn:name($element)}">{fn:data($element)}</td>
        }
   </tr>
               
};
