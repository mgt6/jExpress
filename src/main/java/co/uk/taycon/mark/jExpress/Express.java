package co.uk.taycon.mark.jExpress;

import co.uk.taycon.mark.jExpress.request.Request;
import co.uk.taycon.mark.jExpress.request.StaticRequest;
import co.uk.taycon.mark.jExpress.response.Response;
import co.uk.taycon.mark.jExpress.route.Route;

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

    private ServerSocket listener;

    public void listen(int port) {
        tasks.execute(() -> {
            try  {
                listener = new ServerSocket(port);
                while (listener.isBound()) {
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
                            System.out.println("Error communicating with client:" + e.getMessage());
                        }
                    });
                }
            } catch (IOException e) {
                System.out.println("Sever shut down");
            }
        });
    }

    public void stop() {
        try {
            listener.close();
        } catch (IOException e) {
            System.out.println("Unable to shut down server: " + e.getMessage());
        }
    }

    public void get(String path, BiConsumer<Request, Response> callback) {
        endpointMap.put(new Route("GET", path), callback);
    }

    public void staticResource(String urlPath, String filePath) {
        Route route = new Route("GET", urlPath);
        endpointMap.put(route, StaticRequest.getStatic(filePath));
    }

}
