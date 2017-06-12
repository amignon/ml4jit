package ml4jit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {
  
  public static String processar(String carac) {
    String hostName = "localhost";
    int portNumber = 10002;

    try {
        Socket s = new Socket(hostName, portNumber);
        //DataOutputStream out = new DataOutputStream(s.getOutputStream());
        //out.writeLong(1000);
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out.println(carac);
        String r = in.readLine();
        s.close();
        return r;
    } catch (Exception e) {
      System.out.println ("Erro Socket: " + e);
    }
    return null;
  }
}
