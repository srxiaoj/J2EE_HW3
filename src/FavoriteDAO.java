

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {
    private List<Connection> connectionPool = new ArrayList<Connection>();

    private String jdbcDriver;
    private String jdbcURL;
    private String tableName;

    public FavoriteDAO(String jdbcDriver, String jdbcURL, String tableName)
            throws MyDAOException {
        this.jdbcDriver = jdbcDriver;
        this.jdbcURL = jdbcURL;
        this.tableName = tableName;

        if (!tableExists())
            createTable();
    }

    private synchronized Connection getConnection() throws MyDAOException {
        if (connectionPool.size() > 0) {
            return connectionPool.remove(connectionPool.size() - 1);
        }

        try {
            Class.forName(jdbcDriver);
        } catch (ClassNotFoundException e) {
            throw new MyDAOException(e);
        }

        try {
            return DriverManager.getConnection(jdbcURL);
        } catch (SQLException e) {
            throw new MyDAOException(e);
        }
    }

    private synchronized void releaseConnection(Connection con) {
        connectionPool.add(con);
    }

    public void addToBottom(FavoriteBean bean) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(position) FROM "
                    + tableName + " FOR UPDATE");
            rs.next();
            int maxPos = rs.getInt("MAX(position)"); // Will return 0 if there
                                                     // are no items
            rs.close();
            stmt.close();

            bean.setPosition(maxPos + 1);

            PreparedStatement pstmt = con.prepareStatement("INSERT INTO "
                    + tableName
                    + " (userId,URL,comment,clickCount,position) VALUES (?,?,?,?,?)");
            pstmt.setInt(1, bean.getUserId());
            pstmt.setString(2, bean.getURL());
            pstmt.setString(3, bean.getComment());
            pstmt.setInt(4, bean.getClickCount());
            pstmt.setInt(5, bean.getPosition());
            pstmt.executeUpdate();
            pstmt.close();

            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            bean.setFavoriteId(rs.getInt("LAST_INSERT_ID()"));

            con.commit();
            con.setAutoCommit(true);

            releaseConnection(con);

        } catch (SQLException e) {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e2) { /* ignore */
            }
            throw new MyDAOException(e);
        }
    }

    public FavoriteBean[] getItems(int userId) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();
            
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
                    + tableName + " WHERE userId =?");
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            List<FavoriteBean> list = new ArrayList<FavoriteBean>();
            while (rs.next()) {
                FavoriteBean bean = new FavoriteBean();
                bean.setFavoriteId(rs.getInt("favoriteId"));
                bean.setUserId(rs.getInt("userId"));
                bean.setURL(rs.getString("URL"));
                bean.setComment(rs.getString("comment"));
                bean.setClickCount(rs.getInt("clickCount"));
                bean.setPosition(rs.getInt("position"));
                list.add(bean);
            }
            pstmt.close();
            releaseConnection(con);

            return list.toArray(new FavoriteBean[list.size()]);
        } catch (SQLException e) {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e2) { /* ignore */
            }
            throw new MyDAOException(e);
        }
    }

    public int size() throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id) FROM "
                    + tableName);

            rs.next();
            int count = rs.getInt("COUNT(id)");

            stmt.close();
            releaseConnection(con);

            return count;

        } catch (SQLException e) {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e2) { /* ignore */
            }
            throw new MyDAOException(e);
        }
    }

    private boolean tableExists() throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();
            DatabaseMetaData metaData = con.getMetaData();
            ResultSet rs = metaData.getTables(null, null, tableName, null);

            boolean answer = rs.next();

            rs.close();
            releaseConnection(con);

            return answer;

        } catch (SQLException e) {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e2) { /* ignore */
            }
            throw new MyDAOException(e);
        }
    }

    private void createTable() throws MyDAOException {
        Connection con = getConnection();
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE " + tableName
                    + " (favoriteId INT NOT NULL AUTO_INCREMENT,"
                    + " userId INT NOT NULL," + " URL VARCHAR(255),"
                    + " comment VARCHAR(255), "
                    + " clickCount INT NOT NULL DEFAULT 0, " 
                    + " position INT NOT NULL DEFAULT 0, " 
                    + " PRIMARY KEY(favoriteId))");
            stmt.close();
            releaseConnection(con);
        } catch (SQLException e) {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e2) { /* ignore */
            }
            throw new MyDAOException(e);
        }
    }
}
