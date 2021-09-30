import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private final int port;
    private final Set<String> userNames = new HashSet<>(); //conjunto que não aceita duplicatas
    private final Set<UserThread> userThreads = new HashSet<>();

    public ChatServer(int port) {
        this.port = port;
    }

    //Listening port method
    public void execute() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Online server port: " + port);
            System.out.println("<CTRL+C> to exit");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();
                System.out.println(newUser);
            }

        } catch(IOException ex) {
            System.out.println("Server error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("To execute type: java chatServer <port>");
            System.out.println("Ex.: java chatServer 9000\n");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);
        ChatServer server = new ChatServer(port);
        server.execute();
    }


    public void addUser(String userName) {
        userNames.add(userName);
    }

    public void removeUser(String userName, UserThread userThread) {
        boolean removed = userNames.remove(userName);

        if(removed) {
            userThreads.remove(userThread);
            System.out.println("User: " + userThread + " left");
        }
    }

    boolean hasUser() {
        return !this.userNames.isEmpty();
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    public void broadcast(String serverMessage, UserThread excludeUser) {
        userThreads.stream().filter((aUser) -> (aUser != excludeUser)).forEachOrdered((aUser) -> {
            aUser.sendMessage(serverMessage);
        });
    }

    
    
}
