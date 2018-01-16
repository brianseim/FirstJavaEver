import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Test {

    public static void main(String[] args) throws Exception {
        NotesList notesRepo = new NotesList();
        notesRepo.AddNote("hello world");
        notesRepo.AddNote("goodbye world");

        HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
        server.createContext("/api", new MyApiRootHandler());
        server.createContext("/api/notes", new NotesListHandler(notesRepo));
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyApiRootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the API root handler";
            t.sendResponseHeaders(200, response.getBytes("UTF-8").length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class NotesListHandler implements HttpHandler {
        NotesList notes;
        NotesListHandler(NotesList notesRepo){
            this.notes = notesRepo;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response;
            int responseCode = 200;
            if(t.getRequestMethod().equals("POST")) {

                response = "POST";
            } else {
                String url = t.getRequestURI().toString();
                String[] xx = url.split("/");
                if (xx.length == 4) {
                    int id = Integer.parseInt(xx[xx.length - 1]);
                    response = this.notes.NoteJsonOut(id);
                    if (response.length() == 0) responseCode = 404;
                } else {
                    response = notes.JsonOut();
                }
            }
            t.sendResponseHeaders(responseCode, response.getBytes("UTF-8").length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}