package teste;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ml4jit.SocketClient;


public class TesteSocket {
  
  public static void main (String [] args) {
    String s = "2121695142,teste.Main.f1(),9,0,0,0,0,0,0,1,0.0,0.0,0.0,0.0,0.0,0.0,0.11111111,0.11111111,0.0,0.0,0.0,0.0,0.0,0.0";
    System.out.println(SocketClient.processar(s));
    System.out.println(SocketClient.processar(s));
    System.out.println(SocketClient.processar(s));
    System.out.println(SocketClient.processar("EXIT"));
    
    /*
    String hostName = "localhost";
    int portNumber = 10002;

    try {
        Socket s = new Socket(hostName, portNumber);
        //DataOutputStream out = new DataOutputStream(s.getOutputStream());
        //out.writeLong(1000);
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out.println("2121695142,teste.Main.f1(),9,0,0,0,0,0,0,1,0.0,0.0,0.0,0.0,0.0,0.0,0.11111111,0.11111111,0.0,0.0,0.0,0.0,0.0,0.0");
        System.out.println ("Retorno: " + in.readLine());        
        
        out.println("2121695142,teste.Main.f1(),9,0,0,0,0,0,0,1,0.0,0.0,0.0,0.0,0.0,0.0,0.11111111,0.11111111,0.0,0.0,0.0,0.0,0.0,0.0");
        System.out.println ("Retorno: " + in.readLine());
        
        out.println("2121695142,teste.Main.f1(),9,0,0,0,0,0,0,1,0.0,0.0,0.0,0.0,0.0,0.0,0.11111111,0.11111111,0.0,0.0,0.0,0.0,0.0,0.0");
        System.out.println ("Retorno: " + in.readLine());
        
        s.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    */
  }
}
