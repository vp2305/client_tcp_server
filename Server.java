import java.util.*;
import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 3000;
        ArrayList<PostEntry> entries = new ArrayList<PostEntry>();
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port " + port);
        System.out.println("IP Address for the server is " + InetAddress.getLocalHost().getHostAddress());
        // DataInputStream dataInputStream;
        Socket clientSocket;
        // clientSocket = serverSocket.accept();
        // dataInputStream = new DataInputStream(clientSocket.getInputStream());
        while (true) {
            clientSocket = serverSocket.accept();
            ServerThread thread = new ServerThread(Thread.activeCount() + "", clientSocket);
            thread.start();
            System.out.println("Connection detected, starting server thread [" + (Thread.activeCount() - 1) + "]");
            System.out.println("Active connections: " + (Thread.activeCount() - 1));
            // String clientInputMessage = dataInputStream.readUTF();
            // System.out.println("Client: " + clientInputMessage);
            // if (clientInputMessage.equals("break")) {
            // break;
            // }
        }
        // dataInputStream.close();
        // clientSocket.close();
    }
}
