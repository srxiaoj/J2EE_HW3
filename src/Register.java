import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Register extends HttpServlet{
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;
    private static int count = 1;
    public void init() throws ServletException {
        String jdbcDriverName = getInitParameter("jdbcDriver");
        String jdbcURL = getInitParameter("jdbcURL");

        try {
            userDAO = new UserDAO(jdbcDriverName, jdbcURL, "user");
        } catch (MyDAOException e) {
            throw new ServletException(e);
        }
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String registration = request.getParameter("registration");
        RegisterForm registerForm = new RegisterForm(request);
        String action = registerForm.getButton();
        System.out.println("registrations is: " + registration);
        if (action != null) {
            if (action.equals("Login")) {
                System.out.println("rediect to login");
                response.sendRedirect("");
                return;
            }
        }
        register(request, response);
    }
    private void register (HttpServletRequest request, HttpServletResponse response) 
                throws ServletException, IOException {
            List<String> errors = new ArrayList<>();
            RegisterForm registerForm = new RegisterForm(request);
            
            if (!registerForm.isPresent()) {
                outputRegisterPage(response, registerForm, null);
                return;
            }
//            if (!registerForm.isPresent()) {
            errors.addAll(registerForm.getValidationErrors());
            if (errors.size() != 0) {
                outputRegisterPage(response, registerForm, errors);
                return;
            }
//            }
            try {
                User user;
                
                if (registerForm.getButton().equals("Register")) {
                    System.out.println("click on register");
                    if (userDAO.readInLoginForm(registerForm.getEmail()) != null) {
                        errors.add("Email already used");
                        outputRegisterPage(response, registerForm, errors);
                        return;
                    }
    //                user = userDAO.readInRegisterForm(registerForm.getEmail());
                    user = new User();
                    user.setEmail(registerForm.getEmail());
                    user.setFirstName(registerForm.getFirstName());
                    user.setLastName(registerForm.getLastName());
                    user.setPassword(registerForm.getPassword());
                    userDAO.create(user);
                } else {
                    errors.addAll(registerForm.getValidationErrors());
                    if (errors.size() != 0) {
                        outputRegisterPage(response, registerForm, errors);
                        return;
                    }
    //                user = userDAO.readInLoginForm(registerForm.getEmail());
    //                if (user == null) {
    //                    errors.add("No such user");
    //                    outputRegisterPage(response, registerForm, errors);
    //                    return;
    //                }
                }
                user = userDAO.readInLoginForm(registerForm.getEmail());
                HttpSession session = request.getSession();
                session.setAttribute("email", user);
//                manageList(request, response);
                response.sendRedirect("");
            } catch (MyDAOException e) {
                errors.add(e.getMessage());
                outputRegisterPage(response, registerForm, errors);
            }
        }

    // Methods that generate & output HTML
    private void generateHead(PrintWriter out) {
        out.println("  <head>");
        out.println("    <meta charset=\"utf-8\"/>");
        out.println("    <title>Register</title>");
        out.println("  </head>");
    }

    private void outputRegisterPage(HttpServletResponse response, RegisterForm form,
            List<String> errors) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
    
        out.println("<!DOCTYPE html>");
        out.println("<html>");
    
        generateHead(out);
    
        out.println("<body>");
        out.println("<h2>Register</h2>");
    
        if (errors != null && errors.size() > 0) {
            for (String error : errors) {
                out.println("<p style=\"font-size: large; color: red\">");
                out.println(error);
                out.println("</p>");
            }
        }
    
        // Generate an HTML <form> to get data from the user
        out.println("<form method=\"POST\">");
        out.println("    <table>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">Email Address:</td>");
        out.println("            <td>");
        out.println("                <input type=\"text\" name=\"email\"");
        if (form != null && form.getEmail() != null) {
            out.println("                    value=\"" + form.getEmail()
                    + "\"");
        }
        out.println("                />");
        out.println("            </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">First Name:</td>");
        out.println("            <td>");
        out.println("                <input type=\"text\" name=\"firstName\"");
        if (form != null && form.getFirstName() != null) {
            out.println("                    value=\"" + form.getFirstName()
                    + "\"");
        }
        out.println("                />");
        out.println("            </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">Last Name:</td>");
        out.println("            <td>");
        out.println("                <input type=\"text\" name=\"lastName\"");
        if (form != null && form.getLastName() != null) {
            out.println("                    value=\"" + form.getLastName()
                    + "\"");
        }
        out.println("                />");
        out.println("            </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">Password:</td>");
        out.println("            <td><input type=\"password\" name=\"password\" /></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td colspan=\"2\" style=\"text-align: center;\">");
        out.println("                <input type=\"submit\" name=\"button\" value=\"Login\" />");
        out.println("                <input type=\"submit\" name=\"button\" value=\"Register\" />");
        out.println("                <input type=\"hidden\" name=\"registration\" value=\"true\"/>");
        out.println("            </td>");
        out.println("        </tr>");
        out.println("    </table>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
        
    }
}
