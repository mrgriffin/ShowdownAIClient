/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package showdownaiclient;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;


/**
 *
 * @author Scott
 */
public class PSDAISocket extends WebSocketClient {
    private String challstr;
    private String assertion;
    private String username;
    private boolean autoaccept;
    private boolean roomcode;
    private String croom;
    //opcode headers go here
    final String DELIMITER = "|";
    final String SERVER_MESSAGE = ";;";
    final String ROOM_HEADER = ">";
    final String INPUT_CHALLSTR = "|challstr|";
    final String INPUT_NAMETAKEN = "|nametaken|";
    final String INPUT_LOGINSUCCESS = "|updateuser|";
    final String INPUT_CHALLENGE = "|updatechallenges|";
    final String INPUT_FORMATLIST = "|formats|";
    final String OUTPUT_JOIN = "|/join ";
    final String OUTPUT_CHALLENGE = "|/challenge ";
    final String OUTPUT_ACCEPT = "|/accept ";
    final String OUTPUT_REJECT = "|/reject ";
    final String COMMAND_LOGIN = "AILogin";
    final String COMMAND_JOIN = "AIJoin";
    final String COMMAND_HELP = "AIHelp";
    final String COMMAND_CHALLENGE = "AIChallenge";
    final String COMMAND_ACCEPT = "AIAccept";
    final String COMMAND_REJECT = "AIReject";
    final String COMMAND_ACCEPTALL = "AIAcceptAll";
    final String COMMAND_SEARCH = "AISearch";
    private ArrayList<Format> formats;
    private HashMap<String, Room> battles;
    
    public PSDAISocket(URI uri){
        super(uri);
        challstr = "";
        assertion = "";
        username = "";
        autoaccept = false;
        formats = new ArrayList<Format>();
        battles = new HashMap<String, Room>();
        roomcode = false;
        croom = "";
    }
    
