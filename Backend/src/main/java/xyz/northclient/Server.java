package xyz.northclient;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends WebSocketServer {
    public static class User {
        public String name;
        public String ip;

        public WebSocket webSocket;
        public String minecraftName = null;
        public String serverIp = null;

        public User(String name, String ip,WebSocket webSocket) {
            this.name = name;
            this.ip = ip;
            this.webSocket = webSocket;
        }
    }
    public Server() {
        super(new InetSocketAddress("0.0.0.0",43112));
        client = MongoClients.create("mongodb+srv://tecnessino:dentaparuwa123@glorium.k1hkpqf.mongodb.net/myFirstDatabase");;
        database = client.getDatabase("myFirstDatabase");
    }
    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    public MongoClient client;
    public MongoDatabase database;



    public User getUserByIp(InetSocketAddress address) {
        return users.stream().filter((b) -> b.ip.equalsIgnoreCase(String.valueOf(address))).findFirst().get();
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println(getUserByIp(webSocket.getRemoteSocketAddress()).name + " disconnected/crashed");
        //names.remove(String.valueOf(webSocket.getRemoteSocketAddress()));
        users.removeIf((user) -> user.ip.equalsIgnoreCase(String.valueOf(webSocket.getRemoteSocketAddress())));

    }

    public String sendPacket(WebSocket soc,JsonObject json) {
        String fingerprint = "";
        json.addProperty("fingerprint",fingerprint);
        soc.send(EncryptUtil.encrypt(new Gson().toJson(json)));
        return fingerprint;
    }

    //public HashMap<String,String> names = new HashMap<>();
    public List<User> users = new ArrayList<>();
    public String denyCause = "";

    public boolean canLogin(String username, String hwid, boolean beta) {
        Document checkUsernameDocument = new Document();
        checkUsernameDocument.put("name",username);

        FindIterable<Document> doc1 = database.getCollection("accounts").find(checkUsernameDocument);
        MongoCursor<Document> cursor1 = doc1.iterator();
        Document user = cursor1.hasNext() ? cursor1.next() : null;

        if(hwid.equalsIgnoreCase("c523e940234039aac4f55a52807fe5a1")) {
            denyCause = "refused due to invalid hwid";
            return false;
        }

        if(user == null) {
            denyCause = "Refused due to non exists user";
            System.out.println(username + " refused due to non exists user");
            return false;
        }

        if(((String)user.get("role")).equalsIgnoreCase("User")) {
            denyCause = "Refused due to non having client";
            System.out.println(username + " refused due to non having client");
            return false;
        }

        if(((String)user.get("hwid")).equalsIgnoreCase("")) {
            user.remove("hwid");
            user.put("hwid",hwid);

            BasicDBObject query = new BasicDBObject();
            query.put("name", username);

            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("hwid", hwid);
            BasicDBObject updateObject = new BasicDBObject();
            updateObject.put("$set", newDocument);
            database.getCollection("accounts").updateOne(query, updateObject);
        }

        if(!((String)user.get("hwid")).equalsIgnoreCase(hwid)) {
            denyCause = "Refused due to invalid hwid";
            System.out.println(username + " refused due to invalid hwid");
            //return false;
        }


        if(beta) {
            if(((String)user.get("role")).equalsIgnoreCase("Customer")) {
                denyCause = "Refused due to accessing beta for non beta user";
                System.out.println(username + " refused due to accessing beta for non beta user");
                return false;
            }
        }

        JsonObject banDetails = new Gson().fromJson(user.getString("ban"), JsonObject.class);

        if(banDetails.get("banned").getAsBoolean()) {
            denyCause = "You are banned. Check website for details";
            System.out.println(username + " refused due to ban");
            return false;
        }


        return true;
    }

    public int getUID(String username) {
        Document checkUsernameDocument = new Document();
        checkUsernameDocument.put("name",username);

        FindIterable<Document> doc1 = database.getCollection("accounts").find(checkUsernameDocument);
        MongoCursor<Document> cursor1 = doc1.iterator();
        Document user = cursor1.hasNext() ? cursor1.next() : null;

        if(user == null) {
            return 0;
        }

        return user.getInteger("uid");
    }

    public String getMeta(String username) {
        Document checkUsernameDocument = new Document();
        checkUsernameDocument.put("name",username);

        FindIterable<Document> doc1 = database.getCollection("accounts").find(checkUsernameDocument);
        MongoCursor<Document> cursor1 = doc1.iterator();
        Document user = cursor1.hasNext() ? cursor1.next() : null;

        if(user == null) {
            return "";
        }

        return user.getString("meta");
    }


    public boolean userHasGato(String user) {
        return new Gson().fromJson(getMeta(user), JsonObject.class).get("gatoCape").getAsBoolean();
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        Gson gson = new Gson();
        JsonObject jsonObject = null;
        try {
            jsonObject = gson.fromJson(EncryptUtil.decrypt(s), JsonObject.class);
        }catch (Exception e){
            System.out.println("someone is using old version");
            return;
        }

        if(jsonObject == null)
            return;

        if(jsonObject.get("id").getAsInt() == 0) {

        }

        if(jsonObject.get("id").getAsInt() == 5) {

        }

        if(jsonObject.get("id").getAsInt() == 1) {
            JsonObject res  = new JsonObject();
            denyCause = "";
           // boolean can = canLogin(jsonObject.get("username").getAsString(),jsonObject.get("hwid").getAsString(),jsonObject.get("beta").getAsBoolean());
            boolean can = true;
            res.addProperty("id",2);
            res.addProperty("declined",!can);
            res.addProperty("reason",denyCause);
            res.addProperty("uid",getUID(jsonObject.get("username").getAsString()));
            res.addProperty("meta",getMeta(jsonObject.get("username").getAsString()));
            res.addProperty("marawAutomobile",new Random().nextInt(3000) + 1000);

            if(can) {
                res.addProperty("resFingerprint",jsonObject.get("fingerprint").getAsString());
                users.add(new User(jsonObject.get("username").getAsString(),String.valueOf(webSocket.getRemoteSocketAddress()),webSocket));
            } else {
                res.addProperty("resFingerprint","");
            }
            System.out.println("New user " + jsonObject.get("username").getAsString() + " - " + jsonObject.get("hwid").getAsString());
            sendPacket(webSocket,res);
        }

        if(jsonObject.get("id").getAsInt() == 3) {
            String name = getUserByIp(webSocket.getRemoteSocketAddress()).name;
            System.out.println(name + " - " + jsonObject.get("message").getAsString());

            JsonObject jsonObjectRes = new JsonObject();
            jsonObjectRes.addProperty("id",4);
            jsonObjectRes.addProperty("author",name);
            jsonObjectRes.addProperty("content",jsonObject.get("message").getAsString());
            broadcast(EncryptUtil.encrypt(new Gson().toJson(jsonObjectRes)));
        }

        if(jsonObject.get("id").getAsInt() == 5) {
           try {
               User ref = getUserByIp(webSocket.getRemoteSocketAddress());

               if(!jsonObject.get("ip").getAsString().equalsIgnoreCase("null")) {
                   JsonObject playersOnServer = new JsonObject();
                   JsonArray gatos = new JsonArray();
                   for(User user : users) {
                       if(user.minecraftName != null) {
                           if(user.serverIp.equalsIgnoreCase(jsonObject.get("ip").getAsString())) {
                               playersOnServer.addProperty(user.minecraftName,user.name);
                               if(userHasGato(user.name)) {
                                   gatos.add(new JsonPrimitive(user.minecraftName));
                               }
                               JsonObject toSend = new JsonObject();
                               toSend.addProperty("id",7);
                               toSend.addProperty("minecraftName",jsonObject.get("name").getAsString());
                               toSend.addProperty("clientName",ref.name);
                               toSend.addProperty("action","connect");
                               toSend.addProperty("gato",userHasGato(ref.name));
                               sendPacket(user.webSocket,toSend);
                           }
                       }
                   }

                   JsonObject res = new JsonObject();
                   res.addProperty("id",6);
                   res.add("gatos",gatos);
                   res.add("players",playersOnServer);
                   sendPacket(webSocket,res);
               } else {
                   for(User user : users) {
                       if(user.serverIp.equalsIgnoreCase(ref.serverIp)) {
                           JsonObject toSend = new JsonObject();
                           toSend.addProperty("id",7);
                           toSend.addProperty("minecraftName",ref.minecraftName);
                           toSend.addProperty("clientName",ref.name);
                           toSend.addProperty("action","disconnect");
                           sendPacket(user.webSocket,toSend);
                       }
                   }

               }

               if(!jsonObject.get("ip").getAsString().equalsIgnoreCase("null")) {
                   ref.serverIp = jsonObject.get("ip").getAsString();
                   ref.minecraftName = jsonObject.get("name").getAsString();
               } else {
                   ref.serverIp = null;
                   ref.minecraftName = null;
               }
               System.out.println(ref.name + " " + (jsonObject.get("ip").getAsString().equalsIgnoreCase("null") ? "disconnected from server" : "connected to " + ref.serverIp + " as " + ref.minecraftName));

           }catch (Exception e) {
               e.printStackTrace();
           }
        }

    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {

    }

}
