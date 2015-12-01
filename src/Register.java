/**
 * @author Haorui Wu
 * @date 11/23/2015
 * @courseNumber: 08672
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

public class Register extends HttpServlet{
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;
    private FormBeanFactory<RegisterForm> registerFormFactory = FormBeanFactory.getInstance(RegisterForm.class);
    public void init() throws ServletException {
        String jdbcDriverName = getInitParameter("jdbcDriver");
        String jdbcURL = getInitParameter("jdbcURL");

        try {
            ConnectionPool cp = new ConnectionPool(jdbcDriverName, jdbcURL);
            userDAO = new UserDAO(cp, "haoruiw_user");
        } catch (DAOException e) {
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
//        String registration = request.getParameter("registration");
        List<String> errors = new ArrayList<String>();
        RegisterForm form = null;
        try {
            form = registerFormFactory.create(request);
            String action = form.getButton();
            // System.out.println("registrations is: " + registration);
            if (action != null) {
                if (action.equals("Login")) {
                    // System.out.println("rediect to login");
                    response.sendRedirect("");
                    return;
                }
            }

            request.setAttribute("errors", errors);
            if (!form.isPresent()) {
                RequestDispatcher d = request.getRequestDispatcher("register.jsp");
                d.forward(request, response);
                return;
            }
            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                RequestDispatcher d = request.getRequestDispatcher("register.jsp");
                d.forward(request, response);
                return;
            }
            UserBean user = new UserBean();

            if (form.getButton().equals("Register")) {
                // System.out.println("click on register");
                user = userDAO.getUser(form.getEmail());
                if (user != null) {
                    errors.add("Email already used");
                    RequestDispatcher d = request.getRequestDispatcher("register.jsp");
                    d.forward(request, response);
                    return;
                }
                user = new UserBean();
                user.setEmail(form.getEmail());
                user.setFirstName(form.getFirstName());
                user.setLastName(form.getLastName());
                user.setPassword(form.getPassword());
                // System.out.println("saving user bean in register page");
                // create user bean with primaryKey and override the original
                // user bean
                userDAO.createAutoIncrement(user);
            } else {
                errors.addAll(form.getValidationErrors());
                if (errors.size() != 0) {
                    RequestDispatcher d = request.getRequestDispatcher("register.jsp");
                    d.forward(request, response);
                    return;
                }
            }
            session.setAttribute("email", user);
            // System.out.println("In register page: ");
            // System.out.println("userId is: " + user.getUserId());
            response.sendRedirect("");
            // System.out.println("redirect to favorite list page");
        } catch (RollbackException e) {
            errors.add(e.getMessage());
            RequestDispatcher d = request.getRequestDispatcher("error.jsp");
            d.forward(request, response);
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            RequestDispatcher d = request.getRequestDispatcher("error.jsp");
            d.forward(request, response);
        }
    }
}
