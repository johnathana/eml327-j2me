package com.github.johnathana.elm327.obdcommands;

import com.sun.cldc.util.j2me.CalendarImpl;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by johnathana on 5/19/16.
 */
public class ObdUtils {

    public static int parseHexByte(final String hexString, int index) {
        int byteId = (index * 2);
        return Integer.parseInt(hexString.substring(byteId, byteId + 2), 16);
    }

    public static int parseValue(final String rawData) {
        int A = parseHexByte(rawData, 2);
        int B = parseHexByte(rawData, 3);
        return (A * 256 + B);
    }

    public static String parseTemp(final String rawData) {
        int A = parseHexByte(rawData, 2);
        return Integer.toString(A - 40) + " °C";
    }

    public static String parsePercentage(final String rawData) {
        int perc = (int)(parseHexByte(rawData, 2) * 0.39f);
        return Integer.toString(perc) + "%";
    }

    public static String formatSecondsAsDuration(long seconds) {
        long hour = seconds / 60 / 60;
        long min = (seconds / 60) - (hour * 60);
        long sec = seconds - (min * 60) - (hour * 60 * 60);

        return (hour < 10 ? "0" : "") + hour + ":" +
                (min < 10 ? "0" : "") + min + ":" +
                (sec < 10 ? "0" : "") + sec;
    }

    public static FileConnection getLogFileConnection() throws IOException {
        FileConnection fileConnection = (FileConnection)
                Connector.open("file:///SDCard/" + ObdUtils.getLogFilename(), Connector.WRITE);

        if(!fileConnection.exists()) {
            fileConnection.create();
        }

        return fileConnection;
    }

    private static String getLogFilename() {
        Calendar c = Calendar.getInstance();
        return CalendarImpl.toISO8601String(c).replace(' ', '_').replace('+', '_') + ".log";
    }

}
