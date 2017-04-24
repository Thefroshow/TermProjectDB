package main.javaSrc.DBHelpers;

import java.util.List;

import main.javaSrc.Entities.*;
import main.javaSrc.Entities.EntityImpl.*;
import main.javaSrc.helpers.EVException;
import main.javaSrc.helpers.Logger;

public class ObjectLayerImpl implements ObjectLayer {

    private static Logger log = new Logger( ObjectLayerImpl.class);

    private PersistenceLayer persistenceLayer;


    public ObjectLayerImpl(PersistenceLayer persistenceLayer) {

        this.persistenceLayer = persistenceLayer;
    }

    public ObjectLayerImpl(){
        this.persistenceLayer=null;
    }



    @Override
    public List<Restaurant> findRestaurants(Restaurant modelRestaurant) throws EVException {
        return persistenceLayer.restoreRestaurants(modelRestaurant);
    }

    @Override
    public Restaurant createClient(String name, Double rating, String price, String phone, String URL, String image, String address, String state, String zip, String city) throws EVException {
        return null;
    }

//    @Override
//    public Restaurant findRestaurant(Restaurant modelRestaurant) throws EVException {
//        return persistenceLayer.restoreRestaurant(modelRestaurant);
//    }

//    @Override
//    public Restaurant createRestaurant(String name, Double rating, String price, String phone, String URL, String image, String address, String state, String zip, String city) throws EVException {
//        Restaurant restaurant = new RestaurantImpl(name,rating,price,phone,URL,image,address,state,zip,city);
//        return restaurant;
//    }

    @Override
    public Restaurant createRestaurant() { return new RestaurantImpl();}


    @Override
    public Client createClient(String firstName, String lastName, String userName, String password, String emailAddress, String address, String state, int zip, String city) throws EVException {

        Client client = new ClientImpl(firstName,lastName,userName,password,emailAddress,address,state,zip,city);
        return client;
    }

    @Override
    public Client createClient() {
        return new ClientImpl();
    }


    @Override
    public List<Client> findClient(Client modelClient) throws EVException {

        return persistenceLayer.restoreClient(modelClient);
    }

    @Override
    public Client storeClient(Client client) throws EVException {

       return persistenceLayer.storeClient(client);
    }

    @Override
    public void deleteClient(Client client) throws EVException {

        persistenceLayer.deleteClient(client);
    }


    @Override
    public void storeSysState(String open) throws EVException{
        persistenceLayer.storeSysState(open);
    }

    @Override
    public String findSysOpen() throws EVException {
        return persistenceLayer.restoreSysOpen();
    }


    public void setPersistenceLayer(PersistenceLayer persistenceLayer) {
        this.persistenceLayer = persistenceLayer;
    }

    public PersistenceLayer getPersistenceLayer() {
        return persistenceLayer;
    }

}
