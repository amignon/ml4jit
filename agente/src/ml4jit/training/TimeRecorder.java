package ml4jit.training;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ml4jit.Config;


public class TimeRecorder {
  
  private static final Logger log = Logger.getLogger("AGENTE");
  
  private static final String ARQUIVO = ml4jit.Config.PATH + "tempo.dat";
  
  private static final int MAX = 1024 * 10000;
  private static final DataOutputStream arquivo;
  static {
    DataOutputStream tmp = null;
    try {
      tmp = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(ARQUIVO, false), MAX));
    } catch (Exception e) {
      log.warning(e.getMessage());      
    }
    arquivo = tmp;
  }
    
  
  private static final Map<Integer, Integer> dados = new HashMap<Integer, Integer>();
  private static final ArrayList<Integer> ignorar = new ArrayList<Integer>();
  
  public static synchronized void record(int mid, long dif) {
    if (ignorar.contains(mid)) {
      return;
    }
    Integer i = dados.get(mid);
    if (i == null) {
      dados.put(mid, 1);
    }
    else {
      int v = i.intValue();
      dados.put(mid, v + 1);
      if (v >= Config.SAMPLE_SIZE) {
        ignorar.add(mid);
        return;
      }
    }
    try {
      arquivo.writeInt(mid);
      arquivo.writeLong(dif);
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
    DataInputStream dis = new DataInputStream(new FileInputStream(ARQUIVO));
    PrintWriter pw = new PrintWriter(new File (ml4jit.Config.PATH + "tempos.txt"));
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