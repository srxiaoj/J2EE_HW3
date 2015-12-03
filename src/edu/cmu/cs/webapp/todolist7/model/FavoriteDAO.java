package edu.cmu.cs.webapp.todolist7.model;

import java.util.Comparator;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import edu.cmu.cs.webapp.todolist7.databean.FavoriteBean;

public class FavoriteDAO extends GenericDAO<FavoriteBean> {
    public FavoriteDAO(ConnectionPool cp, String tableName) throws DAOException {
        super(FavoriteBean.class, tableName, cp);
    }

    public void create(FavoriteBean bean) throws RollbackException {

        try {
            Transaction.begin();

            // Create a new ItemBean in the database with the next id number
            createAutoIncrement(bean);
            Transaction.commit();
        } finally {
            if (Transaction.isActive())
                Transaction.rollback();
        }
    }

    public FavoriteBean[] getUserFavorites(int userId) throws RollbackException {
        FavoriteBean[] a = match(MatchArg.equals("userId", userId));
        return a;
    }

    public void incrementClick(int favoriteId) throws RollbackException {
        FavoriteBean bean = read(favoriteId);
        if (bean != null) {
            int i = bean.getClickCount();
            bean.setClickCount(i + 1);
            update(bean);
        }
    }
}
