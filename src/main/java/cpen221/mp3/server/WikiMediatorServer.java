package cpen221.mp3.server;

import cpen221.mp3.wikimediator.WikiMediator;
import cpen221.mp3.wikimediator.WikiMediatorSave;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;

/**
 * A server which takes JSON-formatted queries on a specified port, parses them, executes them, and returns a JSON-formatted response.
 * Valid commands include "search", "getPage", "peakLoad30s", "zeitgeist", "trending", and "stop".
 */
public class WikiMediatorServer {

    // TODO: fix file path
    public static final String SAVE_LOCATION = "local/wikiMediatorMemory.ser";
    public static final int WIKIMEDIATOR_PORT = 4949;
    private ServerSocket serverSocket;
    private WikiMediator mediator;
    private boolean shutdown;
    private int clients = 0;
    private int n;

    // AF:
    // This class represents a server which communicates with up to n different clients via JSON. The socket represents
    // the connection between the client and server. "clients" is the number of clients presently connected to the
    // server. Shutdown indicates the intent for the server to shutdown. All queries to Wikipedia are done via
    // the mediator.

    // RI:
    // all fields are non-null

    /**
     * Start a server at a given port number, with the ability to process
     * upto n requests concurrently. Also attempts to load saved data from /local is available.
     * If not it starts fresh
     *
     * @param port the port number to bind the server to
     * @param n    the number of clients which can be handles concurrently
     */
    public WikiMediatorServer(int port, int n) {
        shutdown = false;
        this.n = n;
        try {
            serverSocket = new ServerSocket(port);
            mediator = startUp();
            serve();
        } catch (FileNotFoundException fnfe) {
            mediator = new WikiMediator();
        } catch (ClassNotFoundException cnfe) {
            mediator = new WikiMediator();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connects incoming clients up to the maximum, and blocks additional incoming connections until bandwidth
     * is available. Then launches each client in parallel to handle its incoming queries
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void serve() throws IOException, InterruptedException {
        while (!shutdown) {
            while (clients >= n) {
                Thread.sleep(50);
            }

            final Socket socket = serverSocket.accept();
            clients++;
            // block until a client connects

            // create a new thread to handle that client
            Thread handler = new Thread(new Runnable() {
                public void run() {
                    try {
                        try {
                            handle(socket);
                        } finally {
                            socket.close();
                            clients--;
                        }
                    } catch (IOException ioe) {
                        // this exception wouldn't terminate serve(),
                        // since we're now on a different thread, but
                        // we still need to handle it
                        ioe.printStackTrace();
                    }
                }
            });
            // start the thread
            handler.start();
        }
    }

    /**
     * Handle one client connection. Returns when client disconnects.
     *
     * @param socket socket where client is connected
     * @throws IOException if connection encounters an error
     */
    private void handle(Socket socket) throws IOException {
        System.err.println("client connected");

        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

        PrintWriter out = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream()), true);

        try {
            StringBuilder builder = new StringBuilder();
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                do {
                    builder.append(line);
                    line = in.readLine();
                } while (!line.contains("}"));
                builder.append(line);
                Map<String, String> parsed = JSON.parseJSON(builder.toString());
                builder = new StringBuilder();

                try {
                    Future<String> deleteTime = new WikiMediatorJSONQuery().execute(this, mediator, parsed);
                    String output;
                    if (parsed.containsKey("timeout")) {
                        output = deleteTime.get(Long.parseLong(parsed.get("timeout")), TimeUnit.SECONDS);
                    } else {
                        output = deleteTime.get();
                    }
                    out.println(output);
                } catch (TimeoutException e) {
                    String[] output = new String[3];
                    output[0] = JSON.formatValue("id", parsed.get("id"));
                    output[1] = JSON.formatValue("status", "failed");
                    output[2] = JSON.formatValue("response", "Operation timed out");
                    out.println(JSON.assembleExpression(output));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            out.close();
            in.close();
        }
    }

    /**
     * Writes current statistical state to file and flags the server to shutdown without taking any more clients.
     * Shutdown allows any already active clients to finish their queries
     *
     * @throws IOException
     */
    public void shutdown() throws IOException {
        new ObjectOutputStream(new FileOutputStream(SAVE_LOCATION)).writeObject(this.mediator.getSave());
        shutdown = true;
    }

    /**
     * Loads data from file into the server
     *
     * @return a WikiMediator containing data read from disk
     * @throws IOException            cannot communicate with file
     * @throws ClassNotFoundException cannot deserialize appropriate object from file
     */
    private WikiMediator startUp() throws IOException, ClassNotFoundException {
        return new WikiMediator((WikiMediatorSave) new ObjectInputStream(new FileInputStream(SAVE_LOCATION)).readObject());
    }
}
