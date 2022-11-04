package cpen221.mp3.server;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

/**
 * WikiMediatorClient is a client that sends JSON-formatted requests to a WikiMediator Server and receives
 * JSON-formatted replies
 * A new Client is "open" until the close() method is called,
 * at which point it is "closed" and may not be used further.
 */
public class WikiMediatorClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    // AF:
    // This class represents a client which communicates with the WikiMediatorServer via JSON. The socket represents
    // the connection between the client and server. All communications are received on "in" and All queries are sent
    // on "out"

    // RI:
    // all fields are non-null

    /**
     * Make a WikiMediatorClient and connect it to a server running on
     * hostname at the specified port.
     *
     * @throws IOException if can't connect
     */
    public WikiMediatorClient(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    /**
     * Send a request to the server. Requires this is "open".
     *
     * @param toSend a JSON formatted String encoding a request to query the server
     */
    public void sendRequest(String toSend) {
        out.print(toSend + "\n");
        out.flush();
    }

    /**
     * Get a reply from the next request that was submitted.
     * Requires this is "open".
     *
     * @return the requested JSON-formatted response from the server
     * @throws IOException if network or server failure
     */
    public String getReply() throws IOException {
        StringBuilder builder = new StringBuilder();
        do {
            String reply = in.readLine();
            if (reply == null) {
                throw new IOException("connection terminated unexpectedly");
            }
            builder.append(reply);

        } while (!isJSONValid(builder.toString()));

        return builder.toString();
    }

    /**
     * @param jsonInString string to check for JSON validity
     * @return true if valid JSON
     * source: https://stackoverflow.com/questions/10174898/how-to-check-whether-a-given-string-is-valid-json-in-java
     */
    private boolean isJSONValid(String jsonInString) {
        try {
            Gson gson = new Gson();
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    /**
     * Closes the client's connection to the server.
     * This client is now "closed". Requires this is "open".
     *
     * @throws IOException if close fails
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}

