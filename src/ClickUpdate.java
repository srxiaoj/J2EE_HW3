import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ClickUpdate extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private FavoriteDAO favoriteDAO;
    private UserDAO userDAO;
    private HW3 hw3;

    public void init() throws ServletException {
        String jdbcDriverName = getInitParameter("jdbcDriver");
        String jdbcURL = getInitParameter("jdbcURL");

        try {
            userDAO = new UserDAO(jdbcDriverName, jdbcURL, "user");
            favoriteDAO = new FavoriteDAO(jdbcDriverName, jdbcURL, "fav");
        } catch (MyDAOException e) {
            throw new ServletException(e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("!!!!!!!!!!");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String favoriteIdStr = request.getParameter("favoriteId");
        int favoriteId;
        if (favoriteIdStr != null) {
            favoriteId = Integer.parseInt(favoriteIdStr);
            try {
                FavoriteBean bean = favoriteDAO.getFavorite(favoriteId);
                favoriteDAO.incrementClick(bean.getClickCount() + 1, favoriteId);
//                int userId = bean.getUserId();
//                User user = (User) userDAO.readInFavoriteForm(userId);
//                HttpSession session = request.getSession();
//                session.setAttribute("email", user);
//                hw3.outputFavorite(response, request, null);
//                manageList(request, response);
            } catch (MyDAOException e) {
                throw new ServletException(e);
            }
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        RegisterForm registerForm = new RegisterForm(request);

        if (!registerForm.isPresent()) {
            outputRegisterPage(response, registerForm, null);
            return;
        }
        if (!registerForm.isFirstTimeVisit(request)) {
            errors.addAll(registerForm.getValidationErrors());
            if (errors.size() != 0) {
                outputRegisterPage(response, registerForm, errors);
                return;
            }
        }
        try {
            User user;

            if (registerForm.getButton().equals("Register")) {
                if (userDAO.readInLoginForm(registerForm.getEmail()) != null) {
                    errors.add("Email already used");
                    outputRegisterPage(response, registerForm, errors);
                    return;
                }
                // user = userDAO.readInRegisterForm(registerForm.getEmail());
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
                // user = userDAO.readInLoginForm(registerForm.getEmail());
                // if (user == null) {
                // errors.add("No such user");
                // outputRegisterPage(response, registerForm, errors);
                // return;
                // }
            }
            user = userDAO.readInLoginForm(registerForm.getEmail());
            HttpSession session = request.getSession();
            session.setAttribute("email", user);
            manageList(request, response);
        } catch (MyDAOException e) {
            errors.add(e.getMessage());
            outputRegisterPage(response, registerForm, errors);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> errors = new ArrayList<String>();

        LoginForm loginForm = new LoginForm(request);
        // RegisterForm registerForm = new RegisterForm(request);

        if (!loginForm.isPresent()) {
            outputLoginPage(response, loginForm, null);
            return;
        }
        if (loginForm.getButton().equals("Register")) {
            // outputRegisterPage(response, registerForm, null);
            register(request, response);
            return;
        }
        errors.addAll(loginForm.getValidationErrors());
        if (errors.size() != 0) {
            outputLoginPage(response, loginForm, errors);
            return;
        }
        try {
            User user;

            if (loginForm.getButton().equals("Login")) {
                errors.addAll(loginForm.getValidationErrors());
                if (errors.size() != 0) {
                    outputLoginPage(response, loginForm, errors);
                    return;
                }
                user = userDAO.readInLoginForm(loginForm.getEmail());
                if (user == null) {
                    errors.add("No such user");
                    outputLoginPage(response, loginForm, errors);
                    return;
                }

                if (!loginForm.getPassword().equals(user.getPassword())) {
                    errors.add("Incorrect password");
                    outputLoginPage(response, loginForm, errors);
                    return;
                }
                HttpSession session = request.getSession();
                session.setAttribute("email", user);
                manageList(request, response);
            }

        } catch (MyDAOException e) {
            errors.add(e.getMessage());
            outputLoginPage(response, loginForm, errors);
        }
    }

    private void manageList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Look at the action parameter to see what we're doing to the list
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if (action == null) {
            // No change to list requested
            outputToDoList(response, request);
            return;
        }

        if (action.equals("Log out")) {
            login(request, response);
            session.setAttribute("email", null); // remove session also
            return;
        }

        if (action.equals("Add Favorite")) {
            processAdd(request, response, true);
            return;
        }
        outputToDoList(response, request, "No such operation: " + action);
    }

    private void processAdd(HttpServletRequest request, HttpServletResponse response, boolean addToFavorite)
            throws ServletException, IOException {
        List<String> errors = new ArrayList<String>();

        FavoriteForm form = new FavoriteForm(request);

        errors.addAll(form.getValidationErrors());
        if (errors.size() > 0) {
            outputFavorite(response, request, errors);
            return;
        }

        try {
            FavoriteBean bean = new FavoriteBean();
            bean.setURL(form.getURL());
            bean.setComment(request.getParameter("comment"));
            // bean.setClickCount(request.getParameter("clickCount") + 1);
            User u = (User) request.getSession().getAttribute("email");
            bean.setUserId(u.getUserId());
            if (addToFavorite) {
                favoriteDAO.addToBottom(bean);
            }
            outputToDoList(response, request, "Favorite Added");
        } catch (MyDAOException e) {
            errors.add(e.getMessage());
            outputFavorite(response, request, errors);
        }
    }

    // Methods that generate & output HTML
    private void generateHead(PrintWriter out) {
        out.println("  <head>");
        out.println("    <meta charset=\"utf-8\"/>");
        out.println("    <title>Login</title>");
        out.println("  </head>");
    }

    private void outputLoginPage(HttpServletResponse response, LoginForm form, List<String> errors) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");

        generateHead(out);

        out.println("<body>");
        out.println("<h2>Login</h2>");

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
            out.println("                    value=\"" + form.getEmail() + "\"");
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
        out.println("                <input type=\"hidden\" name=\"First Time Visit\"/>");
        out.println("            </td>");
        out.println("        </tr>");
        out.println("    </table>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }

    private void outputRegisterPage(HttpServletResponse response, RegisterForm form, List<String> errors)
            throws IOException {
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
            out.println("                    value=\"" + form.getEmail() + "\"");
        }
        out.println("                />");
        out.println("            </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">First Name:</td>");
        out.println("            <td>");
        out.println("                <input type=\"text\" name=\"firstName\"");
        if (form != null && form.getFirstName() != null) {
            out.println("                    value=\"" + form.getFirstName() + "\"");
        }
        out.println("                />");
        out.println("            </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">Last Name:</td>");
        out.println("            <td>");
        out.println("                <input type=\"text\" name=\"lastName\"");
        if (form != null && form.getLastName() != null) {
            out.println("                    value=\"" + form.getLastName() + "\"");
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
        out.println("                <input type=\"hidden\" name=\"First Time Visit\"/>");
        out.println("            </td>");
        out.println("        </tr>");
        out.println("    </table>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }

    private void outputToDoList(HttpServletResponse response, HttpServletRequest request) throws IOException {
        // Just call the version that takes a List passing an empty List
        List<String> list = new ArrayList<String>();
        outputFavorite(response, request, list);
    }

    private void outputToDoList(HttpServletResponse response, HttpServletRequest request, String message)
            throws IOException {
        // Just put the message into a List and call the version that takes a
        // List
        List<String> list = new ArrayList<String>();
        list.add(message);
        outputFavorite(response, request, list);
    }

    private void outputFavorite(HttpServletResponse response, HttpServletRequest request, List<String> messages)
            throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("email");

        // Get the list of favorites to display at the end
        FavoriteBean[] beans;
        try {
            beans = favoriteDAO.getFavoriteLists(user.getUserId());
            Arrays.sort(beans, (FavoriteBean i1, FavoriteBean i2) -> i1.getPosition() - i2.getPosition());

        } catch (MyDAOException e) {
            // If there's an access error, add the message to our list of
            // messages
            messages.add(e.getMessage());
            beans = new FavoriteBean[0];
        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");

        generateHead(out);

        out.println("<body>");
        out.println("<h2>Favorites for " + user.getLastName() + ", " + user.getFirstName() + ": " + user.getUserId()
                + "</h2>");

        // Generate an HTML <form> to get data from the user
        out.println("<form method=\"GET\">");
        out.println("    <table>");
        out.println("        <tr><td colspan=\"3\"><hr/></td></tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: large\">URL:</td>");
        out.println("            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"URL\"/></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: large\">Comment:</td>");
        out.println("            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"comment\"/></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td></td>");
        out.println("            <td><input type=\"submit\" name=\"action\" value=\"Add Favorite\"/></td>");
        out.println("            <td><input type=\"submit\" name=\"action\" value=\"Log out\"/></td>");
        out.println("        </tr>");
        out.println("        <tr><td colspan=\"3\"><hr/></td></tr>");
        out.println("    </table>");
        out.println("</form>");

        for (String message : messages) {
            out.println("<p style=\"font-size: large; color: red\">");
            out.println(message);
            out.println("</p>");
        }

        out.println("<p style=\"font-size: x-large\">The list now has " + beans.length + " favorites.</p>");
        out.println("<table>");
        for (int i = 0; i < beans.length; i++) {
            // String clickStr = request.getParameter("clickCount");
            // String favoriteIdStr = request.getParameter("favoriteId");
            // int click;
            // int favoriteId;
            // if (clickStr != null && favoriteIdStr != null) {
            // click = Integer.parseInt(clickStr) + 1;
            // favoriteId = Integer.parseInt(favoriteIdStr);
            // } else {
            // click = beans[i].getClickCount();
            // favoriteId = beans[i].getFavoriteId();
            // }
            // try {
            // favoriteDAO.incrementClick(click + 1, favoriteId);
            // } catch (MyDAOException e) {
            // // If there's an access error, add the message to our list of
            // // messages
            // messages.add(e.getMessage());
            // beans = new FavoriteBean[0];
            // }
            out.println("    <tr>");
            out.println("        <td>");
            // out.println(" <form method=\"POST\">");
            // out.println(" <input type=\"hidden\" name=\"favoriteId\"
            // value=\""
            // + beans[i].getFavoriteId() + "\" />");
            // out.println(" </form>");
            out.println("        </td>");
            out.println("        <td><span style=\"font-size: x-large\">" + (i + 1) + ".</span></td>");
            out.println("        <td>");
            out.println("          <a href=\"fav?favoriteId=" + beans[i].getFavoriteId() + "\">");
            // out.println(" <a href=\"Calculator\">");
            out.println("            <span style=\"font-size: x-large\">" + beans[i].getURL() + "</span>");
            out.println("          </a>");
            out.println("        </td>");
            out.println("    </tr>");

            out.println("    <tr>");
            out.println("        <td></td>");
            out.println("        <td></td>");
            out.println("        <td><span style=\"font-size: x-large\">" + beans[i].getComment() + "</td>");
            out.println("    </tr>");

            out.println("    <tr>");
            out.println("        <td></td>");
            out.println("        <td></td>");
            out.println("        <td><span style=\"font-size: x-large\">" + beans[i].getClickCount() + " Clicks </td>");
            out.println("    </tr>");
        }
        out.println("</table>");

        out.println("</body>");
        out.println("</html>");
    }
}
