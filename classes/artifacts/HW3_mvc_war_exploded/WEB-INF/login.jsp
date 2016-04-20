<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>HW3 -- Login Page</title>
    </head>
    
	<body>
	
		<h2>Login</h2>
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
		<form action="login.do" method="POST">
		    <table>
		        <tr>
		            <td style="font-size: x-large">User Name:</td>
		            <td>
		                <input type="text" name="email" value="${form.email}" />
		            </td>
		        </tr>
		        <tr>
		            <td style="font-size: x-large">Password:</td>
		            <td><input type="password" name="password" /></td>
		        </tr>
		        <tr>
		            <td colspan="2" style="text-align: center;">
		                <input type="submit" name="action" value="Login" />
		            </td>
		        </tr>
		        <tr>
        			<td colspan = "2" style="text-align: center;">
        				<a href="register.do">
        					<span style="font-size: x-large">Go To Register</span>
        				</a>
        			</td>
        		</tr>
			</table>
		</form>
	</body>
</html>