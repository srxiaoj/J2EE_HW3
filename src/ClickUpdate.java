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

public class ClickUpdate extends HttpServlet{
    private static final long serialVersionUID = 1L;

    private FavoriteDAO favoriteDAO;
    private UserDAO userDAO;

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
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
//        System.out.println("!!!!!!!!!!");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String favoriteIdStr = request.getParameter("favoriteId");
        int favoriteId;
        if (favoriteIdStr != null) {
            favoriteId = Integer.parseInt(favoriteIdStr);
            try {
                FavoriteBean bean = favoriteDAO.getFavorite(favoriteId);
                favoriteDAO.incrementClick(bean.getClickCount() + 1, favoriteId);
                response.sendRedirect("");
            } catch (MyDAOException e) {
                throw new ServletException(e);
            }
        }
    }
    
}
