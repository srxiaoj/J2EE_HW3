<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>HW3 -- Error Page</title>
    </head>
    
	<body>
	
		<h2>Error</h2>
<%
		List<String> errors = (List<String>) request.getAttribute("errors");
		if (errors != null) {
			for (String error : errors) {
%>		
				<h3 style="color:red"> <%= error %> </h3>
<%
			}
		}
		
		if (session.getAttribute("user") == null) {
%>
			Click <a href="Login">here</a> to login.
<%
		} else {
%>
			Click <a href="FavoriteList">here</a> to return to the Favorite List.
<%
		}
%>	
	
	</body>
</html>