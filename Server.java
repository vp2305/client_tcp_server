import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException {
        int port;
        if (args.length == 0) {
            port = 3000;
        } else {
            port = Integer.parseInt(args[0]);
        }
        ArrayList<BulletBoard> bulletBoards = new ArrayList<BulletBoard>();
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is running on port: " + port);
        System.out.println("IP Address: " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("Waiting for people to join...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            ServerThread thread = new ServerThread(clientSocket, Thread.activeCount() + "", bulletBoards);
        }
    }
}