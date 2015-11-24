

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private List<Connection> connectionPool = new ArrayList<Connection>();

    private String jdbcDriver;
    private String jdbcURL;
    private String tableName;

    public UserDAO(String jdbcDriver, String jdbcURL, String tableName)
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

    public void create(User user) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO "
                    + tableName + " (email,firstName,lastName,password) VALUES (?,?,?,?)");

            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getPassword());
            int count = pstmt.executeUpdate();
            if (count != 1)
                throw new SQLException("Insert updated " + count + " rows");

            pstmt.close();
            releaseConnection(con);

        } catch (Exception e) {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e2) { /* ignore */
            }
            throw new MyDAOException(e);
        }
    }

    /**
     * read the email address, search this email in table
     * if no user found, return null, otherwise check if the user is existed.
     * @param email
     * @return
     * @throws MyDAOException
     */
//    public User readInRegisterForm(String email) throws MyDAOException {
//        Connection con = null;
//        try {
//            con = getConnection();
//
//            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
//                    + tableName + " WHERE email=?");
//            pstmt.setString(1, email);
//            ResultSet rs = pstmt.executeQuery();
//
//            User user;
//            if (!rs.next()) {
//                user = null;
//            } else {
//                user = new User();
//                user.setUserId(rs.getInt("userId"));
//                user.setEmail(rs.getString("email"));
//                user.setFirstName(rs.getString("firstName"));
//                user.setLastName(rs.getString("lastName"));
//                user.setPassword(rs.getString("password"));
//            }
//
//            rs.close();
//            pstmt.close();
//            releaseConnection(con);
//            return user;
//
//        } catch (Exception e) {
//            try {
//                if (con != null)
//                    con.close();
//            } catch (SQLException e2) { /* ignore */
//            }
//            throw new MyDAOException(e);
//        }
//    }
    /**
     * read the email address, search this email in table
     * if no user found, return null, otherwise return the user.
     * @param email
     * @return
     * @throws MyDAOException
     */
    public User readInLoginForm(String email) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();

            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
                    + tableName + " WHERE email=?");
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            User user;
            if (!rs.next()) {
                user = null;
            } else {
                user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setPassword(rs.getString("password"));
            }

            rs.close();
            pstmt.close();
            releaseConnection(con);
            return user;

        } catch (Exception e) {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e2) { /* ignore */
            }
            throw new MyDAOException(e);
        }
    }
    /**
     * read the userId, search this userId in table
     * if no user found, return null, otherwise return the user.
     * @param email
     * @return
     * @throws MyDAOException
     */
    public User readInFavoriteForm(int userId) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();

            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
                    + tableName + " WHERE userId=?");
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            User user;
            if (!rs.next()) {
                user = null;
            } else {
                user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setPassword(rs.getString("password"));
            }

            rs.close();
            pstmt.close();
            releaseConnection(con);
            return user;

        } catch (Exception e) {
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
        Connection con = null;
        try {
            con = getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE "
                    + tableName
                    + " (userId int not null auto_increment, email VARCHAR(255) NOT NULL, firstName text, lastName text, password VARCHAR(255), PRIMARY KEY(userId))");
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
