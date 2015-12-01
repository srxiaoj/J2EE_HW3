<%@page import="java.util.List"%>
<%@page import="FavoriteBean"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>ToDoList6 </title>
    </head>
    
    <body>
    
        <h2>ToDoList6 Example</h2>
<%
		List<String> errors = (List<String>) request.getAttribute("errors");
		if (errors != null) {
			for (String error : errors) {
%>		
				<h3 style="color:red"> <%= error %> </h3>
<%
			}
		}
%>	

        <form action="ToDoList" method="POST">
            <table>
                <tr> <td colspan="3"> <hr /> </td> </tr>
                <tr>
                    <td style="font-size: large">
                        Item to Add:
                    </td>
                    <td colspan="2">
                        <input id="item" type="text" size="40" name="item"/>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input type="submit" name="action" value="Add to Top" />
                    </td>
                    <td>
                        <input type="submit" name="action" value="Add to Bottom" />
                    </td>
                </tr>
                <tr> <td colspan="3"> <hr /> </td> </tr>
            </table>
        </form>
        
<%
FavoriteBean[] items = (FavoriteBean[]) request.getAttribute("items");
%>
		<p style="font-size: x-large">The list now has <%= items.length %> items.</p>

		<table>
<%
			for (int i = 0; i < items.length; i++) {
			    FavoriteBean item = items[i];
%>        
           		<tr>
       				<td>
			            <form action="Delete" method="POST">
                			<input type="hidden" name="id" value="<%= item.getId() %>" />
                			<input type="submit" name="button" value="X" />
           				</form>
        			</td>
        			<td style="font-size: x-large"> &nbsp; <%= i + 1 %>. </td>
        			<td>
        				<span style="font-size: x-large">
        					<%= item.read().replace("<", "&lt;").replace(">","&gt;").replace("\"","&quot;") %>
        				</span>
                        (favoriteId = <%= item.getFavoriteId() %>)
                    </td>
   				</tr>
<%
       		}
%>
		</table>

       	Click <a href="Logout">here</a> to log out.
    </body>
</html>