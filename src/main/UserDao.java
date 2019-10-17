package main;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.sql.*;


public class UserDao {
    //    private ConnectionMaker connectionMaker;
    private DataSource dataSource;
    private Connection c;
    private User user;
    private JdbcContext jdbcContext;

    public void setJdbcContext(JdbcContext jdbcContext){
        this.jdbcContext = jdbcContext;
    }

//    public UserDao(ConnectionMaker connectionMaker) {
//        this.connectionMaker = connectionMaker;
//    }

    //    public void setConnectionMaker(ConnectionMaker connectionMaker){
//        this.connectionMaker = connectionMaker;
//    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(final User user) throws SQLException {
//        class AddStatement implements StatementStrategy {
//            User user;
//
//            public  AddStatement (User user) {
//                this.user = user;
//            }
//
//            @Override
//            public PreparedStatement makePreparedStatement (Connection c) throws SQLException {
//                PreparedStatement ps = c.prepareStatement(
//                        "insert into users(id, name, password) values(?,?,?)"
//                );
//                ps.setString(1, user.getId());
//                ps.setString(2, user.getName());
//                ps.setString(3, user.getPassword());
//
//                return ps;
//            }
//        }
        this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement (Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement(
                        "insert into users(id, name, password) values(?,?,?)"
                );
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());

                return ps;
            }
        });
//        StatementStrategy st =
//        jdbcContextWithStatementStrategy(st);
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?"
        );
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        User user = null;
        if (rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));

        }
//        User user = new User();
//        user.setId(rs.getString("id"));
//        user.setName(rs.getString("name"));
//        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        if (user == null) throw new EmptyResultDataAccessException(1);
        return user;
    }

    public void deleteAll() throws SQLException {

        this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("delete from users");
                return ps;
            }
        });
        //        Connection c = null;
//        PreparedStatement ps = null;
//        try {
//            c = dataSource.getConnection();
//
//
////            ps = makeStatement(c);
//            ps = strategy.makePreparedStatement(c);
//
//
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw e;
//        } finally {
//            if(ps != null){
//                try {
//                    ps.close();
//                }catch (SQLException e) {
//
//                }
//            }
//            if(c != null) {
//                try{
//                    c.close();
//                } catch (SQLException e){
//
//                }
//            }
    }

//
//        Connection c = dataSource.getConnection();
//        PreparedStatement ps = c.prepareStatement("delete from users");
//        ps.executeUpdate();
//
//        ps.close();
//        c.close();

    public int getCount() throws SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT  count(*) from users");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        c.close();

        return count;
    }
//
//    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
//        Connection c = null;
//        PreparedStatement ps = null;
//        try {
//            c = dataSource.getConnection();
//
//
//            ps = stmt.makePreparedStatement(c);
//
//
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw e;
//        } finally {
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (SQLException e) {
//
//                }
//            }
//            if (c != null) {
//                try {
//                    c.close();
//                } catch (SQLException e) {
//
//                }
//            }
//        }
//
////    abstract protected PreparedStatement makeStatement (Connection c) throws SQLException;
//    }
}
//
//public class NUserDao extends UserDao {
//    @Override
//    public Connection getConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection c = DriverManager.getConnection(
//                "jdbc:mysql://localhost/jangdn", "jangdn_user", "akdntm90"
//        );
//        return c;
//    }
//}
//
//public class UserDao extends UserDao {
//    @Override
//    public Connection getConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection c = DriverManager.getConnection(
//                "jdbc:mysql://localhost/jangdn", "jangdn_user", "akdntm90"
//        );
//        return c;
//    }
//}