    @Override
    public void onOpen(ServerHandshake shs){
        System.out.println("Connection open");    
        System.out.println("Enter AIHelp for usable commands");
    }
    @Override
    public void onMessage(String string) {
        Scanner scanner = new Scanner(string);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(roomcode){
                battles.get(croom).process(line);
            }
            else if(parseinput(line)){
                System.out.println(line); 
            }
        }
        roomcode = false;
    }

    @Override
    public void onClose(int i, String string, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onError(Exception excptn) {
        System.out.println(excptn); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void send(String s){
        if(parseoutput(s)){
            super.send(s);
        }
    }
    
    private boolean challstr(String input){
        input = input.replace(INPUT_CHALLSTR, "");
            challstr = input;
            //System.out.println("Challstr recorded: " + challstr);
            return false;
    }
    
    private boolean nametaken(String input){
        System.out.println("Authentication failed.  Could not login.  Please try again.");
        return false;
    }
    
    private boolean loginsuccess(String input){
        input = input.replace("|updateuser|", "");
            input = input.substring(0, input.indexOf("|"));
            username = input;
            System.out.println("You have successfully logged in as " + username);
            send(OUTPUT_JOIN + "lobby");
            return false;
    }
    
    private boolean inchallenge(String input){
        try{
                String jstring = input.substring(INPUT_CHALLENGE.length());
                JSONObject jsono = new JSONObject(jstring);
                JSONObject challenges = jsono.getJSONObject("challengesFrom");
                String[] challengers = JSONObject.getNames(challenges);
                if(challengers != null && challengers.length >= 1){
                    System.out.println("You have been challenged to the following:");
                    for(int i = 0; i < challengers.length; i++){
                        System.out.println(challengers[i] + " has challenged you to: "
                                + challenges.getString(challengers[i]));
                        if(autoaccept){
                            send(OUTPUT_ACCEPT + challengers[i]);
                        }
                    }
                }
                if(jsono.isNull("challengeTo")==false){
                    JSONObject challengesto = jsono.getJSONObject("challengeTo");
                    System.out.println("You have challenged "+
                            challengesto.getString("to") + " to a " +
                            challengesto.getString("format"));
                }
            }
            catch(Exception e){
                System.out.println(e);
            }
            return false;
    }
    
    private boolean formatlist(String input){
        if(formats.size() == 0){
                input = input.substring(INPUT_FORMATLIST.length() + 1);
                input = input.substring(input.indexOf("|")+ 1);
                while(input.contains("|")){
                    String stuff = input.substring(0, input.indexOf("|")+ 1);
                    input = input.substring(stuff.length());
                    if(stuff.startsWith(",")){
                        stuff = input.substring(0, input.indexOf("|") + 1);
                        input = input.substring(stuff.length());
                        Format a = new Format(stuff.substring(0, stuff.length() - 1), 'a');
                        formats.add(a);
                        
                        
                    }
                    else{
                        String fname = stuff.substring(0, stuff.indexOf(","));
                        char category = stuff.charAt(stuff.indexOf(",")+1);
                        Format a = new Format(fname, category);
                        formats.add(a);
                    }
                    
                }
                formats.add(new Format(input.substring(0, input.indexOf(",")),
                        input.charAt(input.indexOf(",")+1)));
                
            }
            return false;
    }
    
    private boolean processroom(String input){
        String roomname = input.substring(1);
        roomcode = true;
        if(battles.containsKey(roomname) == false){
            battles.put(roomname, new Room(roomname, username));
            System.out.println("Room " + roomname + " has been processed");
        }
        croom = roomname;
        return false;
    }
    
    private boolean parseinput(String input){
        if(input.startsWith(INPUT_CHALLSTR)){
            return challstr(input);
        }
        else if(input.startsWith(ROOM_HEADER)){
            return processroom(input);
        }
        else if(input.startsWith(INPUT_NAMETAKEN)){
            return nametaken(input);
        }
        else if(input.startsWith(INPUT_LOGINSUCCESS)){
            return loginsuccess(input);
        }
        else if(input.startsWith(INPUT_CHALLENGE)){
            return inchallenge(input);
        }
        else if(input.startsWith(INPUT_FORMATLIST)){
            return formatlist(input);
        }
        else if(input.startsWith(";;")){
            return false;
        }
        return true;
    }
    
    private boolean login(String output){
        if(username.startsWith("Guest")){
                System.out.println("Enter your information.");
                Scanner in = new Scanner(System.in);
                String uname = in.nextLine();
                String pw = in.nextLine();
                signin(uname, pw);
                return false;
            }
        else{
            System.out.println("You are already logged in as " + username);
            return false;
        }
        
    }
    
    private boolean joinroom(String output){
        System.out.println("Which room would you like to join?");
        Scanner in = new Scanner(System.in);
        String room = in.nextLine();
        send(OUTPUT_JOIN + room);
        return false;
    }
    
    private boolean issuechallenge(String output){
        System.out.println("Who would you like to challenge?");
        Scanner in = new Scanner(System.in);
        String opponent = in.nextLine();
        System.out.println("What format?");
        String format = in.nextLine();
        send(OUTPUT_CHALLENGE + opponent + "," + format);
        return false;
    }
    
    private boolean showhelp(String output){
        System.out.println("Here are the commands you can use:");
        System.out.println(COMMAND_LOGIN + " logs you in");
        System.out.println(COMMAND_JOIN + " lets you join a room");
        System.out.println(COMMAND_CHALLENGE + " lets you challenge another user"); //NYI
        System.out.println(COMMAND_ACCEPT + " lets you accept a challenge"); //NYI
        System.out.println(COMMAND_REJECT + " lets you reject a challenge"); //NYI
        System.out.println(COMMAND_ACCEPTALL + " automatically accepts challenges"); //NYI
        System.out.println(COMMAND_SEARCH + " finds an opponent through MM"); //NYI
        return false;
    }
    
    private boolean acceptchallenge(String output){
        System.out.println("Whose challenge are you accepting?");
        Scanner in = new Scanner(System.in);
        String opponent = in.nextLine();
        send(OUTPUT_ACCEPT + opponent);
        return false;
    }
    
    private boolean rejectchallenge(String output){
        System.out.println("Whose challenge are you rejecting?");
        Scanner in = new Scanner(System.in);
        String opponent = in.nextLine();
        send(OUTPUT_REJECT + opponent);
        return false;
    }
    
    private boolean parseoutput(String output){
        if(output.equals(COMMAND_LOGIN)){
            return login(output);
        }
        else if(output.equals(COMMAND_JOIN)){
            return joinroom(output);
        }
        else if(output.equals(COMMAND_CHALLENGE)){
            return issuechallenge(output);                    
        }
        else if(output.equals(COMMAND_ACCEPT)){
            return(acceptchallenge(output));
        }
        else if(output.equals(COMMAND_REJECT)){
            
        }
        else if(output.startsWith(COMMAND_ACCEPTALL)){
            autoaccept = !autoaccept;
            System.out.println("Autoaccept is now set to: " + autoaccept);
            return false;
        }
        else if(output.equals(COMMAND_SEARCH)){
            
        }
        else if(output.equals(COMMAND_HELP)){
            return showhelp(output);
        }
        return true;
    }
    
    private void signin(String uname, String password){
        try{
            String postData = "act=login&name=" + uname +
                    "&pass=" + password + 
                    "&challstr=" + challstr; 
            URL url = new URL("http://play.pokemonshowdown.com/action.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; "
                    + "WOW64) AppleWebKit/537.11 (KHTML, "
                    + "like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());

                // Send request
            outStream.writeBytes(postData);
            outStream.flush();
            outStream.close();

            InputStream inputStream = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream)));

            String output = bufferedReader.readLine();
            
            inputStream.close();
            assertion = output.substring(1);
            JSONObject jsono = new JSONObject(assertion);
            assertion = (String) jsono.get("assertion");
            send("|/trn " + uname + ",0," + assertion);
            
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    
}
