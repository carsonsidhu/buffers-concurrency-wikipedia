package cpen221.mp3.server;

import cpen221.mp3.wikimediator.WikiMediator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A class presenting a Future which takes a JSON map and returns a JSON-formatted output corresponding to the
 * response to the query
 */
public class WikiMediatorJSONQuery {

    /**
     * gives a Future which returns a JSON-formatted output corresponding to the query
     *
     * @param server   the server which invokes this method
     * @param mediator the wikiMediator which is used to interface with Wikipedia for these queries
     * @param parsed   the parsed map of key-value pairs from the original JSON query
     * @return a Future which returns the JSON-formatted output of the provided query
     */
    public Future<String> execute(WikiMediatorServer server, WikiMediator mediator, Map<String, String> parsed) {
        return Executors.newSingleThreadExecutor().submit(() -> {
            List<String> listOutput;
            String stringOutput;
            String[] output;
            switch (parsed.get("type")) {
                case "search":
                    listOutput = mediator.search(parsed.get("query"), Integer.parseInt(parsed.get("limit")));
                    output = new String[3];
                    output[0] = JSON.formatValue("id", parsed.get("id"));
                    output[1] = JSON.formatValue("status", "success");
                    output[2] = JSON.formatList("response", listOutput);
                    return JSON.assembleExpression(output);

                case "getPage":
                    stringOutput = mediator.getPage(parsed.get("pageTitle"));
                    output = new String[3];
                    output[0] = JSON.formatValue("id", parsed.get("id"));
                    output[1] = JSON.formatValue("status", "success");
                    output[2] = JSON.formatValue("response", stringOutput);
                    return JSON.assembleExpression(output);

                case "peakLoad30s":
                    stringOutput = mediator.peakLoad30s() + "";
                    output = new String[3];
                    output[0] = JSON.formatValue("id", parsed.get("id"));
                    output[1] = JSON.formatValue("status", "success");
                    output[2] = JSON.formatValue("response", stringOutput);
                    return JSON.assembleExpression(output);

                case "zeitgeist":
                    listOutput = mediator.zeitgeist(Integer.parseInt(parsed.get("limit")));
                    output = new String[3];
                    output[0] = JSON.formatValue("id", parsed.get("id"));
                    output[1] = JSON.formatValue("status", "success");
                    output[2] = JSON.formatList("response", listOutput);
                    return JSON.assembleExpression(output);

                case "trending":
                    listOutput = mediator.trending(Integer.parseInt(parsed.get("limit")));
                    output = new String[3];
                    output[0] = JSON.formatValue("id", parsed.get("id"));
                    output[1] = JSON.formatValue("status", "success");
                    output[2] = JSON.formatList("response", listOutput);
                    return JSON.assembleExpression(output);

                case "stop":
                    server.shutdown();
                    output = new String[2];
                    output[0] = JSON.formatValue("id", parsed.get("id"));
                    output[1] = JSON.formatValue("type", "bye");
                    return JSON.assembleExpression(output);
            }
            return null;
        });
    }
}
