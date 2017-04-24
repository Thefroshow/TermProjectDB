package main.javaSrc.HttpClasses;

import com.sun.net.httpserver.HttpExchange;
import main.javaSrc.Entities.Entity;
import main.javaSrc.Entities.Restaurant;
import main.javaSrc.helpers.ParamMap;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;



//child of exchange object that provides database related functionality
public class DBExchange extends Exchange{

    private String dbRequest;

    private String requestBody;
    public DBExchange(HttpExchange httpExchange) {
        String spacer = "*********************************************";
        log.out(spacer);
        this.httpExchange = httpExchange;
        this.dbRequest = getDbRequest();
        log.out("Received DB request: "+dbRequest);
        this.requestBody = getDBRequestBody();
        this.paramMap = new ParamMap(getRequest());
    }

    public void returnObjectList(List<Entity> entities) {
        try {
            for(Entity entity : entities) {
                log.out("serving " + entity.getType() + " with id " + entity.getId());
                if(entity.getType() == "Restaurant")
                    System.out.println("RETURN OBJ: " + ((Restaurant)entity).getName());
            }

            httpExchange.sendResponseHeaders(200,0);
            ObjectMapper mapper = new ObjectMapper();
            OutputStream outputStream = httpExchange.getResponseBody();
            mapper.writeValue(outputStream,entities);
            System.out.println(outputStream.toString());
            outputStream.close();
        }catch (Exception e){
            log.error(e.toString());
        }
    }

    public void returnObject(Entity entity){
        try {
            log.out("serving "+entity+" with id "+entity.getId());
            httpExchange.sendResponseHeaders(200,0);
            ObjectMapper mapper = new ObjectMapper();
            OutputStream outputStream = httpExchange.getResponseBody();
            mapper.writeValue(outputStream,entity);
            //outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            log.error(e.toString());
        }


    }

    //used if there is an example object
    private String getDBRequestBody() {
        String RequestBody=null;
        try {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);

            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }

            br.close();
            isr.close();
            requestBody = buf.toString();
        }catch (Exception e ){
            log.error(e.toString());
        }
        return requestBody;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getDBRequestObjectType(){
        int dataLoc = dbRequest.indexOf("data/")+5;
        int typeLoc = dbRequest.indexOf('/',dataLoc)+1;
        int objectTypeLoc = dbRequest.indexOf('/',typeLoc);
        if(objectTypeLoc==-1){
            return dbRequest.substring(typeLoc,dbRequest.length());
        }else{
            return dbRequest.substring(typeLoc,objectTypeLoc);
        }
    }

    //returns an entity
    public String getDBRequestType(){
        int dataLoc = dbRequest.indexOf("data/")+5;
        String requestType = dbRequest.substring(dataLoc,dbRequest.indexOf('/',dataLoc));
        return requestType;
    }

    private String getDbRequest() {
        dbRequest = httpExchange.getRequestURI().toString();
        int cutoff = dbRequest.indexOf('?');
        if (cutoff == -1) {
            cutoff = dbRequest.length();
        }
        dbRequest = dbRequest.substring(0, cutoff);
        return dbRequest;
    }


}

