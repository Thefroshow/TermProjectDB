package main.javaSrc.Entities.EntityImpl;

import main.javaSrc.Entities.Restaurant;
import main.javaSrc.helpers.EVException;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by User on 11/8/2016.
 */
public class RestaurantImpl extends EntityImpl implements Restaurant {

//    private String question;
//    private int yesCount;
//    private int noCount;
    private boolean persistent = false;

    //private String id;
    private String name;
    private Double rating;
    private String price;
    private String phone;
    private String urlString;
    private String image;
    private String address;
    private String city;
    private String state;
    private String zip;

    public RestaurantImpl(String name, Double rating, String price, String phone, String urlString, String image, String address, String state, String zip, String city) {
        setId(2);
        //this.id = id;
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.phone = phone;
        this.urlString = urlString;
        this.image = image;
        this.address = address;
        this.state = state;
        this.zip = zip;
        this.city = city;

    }

    public RestaurantImpl() {
        setId(2);
    }



    @JsonIgnore
    @Override
    public String getRestoreString() throws EVException {
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        String restoreStr = "select * from Restaurants";

        condition.setLength( 0 );
        query.append( restoreStr );

//        if( getId() > 0 ) { // id is unique, so it is sufficient to get a person
//            query.append(" where Issue_ID = " + getId());
//        }
//        else {

                if (getCity() != null) {
                    if (condition.length() > 0)
                        condition.append(" and");
                    else
                        condition.append(" where");
                    condition.append(" city = '" + getCity() + "'");
                }

                if (getState() != null) {
                    if (condition.length() > 0)
                        condition.append(" and");
                    else
                        condition.append(" where");
                    condition.append(" state = '" + getState() + "'");
                }

                if (getZip() != null) {
                     if (condition.length() > 0)
                         condition.append(" and");
                      else
                         condition.append(" where");
                    condition.append(" zip = '" + getZip() + "'");
                }

                if (getPrice() != null) {
                    if (condition.length() > 0)
                        condition.append(" and");
                    else
                        condition.append(" where");
                    condition.append(" length(price) <= " + getPrice().length() + "");
                }

                if (getRating() != null) {
                    if (condition.length() > 0)
                        condition.append(" and");
                    else
                        condition.append(" where");
                    condition.append(" rating >= " + getRating() + "");
                }

            query.append( condition );


        String innerQuery = query.toString();

        innerQuery = innerQuery + " order by rand() limit 1";

        System.out.println(innerQuery);

        return innerQuery;
    }

    @JsonIgnore
    @Override
    public PreparedStatement insertStoreData(PreparedStatement stmt) throws EVException, SQLException {
        //Cannot be null

//        if( getQuestion() != null )
//            stmt.setString( 1, getQuestion() );
//        else
//            throw new EVException( "IssueManager.save: can't save a  Question undefined" );
//
//        //the rest can be null
//        if( getVoteCount() >= 0 )
//            stmt.setInt( 2, getVoteCount() );
//        else
//            stmt.setNull( 2, java.sql.Types.INTEGER );
//
//        if( getYesCount() >= 0 )
//            stmt.setInt( 3, getYesCount() );
//        else
//            stmt.setNull( 3, java.sql.Types.INTEGER );
//
//        if( getNoCount() >= 0 )
//            stmt.setInt( 4, getNoCount() );
//        else
//            stmt.setNull( 4, java.sql.Types.INTEGER );
//
//        if (isPersistent()){
//            stmt.setInt(5,this.getId());
//        }

        return stmt;

    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    @Override
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public String getType() {
        return "Restaurant";
    }

    @Override
    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    @Override
    public boolean isPersistent() {
        return persistent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Double getRating() {
        return rating;
    }

    @Override
    public String getPrice() {
        return price;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getUrlString() {
        return urlString;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getZip() {
        return zip;
    }
}
