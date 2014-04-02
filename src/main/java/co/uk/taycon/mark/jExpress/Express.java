package co.uk.taycon.mark.jExpress;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

public class Express {

    private Map<Route, BiConsumer<Request, Response>> endpointMap = new ConcurrentHashMap<>();

    private static final int POOL_SIZE = 50 * Runtime.getRuntime().availableProcessors();

    private ExecutorService tasks = Executors.newFixedThreadPool(POOL_SIZE);

    private boolean listening;

    public void listen(int port) {
        listening = true;
        tasks.execute(() -> {
            try (ServerSocket listener = new ServerSocket(port)) {
                while (listening) {
                    Socket socket = listener.accept();
                    tasks.execute(() -> {
                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                            List<String> inputLines = new ArrayList<>();
                            String inputLine;
                            while ((inputLine = in.readLine()) != null) {
                                if (inputLine.equals("")) {
                                    break;
                                }
                                inputLines.add(inputLine);
                            }

                            if(!inputLines.isEmpty()) {
                                Route route = new Route(inputLines.get(0));
                                Request req = new Request(inputLines);
                                Response res = new Response(out);
                                if (endpointMap.containsKey(route)) {
                                    endpointMap.get(route).accept(req, res);
                                } else {
                                    res.send(404);
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void stop() {
        listening = false;
        tasks.shutdown();
    }

    public synchronized void get(String path, BiConsumer<Request, Response> callback) {
        endpointMap.put(new Route("GET", path), callback);
    }

}
