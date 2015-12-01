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

public class FavoriteDAO extends GenericDAO<FavoriteBean>{
    public FavoriteDAO(ConnectionPool cp, String tableName) throws DAOException {
        super(FavoriteBean.class, tableName, cp);
    }

    public void create(FavoriteBean bean) throws RollbackException {
        
        try {
            Transaction.begin();
//            FavoriteBean[] a = match(MatchArg.max("position"));
//            
//            FavoriteBean bottomBean;
//            if (a.length == 0) {
//                bottomBean = null;
//            } else {
//                bottomBean = a[0];
//            }
//            int newPos;
//            if (bottomBean == null) {
//                // List is empty...just add it with position = 1
//                newPos = 1;
//            } else {
//                // Create the new item with position one more than the bottom
//                // bean's position
//                newPos = bottomBean.getPosition() + 1;
//            }
//
//            bean.setPosition(newPos);
            
         // Create a new ItemBean in the database with the next id number
            createAutoIncrement(bean);
            Transaction.commit();
        } finally {
            if (Transaction.isActive())
                Transaction.rollback();
        }
        
//            Connection con = null;
//            con = getConnection();
//            con.setAutoCommit(false);
//
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT MAX(position) FROM "
//                    + tableName + " FOR UPDATE");
//            rs.next();
//            int maxPos = rs.getInt("MAX(position)"); // Will return 0 if there
//                                                     // are no items
//            rs.close();
//            stmt.close();
//
//            bean.setPosition(maxPos + 1);
//
//            PreparedStatement pstmt = con.prepareStatement("INSERT INTO "
//                    + tableName
//                    + " (userId,URL,comment,clickCount,position) VALUES (?,?,?,?,?)");
//            pstmt.setInt(1, bean.getUserId());
//            pstmt.setString(2, bean.getURL());
//            pstmt.setString(3, bean.getComment());
//            pstmt.setInt(4, bean.getClickCount());
//            pstmt.setInt(5, bean.getPosition());
//            pstmt.executeUpdate();
//            pstmt.close();
//
//            stmt = con.createStatement();
//            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
//            rs.next();
//            bean.setFavoriteId(rs.getInt("LAST_INSERT_ID()"));
//
//            con.commit();
//            con.setAutoCommit(true);
//
//            releaseConnection(con);

    }
    
    public FavoriteBean getFavorites(int favoriteId) throws RollbackException {
        FavoriteBean bean;
        bean = read(favoriteId);
        return bean;
//        Connection con = null;
//        try {
//            con = getConnection();
//            
//            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
//                    + tableName + " WHERE favoriteId=?");
//            pstmt.setInt(1, favoriteId);
//            ResultSet rs = pstmt.executeQuery();
//
//            FavoriteBean bean = null;
//            while (rs.next()) {
//                bean = new FavoriteBean();
//                bean.setFavoriteId(rs.getInt("favoriteId"));
//                bean.setUserId(rs.getInt("userId"));
//                bean.setURL(rs.getString("URL"));
//                bean.setComment(rs.getString("comment"));
//                bean.setClickCount(rs.getInt("clickCount"));
//                bean.setPosition(rs.getInt("position"));
//            }
//            pstmt.close();
//            releaseConnection(con);
//
//            return bean;
//        } catch (SQLException e) {
//            try {
//                if (con != null)
//                    con.close();
//            } catch (SQLException e2) { /* ignore */
//            }
//            throw new MyDAOException(e);
//        }
    }
    
    public FavoriteBean[] getUserFavorites(int userId) throws RollbackException {
        FavoriteBean[] a = match(MatchArg.equals("userId", userId));
        return a;
//        Connection con = null;
//        try {
//            con = getConnection();
//            
//            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
//                    + tableName + " WHERE userId =?");
//            pstmt.setInt(1, userId);
//            ResultSet rs = pstmt.executeQuery();
//
//            List<FavoriteBean> list = new ArrayList<FavoriteBean>();
//            while (rs.next()) {
//                FavoriteBean bean = new FavoriteBean();
//                bean.setFavoriteId(rs.getInt("favoriteId"));
//                bean.setUserId(rs.getInt("userId"));
//                bean.setURL(rs.getString("URL"));
//                bean.setComment(rs.getString("comment"));
//                bean.setClickCount(rs.getInt("clickCount"));
//                bean.setPosition(rs.getInt("position"));
//                list.add(bean);
//            }
//            pstmt.close();
//            releaseConnection(con);
//
//            return list.toArray(new FavoriteBean[list.size()]);
//        } catch (SQLException e) {
//            try {
//                if (con != null)
//                    con.close();
//            } catch (SQLException e2) { /* ignore */
//            }
//            throw new MyDAOException(e);
//        }
    }
    
    public void incrementClick(int favoriteId) throws RollbackException {
        FavoriteBean bean = read(favoriteId);
        if (bean != null) {
            int i = bean.getClickCount();
            bean.setClickCount(i+1);
            update(bean);
        }
//        Connection con = null;
//        try {
//            con = getConnection();
//            PreparedStatement pstmt = con.prepareStatement("UPDATE " + tableName + " SET clickCount=" + newClick
//                    + " WHERE favoriteId=" + favoriteId);
//            int i = pstmt.executeUpdate();
////            System.out.println(i);
//            pstmt.close();
//            releaseConnection(con);
//        } catch (SQLException e) {
//            try {
//                if (con != null)
//                    con.close();
//            } catch (SQLException e2) { /* ignore */
//            }
//            throw new MyDAOException(e);
//        }
    }
}
