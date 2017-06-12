package ml4jit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Armazena uma tabela com o id do método e seu respectivo nome.
 * @author ale
 *
 */
public class Mapeador {

  private static final Logger log = Logger.getLogger("AGENTE");
  private static final HashMap<Integer, String> tabela = new HashMap<Integer, String>();
  private static final HashMap<String, String> caracteristicas = new HashMap<String, String>();

  public static void add(int id, String nome) {
    if (!tabela.containsKey(id))
      tabela.put(id, nome);
  }  
  
  public static String getNomeMetodo(int mid) {
    return tabela.get(mid);
  }
  
  public static void adicionarCaracteristica(Features c) {
    caracteristicas.put(c.getNome(), c.getCaracteristicas());
  }
  
  public static String getCaracteristica(String m) {
    String s = caracteristicas.get(m);
    if (s == null) {
      log.warning("Caracteristicas nao encontrada: " + m);
    }
    return s;
  }
  
  public static void gravar() {
    log.fine("Executando gravar()");
    try {
      FileWriter arquivo = new FileWriter(Config.PATH + "tabela.txt", false);
      Iterator<Map.Entry<Integer, String>> i = tabela.entrySet().iterator();
      while (i.hasNext()) {
        Map.Entry<Integer, String> e = i.next();
        StringBuilder sb = new StringBuilder();
        sb.append(e.getKey())
          .append("|")
          .append(e.getValue())
          .append("\n");
        arquivo.write(sb.toString());
      }
      arquivo.close();
    } catch (Exception e) {
      log.warning(e.getMessage());
    }    
  }
  
  public static void gravarCaracteristicas() {
    gravarCaracteristicas(Config.PATH + "/features.csv");    
  }
  
  
  /*
  public static void gravarCaracteristicas() {
    log.fine("Executando gravarCaracteristicas()");
    try {
      FileWriter arquivo = new FileWriter(Config.CAMINHO + "caracteristicas.csv", false);
      arquivo.write("ID," + Caracteristicas.getCabecalho() + "\n");
      Iterator<Map.Entry<Integer, String>> i = tabela.entrySet().iterator();
      while (i.hasNext()) {
        Map.Entry<Integer, String> e = i.next();        
        StringBuilder sb = new StringBuilder();
        sb.append(e.getKey())
          .append(",")
          .append(caracteristicas.get(e.getValue()))
          .append("\n");
        arquivo.write(sb.toString());
      }
      arquivo.close();
    } catch (Exception e) {
      log.warning(e.getMessage());
    }    
  }
  */
  
  public static void gravarCaracteristicas(String arq) {
    try {
      FileWriter arquivo = new FileWriter(arq, false);
      arquivo.write(Features.getCabecalho() + "\n");
      Iterator<String> it = caracteristicas.values().iterator();
      while (it.hasNext()) {
        arquivo.write(it.next() + "\n");
      }
      arquivo.close();
    } catch (Exception e) {
      log.warning(e.getMessage());
    }    
  }  
  
  public static void adicionarCaracteristica(String n, String c) {
    caracteristicas.put(n, c);
  }
  
  public static void lerCaracteristicas(String arq) {
    log.fine("Lendo caracteristicas");
    try {
      
      BufferedReader reader = new BufferedReader(new FileReader (arq));
      // ignora o cabecalho
      reader.readLine();
      String linha = null;
      while ((linha = reader.readLine()) != null) {
        String s[] = linha.split(",");   
        adicionarCaracteristica(s[1], linha);
      }      
      reader.close();
    } catch (Exception e) {
      log.warning(e.getMessage());
    }    
  }
}
