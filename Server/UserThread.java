import java.io.*;
import java.net.*;

public class UserThread extends Thread {

    private final Socket socket;
    private final ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            //Displaying connected users
            printUsers();

            //Reads a new connection (user)
            String userName = reader.readLine();
            
            //Verify access permission before grant it 
                //1. Check DB
                //2. Is the IP released?
                
            server.addUser(userName);
            
            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            //Listener (loop)
            do {
                
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);
                System.out.println(serverMessage);

                //Rules implementations
                //1. Blacklist (Could you do it via API or a external API(integration)? Would you have to verify the language/dialect to prevent some words/expressions?)
                //2. Regex
                //3. Storage the conversation

            } while (!clientMessage.equals("bye"));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " saiu";
            server.broadcast(serverMessage, this);

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }


    private void printUsers() {

        if (server.hasUser()) {
            writer.println("Users connected: " + server.getUserNames());
        } else {
            writer.println("No users connected");
        }

    }

    void sendMessage(String message) {
        writer.println(message);
    }
}