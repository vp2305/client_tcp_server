import java.io.*;
import java.net.*;

public class ClientHandler {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void connect(String address, int port) throws IOException {
        clientSocket = new Socket();
        try {
            clientSocket.connect(new InetSocketAddress(address, port), 3000);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new IOException("ERROR: Connection refused please check IP Address and Port and try again");
        }
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    private String processRequest(Request request, String STATUS, Double COORDINATELLC, Double HEIGHT, Double WIDTH,
            String COLOR, String MESSAGE) {
        String requestData = request.name() + "\r\n";
        requestData += "STATUS: " + STATUS + "\nCOORDINATELLC: " + COORDINATELLC + "\nHEIGHT: " + HEIGHT + "\nWIDTH: "
                + WIDTH + "\nCOLOR: " + COLOR + "\nSTATUS: " + STATUS + "\nMESSAGE: " + MESSAGE + "\r\n";
        return requestData;
    }

    public String sendMessage(Request request, String STATUS, Double COORDINATELLC, Double HEIGHT, Double WIDTH,
            String COLOR, String MESSAGE) throws IOException {
        String requestData = processRequest(request, STATUS, COORDINATELLC, HEIGHT, WIDTH, COLOR, MESSAGE);
        out.println(requestData + "\r\n\\EOF");
        String response = "";
        String line = in.readLine();
        while (line != null && !line.contains("\\EOF")) {
            response = response.concat(line + "\r\n");
            line = in.readLine();
        }
        return response;
    }

    public boolean isConnected() {
        try {
            out.println("ping");
            return in.readLine().equals("pong");
        } catch (NullPointerException | IOException e) {
            return false;
        }
    }

}
