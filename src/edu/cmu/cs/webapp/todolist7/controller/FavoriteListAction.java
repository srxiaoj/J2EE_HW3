package edu.cmu.cs.webapp.todolist7.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;

import edu.cmu.cs.webapp.todolist7.databean.UserBean;
import edu.cmu.cs.webapp.todolist7.model.FavoriteDAO;
import edu.cmu.cs.webapp.todolist7.model.Model;

public class FavoriteListAction extends Action {
    private FavoriteDAO favoriteDAO;

    public FavoriteListAction(Model model) {
        favoriteDAO = model.getFavoriteDAO();
    }

    public String getName() {
        return "favoritelist.do";
    }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);
        
        HttpSession session = request.getSession();
        UserBean user = (UserBean) session.getAttribute("user");

        try {
            request.setAttribute("favorites", favoriteDAO.getUserFavorites(user.getUserId()));
            return ("favorite.jsp");
        } catch (RollbackException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}
