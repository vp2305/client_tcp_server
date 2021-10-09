import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final ArrayList<BulletBoard> bulletBoards;

    public ServerThread(Socket socket, String name, ArrayList<BulletBoard> bulletBoards) {
        super(name);
        this.socket = socket;
        this.bulletBoards = bulletBoards;
    }

    public synchronized void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            listen();
            disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        String line, inMessage, outMessage;
        try {
            line = in.readLine();
            while (line != null) {
                inMessage = "";
                if (line.equals("ping")) {
                    outMessage = "pong";
                } else {
                    while (!line.contains("\\EOF")) {
                        inMessage = inMessage.concat(line + "\r\n");
                        line = in.readLine();
                    }
                    outMessage = processData(inMessage.split("\n")).trim() + "\r\n\\EOF";
                }
                out.println(outMessage);
                line = in.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disconnect() throws IOException {
        out.close();
        in.close();
        socket.close();
        System.out.println("Server thread [" + getName() + "] disconnected.");
        System.out.println("Active connections: " + (Thread.activeCount() - 2));
        this.interrupt();
    }

    private String processData(String[] data) {
        final String request = data[0].trim();
        switch (request) {
            case "POST":
                return handlePost(data);
            case "GET":
                return handleGet(data);
            case "CLEAR":
                return handleClear(data);
            case "SHAKE":
                return handleShake(data);
            default:
                return "ERROR: Invalid request please do one of [POST, GET, PIN, UNPIN, CLEAR, SHAKE, DISCONNECT]";
        }
    }

    private String handlePost(String[] data) {
        String message;
        BulletBoard bulletBoard = new BulletBoard();
        for (String line : data) {
            line = line.trim();
            String[] words = line.split(" ");
            String value = line.substring(words[0].length()).trim();
            switch (words[0]) {
                case "COORDINATELLC":
                    if (Util.findByLeftCorner(bulletBoards, words[0]) != null) {
                        message = "ERROR: Post already at this coordinates";
                        return message;
                    }
                    bulletBoard.setCOORDINATELLC(Double.parseDouble(words[0]));
                    break;
                case "HEIGHT":
                    bulletBoard.setHEIGHT(Double.parseDouble(value));
                    break;
                case "WIDTH":
                    bulletBoard.setWIDTH(Double.parseDouble(value));
                    break;
                case "COLOR":
                    bulletBoard.setCOLOR(value);
                    break;
                case "MESSAGE":
                    bulletBoard.setMESSAGE(value);
                    break;
                case "STATUS":
                    bulletBoard.setSTATUS(value);
                    break;
                default:
                    break;
            }
        }
        message = "-----Successfully added-----\n" + bulletBoard.toString();
        bulletBoards.add(bulletBoard);
        return message;
    }

    private String handleGet(String[] data) {
        StringBuilder message = new StringBuilder();
        ArrayList<ArrayList<BulletBoard>> bulletBoardEntireList = new ArrayList<>();
        for (String line : data) {
            line = line.trim();
            String[] words = line.split(" ");
            String value = line.substring(words[0].length()).trim();
            switch (words[0]) {
                case "COORDINATELLC":
                    if (value.length() > 0)
                        bulletBoardEntireList.add(Util.findByAttribute(bulletBoards, "COORDINATELLC", value));
                    break;
                case "HEIGHT":
                    if (value.length() > 0)
                        bulletBoardEntireList.add(Util.findByAttribute(bulletBoards, "HEIGHT", value));
                    break;
                case "WIDTH":
                    if (value.length() > 0)
                        bulletBoardEntireList.add(Util.findByAttribute(bulletBoards, "WIDTH", value));
                    break;
                case "COLOR":
                    if (value.length() > 0)
                        bulletBoardEntireList.add(Util.findByAttribute(bulletBoards, "COLOR", value));
                    break;
                case "MESSAGE":
                    if (value.length() > 0)
                        bulletBoardEntireList.add(Util.findByAttribute(bulletBoards, "MESSAGE", value));
                    break;
                case "STATUS":
                    if (value.length() > 0)
                        bulletBoardEntireList.add(Util.findByAttribute(bulletBoards, "STATUS", value));
                    break;
                default:
                    break;
            }
        }
        ArrayList<BulletBoard> intersection = Util.intersection(bulletBoardEntireList);
        if (intersection == null)
            return "No post found.";
        for (BulletBoard bookEntry : intersection) {
            message.append(bookEntry.toString());
            message.append("\r\n");
        }
        return message.toString();
    }

    private String handleClear(String[] data) {
        String message;
        int removedCount = 0;
        ArrayList<ArrayList<BulletBoard>> bulletBoardEntireList = new ArrayList<>();
        for (String line : data) {
            line = line.trim();
            String[] words = line.split(" ");
            String value = line.substring(words[0].length()).trim();
            switch (words[0]) {
                case "COORDINATELLC":
                    if (value.length() > 0)
                        bulletBoardEntireList.add(Util.findByAttribute(bulletBoards, "COORDINATELLC", value));
                    break;
                case "HEIGHT":
                    if (value.length() > 0)
                        bulletBoardEntireList.add(Util.findByAttribute(bulletBoards, "HEIGHT", value));
                    break;
                case "WIDTH":
                    if (value.length() > 0)
                        bulletBoardEntireList.add(Util.findByAttribute(bulletBoards, "WIDTH", value));
                    break;
                case "COLOR":
                    if (value.length() > 0)
                        bulletBoardEntireList.add(Util.findByAttribute(bulletBoards, "COLOR", value));
                    break;
                case "MESSAGE":
                    if (value.length() > 0)
                        bulletBoardEntireList.add(Util.findByAttribute(bulletBoards, "MESSAGE", value));
                    break;
                case "STATUS":
                    if (value.length() > 0)
                        bulletBoardEntireList.add(Util.findByAttribute(bulletBoards, "STATUS", value));
                    break;
                default:
                    break;
            }
        }
        ArrayList<BulletBoard> intersection = Util.intersection(bulletBoardEntireList);
        if (intersection != null) {
            for (BulletBoard bulletBoard : intersection) {
                bulletBoards.remove(bulletBoard);
                removedCount++;
            }
        }
        message = "Removed " + removedCount + " books";
        return message;
    }

    private String handleShake(String[] data) {
        StringBuilder message = new StringBuilder();
        ArrayList<ArrayList<BulletBoard>> bulletBoardEntireList = new ArrayList<>();
        for (String line : data) {
            line = line.trim();
            String[] words = line.split(" ");
            String value = line.substring(words[0].length()).trim();

            if (words[0].equals("Unpinned")) {
                if (value.length() > 0)
                    bulletBoardEntireList.add(Util.findByAttribute(bulletBoards, "STATUS", value));
            }
        }
        ArrayList<BulletBoard> intersection = Util.intersection(bulletBoardEntireList);
        if (intersection == null)
            return "No post found.";
        for (BulletBoard bulletBoard : intersection) {
            message.append(bulletBoard.toString());
            message.append("\r\n");
        }
        return message.toString();
    }
}