package main.javaSrc.handlers;

import com.sun.net.httpserver.HttpExchange;
import main.javaSrc.DBHelpers.CDFSTHelpers.*;
import main.javaSrc.DBHelpers.DbConnHelper;
import main.javaSrc.DBHelpers.DbConnHelperImpl;
import main.javaSrc.Entities.Entity;
import main.javaSrc.HttpClasses.DBExchange;
import main.javaSrc.helpers.Logger;
import main.javaSrc.services.AuthService;

import java.util.List;

//handler for processing data requests
public class DataHandler extends Handler {

    private static Logger log = new Logger(DataHandler.class);

    public DataHandler(AuthService auth){
        this.auth = auth;
    }

    @Override
    public void handle(HttpExchange httpExchange){
        DBExchange exchange = new DBExchange(httpExchange);
        token = exchange.getParam("token");
        DbConnHelper dbConnHelper = new DbConnHelperImpl();

        String actionType = exchange.getDBRequestType();
        if(actionType.equals("register")||auth.isValidToken(token,dbConnHelper)){

            CDFSTHelper helper=null;


            switch (actionType){


                case "find":
                    helper = new FindHelper(exchange,dbConnHelper);
                    break;

                default:
                    exchange.pageNotFound();
                    break;
            }
            if(helper != null){
                List<Entity> entities = helper.execute();

                if(entities.size()!=0 && !entities.contains(null)) {

                    exchange.returnObjectList(entities);
                }else{
                    exchange.respondStr("200 success","text/html");
                }
            }
        }else{
            exchange.invalidToken();
        }
    }


}
