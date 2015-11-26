/**
 * @author Haorui Wu
 * @date 11/23/2015
 * @courseNumber: 08672
 */



import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

public class UserDAO extends GenericDAO<UserBean> {
    public UserDAO(ConnectionPool cp, String tableName) throws DAOException {
        super(UserBean.class, tableName, cp);
    }
    /**
     * create user bean with primaryKey and override the original user bean
     */
//    public void create(UserBean user) throws RollbackException {
//        
//        try {
//            Transaction.begin();
//         // Create a new ItemBean in the database with the next id number
//            
//            create(user);
////            createAutoIncrement(user);
//            Transaction.commit();
//        } finally {
//            if (Transaction.isActive())
//                Transaction.rollback();
//        }
//    }
    
    public UserBean[] getUser(String email) throws RollbackException {
        UserBean[] beans = match(MatchArg.containsIgnoreCase("email", email));
        return beans;
    }
}

