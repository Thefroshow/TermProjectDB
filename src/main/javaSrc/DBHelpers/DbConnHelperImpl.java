package main.javaSrc.DBHelpers;

import main.javaSrc.helpers.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by User on 11/7/2016.
 */
public class DbConnHelperImpl implements DbConnHelper {

    private static Logger log = new Logger(DbConnHelperImpl.class);

    public DbConnHelperImpl(){
    }



    @Override
    public void disableAutoCommit(Connection conn) {
        try {
            conn.setAutoCommit(false);
        }catch (SQLException ex){
            log.error("SQLException on setting auto-commit false");
        }
    }

    @Override
    public void enableAutoCommit(Connection conn) {
        try {
            conn.setAutoCommit(true);
        }catch (SQLException ex){
            log.error("SQLException on setting auto-commit true");
        }
    }

    @Override
    public void commit(Connection conn) {
        try {
            log.out("Committing changes to Database");
            conn.commit();
        }catch (SQLException ex){
            log.error("SQLException committing connection");
        }
    }

    @Override
    public void rollback(Connection conn) {
        try {
            conn.rollback();
        }catch (SQLException ex){
            log.error("SQLException on rolling back connection");
        }
    }

    @Override
    public Connection getConnection() {
        Connection dbConnection = null;

        try {

            Class.forName(DbConfig.DB_DRIVE_NAME);

        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());
        }

        try {
            log.out("connecting to DinnerDateDecider database");
            dbConnection = DriverManager.getConnection(
                    DbConfig.DB_CONNECTION_URL, DbConfig.DB_CONNECTION_USERNAME, DbConfig.DB_CONNECTION_PWD);
            return dbConnection;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return null;
    }
}
