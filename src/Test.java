import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Test {

    public static void main(String[] args) throws Exception {
        NotesList notesRepo = new NotesList();
        notesRepo.add("hello world");
        notesRepo.add("goodbye world");

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
            Gson gson = new Gson();
            String requestMethod = t.getRequestMethod();
            switch (requestMethod) {
                case "POST":
                    InputStreamReader isr = new InputStreamReader(t.getRequestBody(), "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    int b;
                    StringBuilder buf = new StringBuilder(512);
                    while ((b = br.read()) != -1) {
                        buf.append((char) b);
                    }

                    br.close();
                    isr.close();
                    Note note = gson.fromJson(buf.toString(), Note.class);

                    note = notes.add(note.body);
                    response = gson.toJson(note);
                    break;
                case "DELETE": {
                    // TODO: similar code as GET below - refactor.
                    String url = t.getRequestURI().getPath();
                    String[] xx = url.split("/");
                    if (xx.length == 4) {
                        int id = Integer.parseInt(xx[xx.length - 1]);
                        try {
                            response = gson.toJson(this.notes.delete(id)); // on DELETE I like to send the deleted item to the front end... Standards say it should be empty
                            // and could be a 202 marked for deletion or 204 updated successfully
                        } catch (Exception e) {
                            response = "note not found to delete";
                            responseCode = 404;
                        }
                    } else {
                        //no ID indicated for delete
                        responseCode = 400;
                        response = "Invalid Request";
                    }
                    break;
                }
                case "GET": {
                    String url = t.getRequestURI().getPath();
                    String[] xx = url.split("/");
                    if (xx.length == 4) {
                        int id = Integer.parseInt(xx[xx.length - 1]);
                        try {
                            response = gson.toJson(this.notes.get(id));
                        } catch (Exception e) {
                            response = "note not found";
                            responseCode = 404;
                        }
                    } else {
                        String query = t.getRequestURI().getQuery();
                        if (query != null && query.contains("query")) {
                            Map<String, String> queryMap = queryToMap(query);
                            response = gson.toJson(notes.getFiltered(queryMap.get("query")));
                        } else {
                            response = gson.toJson(notes.get());
                        }
                    }
                    break;
                }
                case "PUT": {
                    InputStreamReader isr2 = new InputStreamReader(t.getRequestBody(), "utf-8");
                    BufferedReader br2 = new BufferedReader(isr2);
                    int b2;
                    StringBuilder buf2 = new StringBuilder(512);
                    while ((b2 = br2.read()) != -1) {
                        buf2.append((char) b2);
                    }
                    br2.close();
                    isr2.close();
                    Note note2 = gson.fromJson(buf2.toString(), Note.class);

                    try {
                        note2 = notes.update(note2);
                        response = gson.toJson(note2);
                    } catch (Exception e){
                        response = "note not found";
                        responseCode = 404;
                    }
                    break;
                }
                default: {
                    responseCode = 400;
                    response = requestMethod + " not supported.";
                }
            }
            t.sendResponseHeaders(responseCode, response.getBytes("UTF-8").length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

    }

    static Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length>1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }
}