<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>HW3 -- Register Page</title>
    </head>
    
	<body>
	
		<h2>HW3 Login</h2>
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
		<form action="Register" method="POST">
		    <table>
		        <tr>
            <td style="font-size: x-large">Email Address:</td>
            <td>
                <input type="text" name="email"
                />
            </td>
        </tr>
        <tr>
            <td style="font-size: x-large">First Name:</td>
            <td>
                <input type="text" name="firstName"
                />
            </td>
        </tr>
        <tr>
            <td style="font-size: x-large">Last Name:</td>
            <td>
                <input type="text" name="lastName"
                />
            </td>
        </tr>
        <tr>
            <td style="font-size: x-large">Password:</td>
            <td><input type="password" name="password" /></td>
        </tr>
        <tr>
            <td colspan="2" style="text-align: center;">
                <input type="submit" name="action" value="Login" />
                <input type="submit" name="action" value="Register" />
                <input type="hidden" name="registration" value="true"/>
            </td>
        </tr>
			</table>
		</form>
	</body>
</html>