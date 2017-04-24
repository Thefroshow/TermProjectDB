package main.javaSrc.DBHelpers.Managers;

import main.javaSrc.DBHelpers.DbConnHelper;
import main.javaSrc.helpers.EVException;
import main.javaSrc.helpers.RandomString;

import java.sql.*;


/**
 * Created by User on 11/16/2016.
 */
public class AuthManager {

    public String[] validateCredentials(String userName, String password, Connection conn) throws EVException {

        try {

            String queryStr = "select User_Password from Users where Username = '"+userName+"'";
            Statement stmt = conn.createStatement();
            stmt.execute(queryStr);
            ResultSet rs = stmt.getResultSet();
            String userPass="";

            if(rs.next()){

                    userPass = rs.getString(1);

                if (password.equals(userPass)){

                    String[] response = new String[2];
                    response[0] = createToken("EO" + userName,conn);
                    response[1] = "http://localhost:9001/home.html";
                    return response;

                }else{
                    throw new EVException("incorrect password for user "+userName);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new EVException("AuthManager: could not find user with username "+userName);
    }

    private String createToken(String userName, Connection conn) throws EVException {

        RandomString randStr = new RandomString(4);
        String token = userName+"Token"+randStr.nextString();
        String uname = userName.substring(2);

        checkUserHasExistingToken(uname,conn);

        String query = "insert into Token values ( '"+uname+"', '"+token+"')";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            if(stmt.executeUpdate()>=1){
                return token;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new EVException("could not insert new Token "+token);
    }

    private void checkUserHasExistingToken(String userName, Connection conn) {

        try {

            Statement stmt= conn.createStatement();
            String query = "delete from Token where User_Name = '"+userName+"'";
            stmt.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean validateToken(String token, Connection connection) {
        String query = "select Token from Token where Token = '"+token+"'";
        boolean isValid = false;
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(query);
            ResultSet rs = stmt.getResultSet();
            if(rs.next()){
                isValid = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }
}
