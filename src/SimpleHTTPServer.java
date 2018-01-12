import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

// http://javarevisited.blogspot.com/2015/06/how-to-create-http-server-in-java-serversocket-example.html

public class SimpleHTTPServer {
    public static void main(String[] args) throws Exception {
        final ServerSocket server = new ServerSocket(8080);
        System.out.println("Listening for connection on port 8080 ....");
        while (true) {
            try(Socket socket = server.accept()){
                Date today = new Date();
                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
                socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
            }
//            Socket clientSocket = server.accept();
//            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
//            BufferedReader reader = new BufferedReader(isr);
//            String line = reader.readLine();
//            while (!line.isEmpty()) {
//                System.out.println(line);
//                line = reader.readLine();
//            }
        }
    }
}