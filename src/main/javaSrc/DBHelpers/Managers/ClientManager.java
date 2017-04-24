package main.javaSrc.DBHelpers.Managers;

import main.javaSrc.DBHelpers.ObjectLayer;
import main.javaSrc.Entities.Client;
import main.javaSrc.helpers.EVException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11/2/2016.
 */
public class ClientManager extends Manager{


    public ClientManager(Connection conn, ObjectLayer objectLayer){
        super(conn, objectLayer);
    }

    public ClientManager() {

    }

    public List<Client> restore(Client client) throws EVException {

        Statement    stmt = null;
        String query = "";
        List<Client> clients = new ArrayList<Client>();

        if( client != null ) {
            query = client.getRestoreString();
        }

        try {

            stmt = conn.createStatement();

            // retrieve the persistent client objects
            //
            if( stmt.execute( query) ) { // statement returned a result

                int electionsOfficerId;
                String firstName;
                String lastName;
                String userName;
                String userPassword;
                String emailAddress;
                String address;
                String state;
                String city;
                String zip;
                Client nextClient = null;

                ResultSet rs = stmt.getResultSet();

                // retrieve the retrieved clients
                while( rs.next() ) {

                    electionsOfficerId = rs.getInt( 1 );
                    firstName = rs.getString( 2 );
                    lastName = rs.getString( 3 );
                    userName = rs.getString( 4 );
                    userPassword = rs.getString( 5 );
                    emailAddress = rs.getString( 6 );
                    address = rs.getString( 7 );
                    state = rs.getString( 8 );
                    city = rs.getString( 9 );
                    zip = rs.getString( 10 );

                    nextClient = objectLayer.createClient(); // create a proxy client object
                    // and now set its retrieved attributes
                    nextClient.setId( electionsOfficerId );
                    nextClient.setFirstName( firstName );
                    nextClient.setLastName( lastName );
                    nextClient.setUserName( userName );
                    nextClient.setUserPassword( userPassword );
                    nextClient.setEmailAddress( emailAddress );
                    nextClient.setAddress( address );
                    nextClient.setState( state );
                    nextClient.setCity( city );
                    nextClient.setZip( Integer.parseInt(zip) );
                    nextClient.setPersistent(true);

                    clients.add(nextClient);
                }

                return clients;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "clientManager.restore: Could not restore persistent client objects; Root cause: " + e );
        }

        throw new EVException( "clientManager.restore: Could not restore persistent client objects" );

    }

    public Client store(Client client) throws EVException{
        String insertElectionsOfficer = "insert into Users ( First_Name, Last_Name, Username, User_Password, Email_Address, Address, City, State, Zip) values ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        String updateElectionsOfficer = "update Users set First_Name = ?, Last_Name = ?, Username = ?, User_Password = ?, Email_Address = ?, Address = ?, City = ?, State = ?, Zip = ? where Elections_Officer_ID = ?";
        PreparedStatement stmt = null;
        int queryExecution;
        int electionsOfficerId;

        try {

            if( !client.isPersistent() )
                stmt = conn.prepareStatement( insertElectionsOfficer );
            else
                stmt = conn.prepareStatement( updateElectionsOfficer );

            stmt = client.insertStoreData(stmt);

            queryExecution = stmt.executeUpdate();

            if( !client.isPersistent() ) {
                if( queryExecution >= 1 ) {
                   client = (Client)setId(stmt, client);
                    client.setPersistent(true);
                }
                else
                    throw new EVException( "ClientManager.save: failed to save a client" );
            }
            else {
                if( queryExecution < 1 )
                    throw new EVException( "ClientManager.save: failed to save a client" );
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "ClientManager.save: failed to save a client: " + e );
        }

        return client;
    }

    public void delete(Client client) throws EVException {

        String               deleteElectionsOfficer = "delete from Users where Elections_Officer_ID = ?";
        PreparedStatement    stmt = null;
        int                  queryExecution;

        if( !client.isPersistent() ) // is the client object persistent?  If not, nothing to actually delete
            return;

        try {
            stmt = conn.prepareStatement( deleteElectionsOfficer );
            stmt.setInt( 1, client.getId() );
            queryExecution = stmt.executeUpdate();
            if( queryExecution == 1 ) {
                return;
            }
            else
                throw new EVException( "clientManager.delete: failed to delete a client" );
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "clientManager.delete: failed to delete a client: " + e );        }
    }

    public void storeSysState(String open) throws EVException {

        String updateSysState = "update sysOpen set IsOpen = ?";
        PreparedStatement stmt = null;
        int queryExecution;

        try {
            stmt = conn.prepareStatement( updateSysState );

            stmt.setString(1,open);

            queryExecution = stmt.executeUpdate();

            if( queryExecution < 1 )
                throw new EVException( "sysState.save: failed to save a state" );
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "SysState.save: failed to save a state: " + e );
        }

    }
    public String restoreSysState() throws EVException {

        Statement    stmt = null;
        String query = "select IsOpen from sysOpen where SysOpen_ID = 1";

        try {

            stmt = conn.createStatement();


            if( stmt.execute( query) ) { // statement returned a result

                ResultSet rs = stmt.getResultSet();
                String isOpen= "false";
                while( rs.next() ) {
                    isOpen = rs.getString(1);
                }

                return isOpen;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "sys.restore: Could not restore persistent sys objects; Root cause: " + e );
        }

        throw new EVException( "sys.restore: Could not restore persistent sys objects" );

    }
}

