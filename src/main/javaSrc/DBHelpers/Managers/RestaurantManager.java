package main.javaSrc.DBHelpers.Managers;

import main.javaSrc.DBHelpers.ObjectLayer;
import main.javaSrc.Entities.Restaurant;
import main.javaSrc.helpers.EVException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11/2/2016.
 */
public class RestaurantManager extends Manager{

    public RestaurantManager(Connection conn, ObjectLayer objectLayer){
        super(conn,objectLayer);
    }

    public RestaurantManager() {

    }

    public List<Restaurant> restore(Restaurant restaurant) throws EVException {

        Statement    stmt = null;
        String query = "";
        List<Restaurant>   restaurants = new ArrayList<Restaurant>();

        if( restaurant != null ) {
            query = restaurant.getRestoreString();
        }

        try {

            stmt = conn.createStatement();

            // retrieve the persistent voter objects
            //
            if( stmt.execute( query) ) { // statement returned a result

                //int voterId;
                String id;
                String name;
                Double rating;
                String price;
                String phone;
                String urlString;
                String image;
                String address;
                String city;
                String state;
                String zip;
                Restaurant  nextRestaurant = null;

                ResultSet rs = stmt.getResultSet();

                // retrieve the retrieved voters
                while( rs.next() ) {

                    id = rs.getString( 1 );
                    name = rs.getString( 2 );
                    rating = rs.getDouble( 3 );
                    price = rs.getString( 4 );
                    phone = rs.getString( 5 );
                    urlString = rs.getString( 6 );
                    image = rs.getString( 7 );
                    address = rs.getString( 8 );
                    state = rs.getString( 10 );
                    city = rs.getString( 9 );
                    zip = rs.getString( 11 );

                    nextRestaurant = objectLayer.createRestaurant(); // create a proxy voter object
                    // and now set its retrieved attributes
                    //nextRestaurant.setId( id );
                    nextRestaurant.setName( name );
                    nextRestaurant.setRating( rating );
                    nextRestaurant.setPrice( price );
                    nextRestaurant.setPhone( phone );
                    nextRestaurant.setUrlString( urlString );
                    nextRestaurant.setImage( image );
                    nextRestaurant.setAddress( address );
                    nextRestaurant.setState( state );
                    nextRestaurant.setCity( city );
                    nextRestaurant.setZip( zip );
                    nextRestaurant.setPersistent(true);

                    System.out.println("NEXT REST NAME: " + nextRestaurant.getName());

                    restaurants.add( nextRestaurant );
                }

                return restaurants;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "restaurantManager.restore: Could not restore persistent voter objects; Root cause: " + e );
        }

        throw new EVException( "restaurantManager.restore: Could not restore persistent voter objects" );

    }

//    public Voter store(Voter voter) throws EVException{
//        String insertVoter = "insert into Voter (First_Name, Last_Name, Username, User_Password, Email_Address, Address, City, State, Zip) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        String updateVoter = "update Voter set First_Name = ?, Last_Name = ?, Username = ?, User_Password = ?, Email_Address = ?, Address = ?, City = ?, State = ?, Zip = ? where Voter_ID = ?";
//        PreparedStatement stmt = null;
//        int queryExecution;
//        int voterId;
//
//        try {
//
//            if( !voter.isPersistent() ) {
//                stmt = conn.prepareStatement(insertVoter);
//            }else {
//                stmt = conn.prepareStatement(updateVoter);
//            }
//
//            stmt=voter.insertStoreData(stmt);
//
//            queryExecution = stmt.executeUpdate();
//
//            if( !voter.isPersistent() ) {
//                if( queryExecution >= 1 ) {
//                   voter = (Voter) setId(stmt,voter);
//                    voter.setPersistent(true);
//                }
//                else
//                    throw new EVException( "VoterManager.save: failed to save a voter" );
//            }
//            else {
//                if( queryExecution < 1 )
//                    throw new EVException( "VoterManager.save: failed to save a voter" );
//            }
//        }
//        catch( SQLException e ) {
//            e.printStackTrace();
//            throw new EVException( "VoterManager.save: failed to save a voter: " + e );
//        }
//
//        return voter;
//    }








//    public void delete(Voter voter) throws EVException {
//
//        String               deleteVoter = "delete from Voter where Voter_ID = ?";
//        PreparedStatement    stmt = null;
//        int                  queryExecution;
//
//        if( !voter.isPersistent() ) // is the voter object persistent?  If not, nothing to actually delete
//            return;
//
//        try {
//            stmt = conn.prepareStatement( deleteVoter );
//            stmt.setInt( 1, voter.getId() );
//            queryExecution = stmt.executeUpdate();
//            if( queryExecution == 1 ) {
//                return;
//            }
//            else
//                throw new EVException( "voterManager.delete: failed to delete a voter" );
//        }
//        catch( SQLException e ) {
//            e.printStackTrace();
//            throw new EVException( "voterManager.delete: failed to delete a voter: " + e );        }
//    }

}

