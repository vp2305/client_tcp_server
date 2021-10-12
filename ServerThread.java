import java.util.*;
import java.net.*;
import java.io.*;

public class ServerThread extends Thread {
    private final Socket s;
    private DataInputStream in;
    private BufferedReader out;

    public ServerThread(String name, Socket socket) {
        super(name);
        this.s = socket;
    }

    public synchronized void run() {
        try {
            in = new DataInputStream(s.getInputStream());
            out = new BufferedReader(new InputStreamReader(System.in));
            listen();
            disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        String line, inMessage, outMessage;
        try {
            line = in.readUTF();
            while (line != null) {
                inMessage = "";
                if (line.equals("ping")) {
                    outMessage = "pong";
                } else {
                    while (!line.contains("\\EOF")) {
                        System.out.println(line);
                        inMessage = inMessage.concat(line + "\r\n");
                        System.out.println("In Message: " + inMessage);
                        line = in.readUTF();
                        System.out.println("Line afdter: " + line);
                    }
                    // outMessage = processData(inMessage.split("\n")).trim() + "\r\n\\EOF";
                }
                // out.println(outMessage);
                line = in.readUTF();
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disconnect() throws IOException {
        System.out.println("Disconnect");
        out.close();
        in.close();
        s.close();
        System.out.println("Server thread [" + getName() + "] disconnected.");
        System.out.println("Active connections: " + (Thread.activeCount() - 2));
        this.interrupt();
    }
}
