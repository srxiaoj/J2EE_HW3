package edu.cmu.cs.webapp.todolist6;
/**
 * @author Haorui Wu
 * @date 11/23/2015
 * @courseNumber: 08672
 */
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.RollbackException;

import edu.cmu.cs.webapp.todolist6.dao.FavoriteDAO;

public class ClickUpdate extends HttpServlet{
    private static final long serialVersionUID = 1L;

    private FavoriteDAO favoriteDAO;

    public void init() throws ServletException {
        ServletContext context = getServletContext();
        String jdbcDriverName = context.getInitParameter("jdbcDriverName");
        String jdbcURL = context.getInitParameter("jdbcURL");

        try {
            ConnectionPool cp = new ConnectionPool(jdbcDriverName, jdbcURL);
            favoriteDAO = new FavoriteDAO(cp, "haoruiw_favorite");
        } catch (DAOException e) {
            throw new ServletException(e);
        }
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        System.out.println("Click servlet start");
        response.setContentType("text/html");
        String favoriteIdStr = request.getParameter("favoriteId");
        if (favoriteIdStr != null) {
            int favoriteId = Integer.parseInt(favoriteIdStr);
            try {
                favoriteDAO.incrementClick(favoriteId);
//                response.sendRedirect("FavoriteList");
            } catch (RollbackException e) {
                throw new ServletException(e);
            }
        }
    }
    
}
