package main.javaSrc.DBHelpers;

import main.javaSrc.Entities.*;
import main.javaSrc.helpers.EVException;

import java.util.List;


public interface ObjectLayer {


    public List<Restaurant> findRestaurants(Restaurant modelRestaurant) throws EVException;

    //public Restaurant findRestaurant(Restaurant modelRestaurant) throws EVException;

    public Restaurant createClient(String name, Double rating, String price, String phone, String URL, String image, String address, String state, String zip, String city) throws EVException;

    public Restaurant createRestaurant();


    void setPersistenceLayer(PersistenceLayer persistenceLayer);
    
    public Client createClient(String firstName, String lastName, String userName, String password, String emailAddress, String address, String state, int zip, String city ) throws EVException;

    
    public Client createClient();

    
    public List<Client> findClient(Client modelClient) throws EVException;

    
    public Client storeClient(Client client) throws EVException;

    
    public void deleteClient( Client client) throws EVException;


    void storeSysState(String open) throws EVException;

    String findSysOpen() throws EVException;
}


