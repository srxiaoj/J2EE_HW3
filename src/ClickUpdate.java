/**
 * @author Haorui Wu
 * @date 11/23/2015
 * @courseNumber: 08672
 */
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.RollbackException;

public class ClickUpdate extends HttpServlet{
    private static final long serialVersionUID = 1L;

    private FavoriteDAO favoriteDAO;

    public void init() throws ServletException {
        String jdbcDriverName = getInitParameter("jdbcDriver");
        String jdbcURL = getInitParameter("jdbcURL");

        try {
            ConnectionPool cp = new ConnectionPool(jdbcDriverName, jdbcURL);
            favoriteDAO = new FavoriteDAO(cp, "haoruiw_favorite");
        } catch (DAOException e) {
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
                favoriteDAO.incrementClick(favoriteId);
                response.sendRedirect("");
            } catch (RollbackException e) {
                throw new ServletException(e);
            }
        }
    }
    
}
