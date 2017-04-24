package main.javaSrc.DBHelpers;


import main.javaSrc.Entities.*;
import main.javaSrc.helpers.EVException;

import java.util.List;

public interface PersistenceLayer {

    //public Restaurant restoreRestaurant(Restaurant modelRestaurant) throws EVException;

    public List<Restaurant> restoreRestaurants(Restaurant modelRestaurant) throws EVException;


    public List<Client> restoreClient(Client modelClient) throws EVException;

    
    public Client storeClient(Client client) throws EVException;

    
    public void deleteClient( Client client) throws EVException;


    void storeSysState(String open) throws EVException;

    String restoreSysOpen() throws EVException;
}

