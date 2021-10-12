import java.io.*;
import java.net.*;
import java.util.*;

public class PostEntry {
    private Double COORDINATELLC = 0.0;
    private Double HEIGHT = 0.0;
    private Double WIDTH = 0.0;
    private String COLOR = "";
    private String MESSAGE = "";
    private String STATUS = "";

    @Override
    public String toString() {
        return "STATUS: " + STATUS + "\nCOORDINATELLC: " + COORDINATELLC + "\nHEIGHT: " + HEIGHT + "\nWIDTH: " + WIDTH
                + "\nCOLOR: " + COLOR + "\nSTATUS: " + STATUS + "\nMESSAGE: " + MESSAGE + "\r\n";
    }

    public Double getCOORDINATELLC() {
        return COORDINATELLC;
    }

    public Double getHEIGHT() {
        return HEIGHT;
    }

    public Double getWIDTH() {
        return WIDTH;
    }

    public String getCOLOR() {
        return COLOR;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setCOORDINATELLC(Double COORDINATELLC) {
        this.COORDINATELLC = COORDINATELLC;
    }

    public void setHEIGHT(Double HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public void setWIDTH(Double WIDTH) {
        this.WIDTH = WIDTH;
    }

    public void setCOLOR(String COLOR) {
        this.COLOR = COLOR;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }
}
