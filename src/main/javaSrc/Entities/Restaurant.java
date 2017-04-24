package main.javaSrc.Entities;

import main.javaSrc.helpers.EVException;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public interface Restaurant extends Entity{


//    void setPersistent(boolean persistent);
//
//    boolean isPersistent();
//
//    String getRestoreString() throws EVException;
//
//    PreparedStatement insertStoreData(PreparedStatement stmt) throws EVException, SQLException;

    //String getId();

    //String getType();

    String getName();

    Double getRating();

    String getPrice();

    String getPhone();

    String getUrlString();

    String getImage();

    String getAddress();

    String getCity();

    String getState();

    String getZip();

    //void setId(String id);

    void setName(String name);

    void setRating(Double rating);

    void setPrice(String price);

    void setPhone(String phone);

    void setUrlString(String urlString);

    void setImage(String image);

    void setAddress(String address);

    void setCity(String city);

    void setState(String state);

    void setZip(String zip);

}
