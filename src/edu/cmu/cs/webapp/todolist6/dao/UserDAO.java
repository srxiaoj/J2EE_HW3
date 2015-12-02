package edu.cmu.cs.webapp.todolist6.dao;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;

import edu.cmu.cs.webapp.todolist6.databean.UserBean;

public class UserDAO extends GenericDAO<UserBean> {
	public UserDAO(ConnectionPool cp, String tableName) throws DAOException {
		super(UserBean.class, tableName, cp);
	}
	
    public UserBean read(String email) throws RollbackException {
        UserBean[] beans = match(MatchArg.containsIgnoreCase("email", email));
        if (beans.length == 0) {
            return null;
        }
        else {
            return beans[0];
        }
    }
}
