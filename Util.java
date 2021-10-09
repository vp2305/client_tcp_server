import java.io.*;
import java.net.*;
import java.util.*;

public class Util {
    public static BulletBoard findByLeftCorner(ArrayList<BulletBoard> bulletBoard, String leftCorner) {
        for (BulletBoard postBoard : bulletBoard) {
            if (postBoard.getCOORDINATELLC().equals(leftCorner)) {
                return postBoard;
            }
        }
        return null;
    }

    public static ArrayList<BulletBoard> findByAttribute(ArrayList<BulletBoard> bulletBoards, String attribute,
            String value) {
        ArrayList<BulletBoard> foundSet = new ArrayList<BulletBoard>();
        for (BulletBoard bulletBoard : bulletBoards) {
            switch (attribute) {
                case "COORDINATELLC":
                    if (bulletBoard.getCOORDINATELLC().equals(Double.parseDouble(value))) {
                        foundSet.add(bulletBoard);
                    }
                    break;
                case "HEIGHT":
                    if (bulletBoard.getHEIGHT().equals(Double.parseDouble(value))) {
                        foundSet.add(bulletBoard);
                    }
                    break;
                case "WIDTH":
                    if (bulletBoard.getWIDTH().equals(Double.parseDouble(value))) {
                        foundSet.add(bulletBoard);
                    }
                    break;
                case "COLOR":
                    if (bulletBoard.getCOLOR().equals(value)) {
                        foundSet.add(bulletBoard);
                    }
                    break;
                case "MESSAGE":
                    if (bulletBoard.getMESSAGE().equals(value)) {
                        foundSet.add(bulletBoard);
                    }
                    break;
                case "STATUS":
                    if (bulletBoard.getSTATUS().equals(value)) {
                        foundSet.add(bulletBoard);
                    }
                    break;
                default:
                    break;
            }
        }
        if (foundSet.size() == 0)
            return null;
        return foundSet;
    }

    public static ArrayList<BulletBoard> intersection(ArrayList<ArrayList<BulletBoard>> bulletBoardEntireList) {
        ArrayList<BulletBoard> intersection = null;
        for (ArrayList<BulletBoard> bulletBoard : bulletBoardEntireList) {
            intersection = intersection == null ? bulletBoard : intersection;
            if (intersection == null)
                break;
            if (bulletBoard != null)
                intersection.retainAll(bulletBoard);
            else {
                intersection = null;
                break;
            }
        }
        return intersection;
    }
}
