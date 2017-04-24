package main.javaSrc.DBHelpers.CDFSTHelpers;

import main.javaSrc.DBHelpers.DbConnHelper;
import main.javaSrc.Entities.*;
import main.javaSrc.Entities.EntityImpl.*;
import main.javaSrc.HttpClasses.DBExchange;
import main.javaSrc.helpers.EVException;
import main.javaSrc.helpers.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;


public class FindHelper extends CDFSTHelper {
    private static Logger log = new Logger(FindHelper.class);

    public FindHelper(DBExchange dbExchange , DbConnHelper dbConnHelper) {

        super(dbExchange,dbConnHelper);
    }

    public List<Entity> execute(){

        String objectType = dbExchange.getDBRequestObjectType();
        String sourced = dbExchange.getParam("sourced");
        ObjectMapper mapper = new ObjectMapper();
        Entity model;

        try{
            switch (objectType){

                case "sysOpen":
                    log.out("attempting to find VoterRecord based on model object");
                    String isSysOpen = objectLayer.findSysOpen();
                case "Restaurants":
                    if (sourced.equals("true")) {
                        log.out("attempting to find Restaurant based on model object");
                        model = mapper.readValue(dbExchange.getRequestBody(), RestaurantImpl.class);
                        entities.addAll(objectLayer.findRestaurants((Restaurant) model));
                    }
                    break;
                default:
                    log.error("Unsupported object type "+objectType);
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (EVException e){
            e.printStackTrace();
        }
        dbConnHelper.commit(connection);

//        for(Entity entity : entities){
//            System.out.println();
//        }

        return entities;
    }
}
