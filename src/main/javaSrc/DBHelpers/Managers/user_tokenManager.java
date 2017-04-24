package main.javaSrc.DBHelpers.Managers;

import main.javaSrc.DBHelpers.ObjectLayer;
import main.javaSrc.Entities.Token;
import main.javaSrc.Entities.User;
import main.javaSrc.helpers.EVException;

import java.sql.*;

/**
 * Created by User on 11/9/2016.
 */
public class user_tokenManager {
    private ObjectLayer objectLayer;
    private Connection conn;

    public user_tokenManager(ObjectLayer objectLayer, Connection conn){
        this.objectLayer = objectLayer;
        this.conn = conn;
    }

    public User restore (Token token) throws EVException{
        StringBuffer query = new StringBuffer(500);
        Statement stmt = null;

        String userType = token.getTokenValue().substring(0,2);
        int tokenSpot = token.getTokenValue().lastIndexOf("Token");
        String userName = token.getTokenValue().substring(2, tokenSpot);
        System.out.println("UserName in user_tokenManager: " + userName);
        boolean isEO = false;

        if(userType.equals("EO")){

            query.append("select Elections_Officer_ID, First_Name, Last_Name, Username," +
                    " User_Password, Email_Address, Address, City, State, Zip from Users " +
                    "where Username = '" + userName + "'");
            isEO = true;
        }

        try{
            stmt = conn.createStatement();

            if (stmt.execute(query.toString())) {
                int voterId;
                String firstName;
                String lastName;
                String uName;
                String userPassword;
                String emailAddress;
                String address;
                String state;
                String city;
                String zip;
                User   nextUser = null;

                ResultSet rs = stmt.getResultSet();

                while (rs.next()) {
                    voterId = rs.getInt( 1 );
                    firstName = rs.getString( 2 );
                    lastName = rs.getString( 3 );
                    uName = rs.getString( 4 );
                    userPassword = rs.getString( 5 );
                    emailAddress = rs.getString( 6 );
                    address = rs.getString( 7 );
                    city = rs.getString( 8 );
                    state = rs.getString( 9 );
                    zip = rs.getString( 10 );

                    if(isEO)
                        nextUser = objectLayer.createClient();

                    // and now set its retrieved attributes
                    nextUser.setId( voterId );
                    nextUser.setFirstName( firstName );
                    nextUser.setLastName( lastName );
                    nextUser.setUserName( uName );
                    nextUser.setUserPassword( userPassword );
                    nextUser.setEmailAddress( emailAddress );
                    nextUser.setAddress( address );
                    nextUser.setState( state );
                    nextUser.setCity( city );
                    nextUser.setZip( Integer.parseInt(zip) );
                    nextUser.setPersistent(true);
                    break;
                }
                return nextUser;
            }
        }

        catch(SQLException e){
            e.printStackTrace();
            throw new EVException("client.store failed to save a candidate_Issue" +e);
        }
        throw new EVException("client.restore could not restore candidate object");
    }


}
