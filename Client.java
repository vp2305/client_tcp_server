import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        // ClientHandler clientHandler = new ClientHandler();
        // clientHandler.connect("10.0.0.76", 3000);
        Socket s = new Socket("10.0.0.76", 3000);
        DataOutputStream dataStream = new DataOutputStream(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String clientMessage = br.readLine();
            dataStream.writeUTF(clientMessage);
            if (clientMessage.equalsIgnoreCase("break")) {
                break;
            }
        }
        s.close();
        dataStream.close();
    }
}
