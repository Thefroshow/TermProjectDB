package main.javaSrc.Entities.EntityImpl;

import main.javaSrc.Entities.Client;
import main.javaSrc.helpers.EVException;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by User on 10/31/2016.
 */
public class ClientImpl extends UserImpl implements Client {
    public ClientImpl(){}

    public ClientImpl(String firstName, String lastName, String userName, String userPassword, String emailAddress, String address, String state, int zip, String city) {
        super(firstName, lastName, userName, userPassword, emailAddress, address, state, zip, city);
    }

    @JsonIgnore
    @Override
    public String getRestoreString() throws EVException {
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        String restoreStr = "select Elections_Officer_ID, First_Name, Last_Name, Username, User_Password, Email_Address, Address, City, State, Zip from Users";

        condition.setLength( 0 );
        query.append( restoreStr );

        if( getId() > 0 ) { // id is unique, so it is sufficient to get a person
            query.append(" where Elections_Officer_ID = " + getId());
        }
        else {

            if( getFirstName() != null )
                condition.append( " where First_Name = '" + getFirstName() + "'" );

            if( getLastName() != null ) {
                if( condition.length() > 0 )
                    condition.append( " and" );
                else
                    condition.append( " where" );
                condition.append( " Last_Name = '" + getLastName() + "'" );
            }

            if( getUserName() != null ){
                if( condition.length() > 0 )
                    condition.append( " and" );
                else
                    condition.append( " where" );
                condition.append( " User_Name = '" + getUserName() + "'" );
            }


            if( getUserPassword() != null ){
                if( condition.length() > 0 )
                    condition.append( " and" );
                else
                    condition.append( " where" );
                condition.append( " User_Password = '" + getUserPassword() + "'" );

            }

            if( getEmailAddress() != null ){
                if( condition.length() > 0 )
                    condition.append( " and" );
                else
                    condition.append( " where" );
                condition.append( " Email_Address = '" + getEmailAddress() + "'" );

            }

            if( getAddress() != null ){
                if( condition.length() > 0 )
                    condition.append( " and" );
                else
                    condition.append( " where" );
                condition.append( " Address = '" + getAddress() + "'" );

            }

            if( getCity() != null ){
                if( condition.length() > 0 )
                    condition.append( " and" );
                else
                    condition.append( " where" );
                condition.append( " City = '" + getCity() + "'" );

            }

            if( getState() != null ){
                if( condition.length() > 0 )
                    condition.append( " and" );
                else
                    condition.append( " where" );
                condition.append( " State = '" + getState() + "'" );

            }

            if( Integer.toString(getZip()) != null ){
                if( condition.length() > 0 )
                    condition.append( " and" );
                else
                    condition.append( " where" );
                condition.append( " Zip = '" + getZip() + "'" );

            }

            query.append( condition );
        }

        return query.toString();
    }

    @JsonIgnore
    @Override
    public PreparedStatement insertStoreData(PreparedStatement stmt) throws EVException, SQLException {
        //Cannot be null

        if( getFirstName() != null )
            stmt.setString( 1, getFirstName() );
        else
            throw new EVException( "Client.save: can't save a  First Name undefined" );

        if( getLastName() != null )
            stmt.setString( 2, getLastName() );
        else
            throw new EVException( "Client.save: can't save a  Last Name undefined" );

        if( getUserName() != null )
            stmt.setString( 3, getUserName() );
        else
            throw new EVException( "Client.save: can't save a  Username undefined" );

        if( getUserPassword() != null )
            stmt.setString( 4, getUserPassword() );
        else
            throw new EVException( "Client.save: can't save a  Password undefined" );

        //The rest can be null

        if( getEmailAddress() != null )
            stmt.setString( 5, getEmailAddress() );
        else
            stmt.setNull( 5, java.sql.Types.VARCHAR );

        if( getAddress() != null )
            stmt.setString( 6, getAddress() );
        else
            stmt.setNull( 6, java.sql.Types.VARCHAR );

        if( getCity() != null )
            stmt.setString( 7, getCity() );
        else
            stmt.setNull( 7, java.sql.Types.VARCHAR );

        if( getState() != null )
            stmt.setString( 8, getState() );
        else
            stmt.setNull( 8, java.sql.Types.CHAR );

        //Client.getZip() returns an int, the db schema specified zip as a char array of length 5
        //I just converted the .getZip() output to a string. Could cause problems later
        if( Integer.toString(getZip()) != null )
            stmt.setString( 9, Integer.toString(getZip()) );
        else
            stmt.setNull( 9, java.sql.Types.CHAR );

        if(isPersistent()){
            stmt.setInt(10,this.getId());
        }
        return stmt;
    }

    @JsonIgnore
    @Override
    public String getType() {
        return "Client";
    }

}
