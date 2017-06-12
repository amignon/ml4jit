package teste;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

public class Leitor {
  public static void main (String [] args) throws Exception {
    String caminho = "/home/ale/Dacapo/";    
    DataInputStream dis = new DataInputStream(new FileInputStream(caminho + "tempo.dat"));
    PrintWriter pw = new PrintWriter(new File (caminho + "tempos.txt"));
    int i = 0;    
    System.out.println ("DIS: " + dis.available());
    while(dis.available()>0) {
       i++;
       int id = dis.readInt();
       pw.write(id + "\n");
    }
    pw.close();
    System.out.println ("TAM: " + i);
    dis.close();
  }

}
