import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class TestServer {

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(8888);
        try {
            while (true) {
                Socket socket = listener.accept();
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("This is a test");
                } finally {
                    socket.close();
                }
            }
        }
        finally {
            listener.close();
        }
    }
}