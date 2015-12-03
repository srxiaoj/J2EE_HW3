<%@page import="java.util.List"%>
<%@page import="edu.cmu.cs.webapp.todolist7.databean.FavoriteBean"%>
<%@page import="edu.cmu.cs.webapp.todolist7.databean.UserBean"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>HW3 </title>
    </head>
    
    <body>
	<%
	    UserBean user = (UserBean) request.getSession().getAttribute("user");
	%>
	<h2>Favorite list for <%= user.getFirstName() %> <%= user.getLastName() %></h2>
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

        <form action="add.do" method="POST">
		<table>
			<tr>
				<td colspan="3"><hr /></td>
			</tr>
			<tr>
				<td style="font-size: large">URL:</td>
				<td colspan="2"><input type="text" size="40" name="url" /></td>
			</tr>
			<tr>
				<td style="font-size: large">Comment:</td>
				<td colspan="2"><input type="text" size="40" name="comment" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" name="action" value="Add Favorite" /></td>
				<td><input type="submit" name="action" value="Log out" /></td>
			</tr>
			<tr>
				<td colspan="3"><hr /></td>
			</tr>
		</table>
	</form>
<%
	FavoriteBean[] favorites = (FavoriteBean[]) request.getAttribute("favorites");
%>
		<p style="font-size: x-large">The list now has <%= favorites.length %> items.</p>

		<table>
<%
			for (int i = 0; i < favorites.length; i++) {
			    FavoriteBean favorite = favorites[i];
%>
			<tr>
				<td></td>
				<td><span style="font-size: x-large"><%= i + 1 %>.</span></td>
				<td><a href="?favoriteId=<%= favorite.getFavoriteId() %>"> <span
						style="font-size: x-large">
						<%= favorite.getUrl().replace("<", "&lt;").replace(">","&gt;").replace("\"","&quot;") %></span>
				</a></td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td><span style="font-size: x-large">
				<%= favorite.getComment().replace("<", "&lt;").replace(">","&gt;").replace("\"","&quot;") %></span></td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td><span style="font-size: x-large"><%=favorite.getClickCount() %> Clicks </span></td>
			</tr>
<%
       		}
%>
		</table>
    </body>
</html>