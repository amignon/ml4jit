package teste;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Teste2 {

  public static void main(String[] args) {
    try {
      String line;
      String[] cmd = { "python", "test2.py", String.valueOf(-739439859)};
      Process p = Runtime.getRuntime().exec(cmd);
      BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
      while ((line = in.readLine()) != null) {
        System.out.println(line);
      }
      in.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
