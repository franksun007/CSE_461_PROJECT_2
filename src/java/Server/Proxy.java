package Server;

import Utils.Utilities;
import java.io.PrintStream;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * This is a proxy class that can perform non-connect and connect requests.
 */
public class Proxy {

    public static final int DEFAULT_SERVER_PORT = 12435;
    public static final PrintStream OUTPUT = System.out;

    public static void main(String[] args) {
        if (args.length != 1) {
            Usage();
        }

        int serverPort = DEFAULT_SERVER_PORT;

        try {
            serverPort = Integer.parseInt(args[0]);
            if (serverPort < 1100 || serverPort > 49151) {
                Usage();
            }
        } catch (Exception e) {
            OUTPUT.println("Parsing port error");
            e.printStackTrace();
            System.exit(1);
        }

        ServerSocketChannel mainSocket = null;
        try {
            mainSocket = ServerSocketChannel.open();
            mainSocket.bind(new InetSocketAddress(serverPort));
        } catch (Exception e) {
            OUTPUT.println("Server Socket cannot be created");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            OUTPUT.println(Utilities.getCurrentTime() + " - Proxy listening on "
                    + InetAddress.getLocalHost().getHostAddress() + ":" + serverPort);
        } catch (UnknownHostException e) {
            OUTPUT.println("Cannot get the ip address of the host");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            for ( ; ; ) {
                SocketChannel communicationSocket = mainSocket.accept();
                (new ProxyThread(communicationSocket)).start();
            }
        } catch (Exception e) {
            OUTPUT.println("Unexpected IO Exception");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void Usage() {
        OUTPUT.println("No port specified");
        OUTPUT.println("Port should be in range from 1100 to 49151");
        System.exit(1);
    }
}