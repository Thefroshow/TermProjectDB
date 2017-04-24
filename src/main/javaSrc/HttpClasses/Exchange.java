package main.javaSrc.HttpClasses;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import main.javaSrc.helpers.Logger;
import main.javaSrc.helpers.ParamMap;


import java.io.*;

//Wrapper class for an httpExchange providing related functionality
public class Exchange {

    protected static Logger log = new Logger(Exchange.class);

    protected String root = "resources/";
    protected String request;
    protected HttpExchange httpExchange;
    protected ParamMap paramMap;

    public Exchange(HttpExchange httpExchange){

        this.httpExchange = httpExchange;
        this.request = getRequest();
        String spacer = "*********************************************";
        log.out(spacer);
        log.out("Received request: "+request);
        try {
            this.paramMap = new ParamMap(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Exchange() {
    }

    //determines if request for page
    public boolean isHtmlRequest(){
        String path = httpExchange.getRequestURI().getPath();
        return path.substring(path.length() - 5).equals(".html");
    }

    //responds with resource asked for in request
    public void respondFile(){

        String path = httpExchange.getRequestURI().getPath();
        System.out.println("PATH TESTING: " + path);
        respondFile(path);

    }

    //responds with given file
    public void respondFile(String path){

        try {
            log.out("looking for: " + path);
            if(path.charAt(0)=='/'){
                path=path.substring(1);
            }
            //this makes it work in a packaged jar
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);


            if (in == null) { //cant find resource
                pageNotFound();

            } else {

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                // Object exists and is a file: accept with response code 200.
                String mime = getMime(path);
                Headers h = httpExchange.getResponseHeaders();
                h.set("Content-Type", mime);
                h.add("Connection", "close");
                httpExchange.sendResponseHeaders(200, 0);

                OutputStream os = httpExchange.getResponseBody();

                final byte[] buffer = new byte[0x10000];  //read resource
                int count = 0;
                while ((count = in.read(buffer)) >= 0) {
                    os.write(buffer, 0, count);
                }
                in.close();
                os.flush();
                os.close();  //respond
                log.out("serving "+path);

            }
        } catch (IOException e) {
            log.error(e.getStackTrace().toString());
        }

    }
    //responds with given string
    public void respondStr(String response, String mime){

        try {

            Headers h = httpExchange.getResponseHeaders();
            h.set("Content-Type", mime);
            h.add("Connection", "close");
            httpExchange.sendResponseHeaders(200, 0);

            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes(),0,response.getBytes().length);
            os.flush();
            os.close();
            log.out("serving response " + response);

        }catch (Exception e){
            log.error("response error "+e.getStackTrace());
        }
    }

    public String getRequest(){

        return httpExchange.getRequestURI().toString();
    }

    public String getParam(String param){
        return paramMap.getParam(param);
    }

    //returns 401 unauthorized
    public void invalidToken(){

        try {

            log.error("Invalid token");
            String response = "401 (Unauthorized)\n";
            httpExchange.sendResponseHeaders(401, response.length());

            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.flush();
            os.close();
        }catch (Exception e){
            log.error("error invalid token "+e.getStackTrace().toString());
        }
    }

    //returns 404 not found
    public void pageNotFound(){

        try {

            log.error("not a file ");
            String response = "404 (Not Found)\n";
            httpExchange.sendResponseHeaders(404, response.length());

            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.flush();
            os.close();
            log.error("Serving 404 response");
        }catch (Exception e){
            log.error("error During page not found "+e.getStackTrace().toString());
        }
    }

    //determines resource type
    protected String getMime(String path){

        String mime = "text/html";

        if (path.substring(path.length() - 3).equals(".js")){
            mime = "application/javascript";
        }

        if (path.substring(path.length() - 3).equals("css")) {
            mime = "text/css";
        }

        return mime;
    }

}
