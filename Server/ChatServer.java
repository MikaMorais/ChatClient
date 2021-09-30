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


    public void addUser(String userName) {
    }

    public void removeUser(String userName, UserThread userThread) {
    }

    public void broadcast(String serverMessage, UserThread userThread) {
    }

    public boolean hasUser() {
        return false;
    }

    public String getUserNames() {
        return null;
    }
    
}
