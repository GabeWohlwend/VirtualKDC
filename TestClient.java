import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;


public class TestClient {

   public static void main(String[] args) throws IOException {
      Scanner keyboardIn = new Scanner(System.in);
      System.out.println("Enter IP of server");
      String serverAddress = keyboardIn.nextLine();        
      Socket s = new Socket(serverAddress, 8888);
      BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
      System.out.println(input.readLine());
    }
}