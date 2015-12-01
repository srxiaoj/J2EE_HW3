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

public class HW3 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private FavoriteDAO favoriteDAO;
//    private UserDAO userDAO;
    private FormBeanFactory<FavoriteForm> favoriteFormFactory = FormBeanFactory.getInstance(FavoriteForm.class);

    public void init() throws ServletException {
        String jdbcDriverName = getInitParameter("jdbcDriver");
        String jdbcURL = getInitParameter("jdbcURL");

        try {
            ConnectionPool cp = new ConnectionPool(jdbcDriverName, jdbcURL);
//            userDAO = new UserDAO(cp, "haoruiw_user");
            favoriteDAO = new FavoriteDAO(cp, "haoruiw_favorite");
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
        UserBean user = (UserBean) session.getAttribute("email");
        String action = request.getParameter("action");
        if (user == null) {
            response.sendRedirect("Login");
            return;
        }
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);

        try {
            // Fetch the items now, so that in case there is no form or there
            // are errors
            // We can just dispatch to the JSP to show the item list (and any
            // errors)
            request.setAttribute("favorites", favoriteDAO.getUserFavorites(user.getUserId()));

            FavoriteForm form = favoriteFormFactory.create(request);
            request.setAttribute("form", form);
            if (!form.isPresent()) {
                RequestDispatcher d = request
                        .getRequestDispatcher("hw3.jsp");
                d.forward(request, response);
                return;
            }
            errors.addAll(form.getValidationErrors());
            if (errors.size() > 0) {
                RequestDispatcher d = request
                        .getRequestDispatcher("hw3.jsp");
                d.forward(request, response);
                return;
            }
            FavoriteBean bean = new FavoriteBean();
            bean.setUrl(form.getURL());
            bean.setComment(form.getComment());
            UserBean u = (UserBean) request.getSession().getAttribute("email");
            bean.setUserId(u.getUserId());

            if (action == null) {
                // No change to list requested
                RequestDispatcher d = request
                        .getRequestDispatcher("hw3.jsp");
                d.forward(request, response);
                return;
            }

            if (action.equals("Log out")) {
                response.sendRedirect("Login");
                session.setAttribute("email", null); //remove session also
                return;
            }

            if (action.equals("Add Favorite")) {
                RequestDispatcher d = request
                        .getRequestDispatcher("hw3.jsp");
                d.forward(request, response);
                return;
            }
            
         // Fetch the items again, since we modified the list
            request.setAttribute("favorites", favoriteDAO.getUserFavorites(user.getUserId()));
            RequestDispatcher d = request
                    .getRequestDispatcher("hw3.jsp");
            d.forward(request, response);
            
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
