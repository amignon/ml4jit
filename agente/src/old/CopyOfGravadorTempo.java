package old;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.logging.Logger;


public class CopyOfGravadorTempo {
  
  private static final Logger log = Logger.getLogger("AGENTE");
  
  private static final int MAX = 1024 * 10000;
  private static final DataOutputStream arquivo;
  static {
    DataOutputStream tmp = null;
    try {
      tmp = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("tempo.dat", false), MAX));
    } catch (Exception e) {
      log.warning(e.getMessage());      
    }
    arquivo = tmp;
  }
  
  public static synchronized void add(int mid, long dif) {
    try {
      arquivo.writeInt(mid);
      arquivo.writeLong(dif);
      Produtor.instance().notificar(mid);
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
  }

  public static void finalizar() {
    log.fine("Executando finalizar()");
    try {
      arquivo.flush();
      arquivo.close();
      gerarTxt();
    } catch (Exception e) {
      log.warning(e.getMessage());
    }    
  }
  
  private static void gerarTxt() throws Exception {
    DataInputStream dis = new DataInputStream(new FileInputStream("tempo.dat"));
    PrintWriter pw = new PrintWriter(new File ("tempos.txt"));
    int i = 0;
    while(dis.available() > 0) {
       i++;
       StringBuilder sb = new StringBuilder();
       sb.append(dis.readInt());
       sb.append("|");
       sb.append(dis.readLong());
       sb.append("\n");
       pw.write(sb.toString());
    }
    pw.close();    
    dis.close();
    log.fine("TAM: " + i);
  }
}