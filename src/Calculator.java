import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Calculator servlet
 * @author haoruiw
 */
//@WebServlet("/Calculator")
public class Calculator extends HttpServlet {
    private static final long serialVersionUID = 1;
    private static final String PATTERN = "";
    
//    public void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        doGet(request, response);
//    }
    
    public void doGet(HttpServletRequest request ,HttpServletResponse response) 
            throws IOException, ServletException {
        System.out.println("!!!!!!!!!");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String xValue = request.getParameter("xVal");
        String yValue = request.getParameter("yVal");
        String action = request.getParameter("action");

        
        out.println("<!doctype html>");
        out.println("<html>");
        out.println("    <head>");
        out.println("        <title>Calculator</title>");
        out.println("    </head>");
        out.println("    <body>");
        boolean validX = false;
        boolean validY = false;
        double x = 0;
		if (xValue != null) {
			try {
				xValue = sanitize(xValue);
				if (xValue.equals("")) {
					out.println("<p><span style=\"color:red\"> x is missing!</span></p>");
				} else {
					out.println("<p><span style=\"color:blue\"> xValue is: " + xValue + "</span></p>");
					x = Double.parseDouble(xValue);
					validX = true;
				}
			} catch (NumberFormatException e) {
				out.println("<p><span style=\"color:red\"> x is not a number!</span></p>");
			}
		} else if (xValue == null & action != null) {
			out.println("<p><span style=\"color:blue\"> xValue is: " + "NULL" + "</span></p>");
		}
        double y = 0;
        if (yValue != null) {
        	try {
        		yValue = sanitize(yValue);
        		if (yValue.equals("")) {
        			out.println("<p><span style=\"color:red\"> y is missing!</span></p>");
				} else if (action.equals("/") && yValue.equals("0")) {
					out.println("<p><span style=\"color:blue\">y cannot be 0 when dividing</span></p>");
				} else {
					out.println("<p><span style=\"color:blue\"> yValue is : " + yValue + "</span></p>");
					y = Double.parseDouble(yValue);
					validY = true;
        		}
			} catch (NumberFormatException e) {
				out.println("<p><span style=\"color:red\"> y is not a number!</span></p>");
			}
        } else if (xValue == null & action != null) {
			out.println("<p><span style=\"color:blue\"> yValue is: " + "NULL" + "</span></p>");
		}
        out.println("  <div>");
        out.println("    <form action=\"Calculator\" method=\"GET\">");
        out.println("       <table class=\"oneTable\">");
        out.println("          <tr>");
		if (action != null && validX && validY) {
			if ("+".equals(action)) {
				double sum = getSum(x, y);
				out.println("            <td colspan=\"2\" style=\"text-align:center;\">" + String.format(" %,.2f", x) + " + " + String.format(" %,.2f", y) + " = " + String.format(" %,.2f", sum) + "</td>");
			} else if ("-".equals(action)) {
				double diff = getDiff(x, y);
				out.println("            <td colspan=\"2\" style=\"text-align:center;\">" + String.format(" %,.2f", x) + " - " + String.format(" %,.2f", y) + " = " + String.format(" %,.2f", diff) + "</td>");
			} else if ("*".equals(action)) {
				double product = getProduct(x, y);
				out.println("            <td colspan=\"2\" style=\"text-align:center;\">" + String.format(" %,.2f", x) + " * " + String.format(" %,.2f", y) + " = " + String.format(" %,.2f", product) + "</td>");
			} else if ("/".equals(action)) {
				double divide = getDivision(x, y);
				out.println("            <td colspan=\"2\" style=\"text-align:center;\">" + String.format(" %,.2f", x) + " / " + String.format(" %,.2f", y) + " = " + String.format(" %,.2f", divide) + "</td>");
			} else {
				out.println("            <td colspan=\"2\" style=\"text-align:center;\">" + "Please have some legal operators" + "</td>");
			}
		}
		out.println("          </tr>");
		
		out.println("          <tr>");
        out.println("            <td>X: </td>");
        out.println("            <td>");
        if (xValue != null) {
        	out.println("              <input id=\"Xlabel\" type=\"text\" size=\"30\" name=\"xVal\" value=" + xValue +">");
        } else {
        	out.println("              <input id=\"Xlabel\" type=\"text\" size=\"30\" name=\"xVal\">");
        }
        out.println("            </td>");
        out.println("          </tr>");
        out.println("          <tr>");
        out.println("            <td>Y: </td>");
        out.println("            <td>");
        if (yValue != null) {
        	out.println("              <input id=\"ylabel\" type=\"text\" size=\"30\" name=\"yVal\" value=" + yValue +">");
        } else {
        	out.println("              <input id=\"ylabel\" type=\"text\" size=\"30\" name=\"yVal\">");
        }
        out.println("            </td>");
        out.println("          </tr>");
        out.println("          <tr>");
        out.println("            <td colspan=\"2\" style=\"text-align:center;\">");

        out.println("               <input id=\"plusButton\" type=\"submit\" name=\"action\" value=\"+\"/>");
        out.println("               <input id=\"minusButton\" type=\"submit\" name=\"action\" value=\"-\"/>");
        out.println("               <input id=\"timesButton\" type=\"submit\" name=\"action\" value=\"*\"/>");
        out.println("               <input id=\"divideButton\" type=\"submit\" name=\"action\" value=\"/\"/>");
        out.println("            </td>");
        out.println("          </tr>");
        out.println("        </table>");
        out.println("      </form>");
        out.println("    </div>");
        out.println("  </body>");
        out.println("</html>");
    }
    private String sanitize(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace(" ", "&nbsp;");
    }
    private double getSum(double x, double y) {
    	return x + y;
    }
    private double getDiff(double x, double y) {
    	return x - y;
    }
    private double getProduct(double x, double y) {
    	return x * y;
    }
    private double getDivision(double x, double y) {
    	return x / y;
    }
}
