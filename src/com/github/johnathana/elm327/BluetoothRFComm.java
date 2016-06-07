package com.github.johnathana.elm327;

import com.github.johnathana.elm327.obdcommands.ObdUtils;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.file.FileConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by johnathana on 5/17/16.
 */
public class BluetoothRFComm {

    private StreamConnection connection;

    private InputStream in;
    private OutputStream out;

    private static final BluetoothRFComm instance;

    static {
        instance = new BluetoothRFComm();
    }

    public static BluetoothRFComm getInstance() {
        return instance;
    }

    private BluetoothRFComm() {}

    public void connect(final String macAddress) {

        try {
            String url = "btspp://" + macAddress + ":1";
            connection = (StreamConnection) Connector.open(url);

            in = connection.openInputStream();
            out = connection.openOutputStream();

            String[] commands = {"ATD", "ATZ", "ATE0", "ATL0", "ATS0", "ATH0", "ATSP0"};
            executeCommands(commands);

        } catch (IOException ignored) {}
    }

    public String executeCommand(final String command) throws IOException {
        sendCommand(command);
        return (getResults());
    }

    public void executeCommands(String[] commands) throws IOException {
        for (int i = 0; i < commands.length; i++) {
            executeCommand(commands[i]);
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (IOException ignored) {}
    }

    private void sendCommand(final String cmd) throws IOException {
        out.write((cmd + "\r").getBytes());
        out.flush();
    }

    private String getResults() throws IOException {

        StringBuffer res = new StringBuffer();

        char c;
        while (((c = (char) in.read()) > -1)) {
            if (c == '>') {
                break;
            }
            res.append(c);
        }

        return res.toString();
    }

    public void startMonitor() {

        try {
            String[] commands = {"ATL1", "ATH1", "ATS1", "ATAL"};
            executeCommands(commands);

            sendCommand("ATMA");

            FileConnection fileConnection = ObdUtils.getLogFileConnection();

            PrintStream printStream = new PrintStream(fileConnection.openOutputStream());

            StringBuffer res = new StringBuffer();
            char c;
            while (((c = (char) in.read()) > -1)) {
                if (c == '\n' || c == '\r') {
                    printStream.println(res.toString());
                    res.setLength(0);
                } else if (c == '>') {
                    break;
                }
                res.append(c);
            }

            printStream.close();
            fileConnection.close();

        } catch (Exception ignored) {
        }
    }

}
