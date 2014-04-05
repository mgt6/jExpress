package co.uk.taycon.mark.jExpress;

import java.io.PrintWriter;
import java.time.LocalDateTime;

public class Response {

    private PrintWriter out;

    public Response(PrintWriter out) {
        this.out = out;
    }

    public void send(String message) {
        out.println("HTTP/1.1 200 OK");
        finaliseSend(message);
    }

    public void send(int code) {
        out.println("HTTP/1.1 " + code);
        finaliseSend("");
    }

    public void send(int code, String message) {
        out.println("HTTP/1.1 " + code);
        finaliseSend(message);
    }

    private void finaliseSend(String message) {
        out.println("Date: " +  LocalDateTime.now().toString());
        out.println("Server: jExpress 0.0.1");
        out.println("Content-Type: text/html");
        out.println();
        out.println(message);
        out.close();
    }
}
