package main.javaSrc.DBHelpers;

import main.javaSrc.DBHelpers.Managers.*;
import main.javaSrc.Entities.*;
import main.javaSrc.helpers.EVException;

import java.sql.Connection;
import java.util.List;

/**
 * Created by User on 10/27/2016.
 */
public class PersistenceLayerImpl implements PersistenceLayer{


    ClientManager clientManager = null;


    user_tokenManager user_tokenManager = null;

    RestaurantManager restaurantManager = null;


    public PersistenceLayerImpl() {

    }

    public PersistenceLayerImpl(Connection connection,ObjectLayer objectLayer) {

        this.clientManager = new ClientManager(connection,objectLayer);

        this.user_tokenManager = new user_tokenManager(objectLayer, connection);

        this.restaurantManager = new RestaurantManager(connection, objectLayer);
    }


    @Override
    public List<Restaurant> restoreRestaurants(Restaurant modelRestaurant) throws EVException {
        return restaurantManager.restore(modelRestaurant);
    }

    @Override
    public List<Client> restoreClient(Client client) throws EVException {
        return clientManager.restore(client);
    }

    @Override
    public Client storeClient(Client client) throws EVException {
        return clientManager.store(client);

    }

    @Override
    public void deleteClient(Client client) throws EVException {
        clientManager.delete(client);
    }


    @Override
    public void storeSysState(String open) throws EVException {
        clientManager.storeSysState(open);
    }

    @Override
    public String restoreSysOpen() throws EVException {
        return clientManager.restoreSysState();
    }

}
