package ml4jit.counter;

import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Armazena a quantidade de execucoes de cada metodo.
 * 
 * E chamado pelos metodos que foram instrumentalizados com a opcao de contagem de chamadas.
 * 
 * Grava os dados ao final da execucao em um arquivo texto.
 *  
 * @author ale
 *
 */
public class Counter {
  
  private static final Logger log = Logger.getLogger("AGENTE");
  
  private static long contador = 0;
  private static final Map<Integer, String> tabela = new HashMap<Integer, String>();  
  private static final HashMap<Integer, Long> execucoes = new HashMap<Integer, Long>();
  
  private static final String ARQUIVO = ml4jit.Config.PATH + "counter.txt";
    
  /**
   * Adiciona o mapeamento id-nome_metodo
   * @param id Hashcode representando o id do metodo
   * @param nome Nome do metodo
   */
  public static void addId (int id, String nome) {
    if (!tabela.containsKey(id)) {
      tabela.put(id, nome);
    }
  }  
  
  public static synchronized void record (int id) {
    contador++;    
    Long i = execucoes.get(id);
    if (i == null) {
      execucoes.put(id, 1L);
    }
    else {
      execucoes.put(id, i.longValue() + 1);
    }
    if (contador % 10000 == 0) {
      log.fine("Counter: " + contador);
    }
  } 
  
  @SuppressWarnings("rawtypes")
  public static synchronized void end() {
    log.fine("Executando Contador.finalizar()");
    try {
      PrintWriter pw = new PrintWriter(new File (ARQUIVO));
      Iterator it = sortByValues(execucoes).entrySet().iterator();
      
      DecimalFormat df = new DecimalFormat();
      StringBuilder sb = new StringBuilder();
      sb.append("*** METHODS: ")
        .append(execucoes.entrySet().size())
        .append(" TOTAL: ")
        .append(df.format(contador))
        .append(" ***\n");      
      pw.write(sb.toString());
      
      while (it.hasNext()) {
        Map.Entry e = (Map.Entry) it.next();
        //String n = tabela.get(e.getKey());
        StringBuilder s = new StringBuilder();
        s.append(String.format("%1$11d", e.getKey()))
          .append("|")
          .append(String.format("%1$-130s",tabela.get(e.getKey())))
          .append("|")
          .append(df.format(e.getValue()))
          .append("\n");
        pw.write(s.toString());
      }
      pw.close();
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
  }
  
  @SuppressWarnings("all")
  private static HashMap sortByValues(HashMap map) { 
    List list = new LinkedList(map.entrySet());
    // Defined Custom Comparator here
    Collections.sort(list, new Comparator() {         
        public int compare(Object o1, Object o2) {
            return ((Comparable) ((Map.Entry) (o1)).getValue())
               .compareTo(((Map.Entry) (o2)).getValue());
         }
    });

    // Here I am copying the sorted list in HashMap
    // using LinkedHashMap to preserve the insertion order
    HashMap sortedHashMap = new LinkedHashMap();
    for (Iterator it = list.iterator(); it.hasNext();) {
           Map.Entry entry = (Map.Entry) it.next();
           sortedHashMap.put(entry.getKey(), entry.getValue());
    } 
    return sortedHashMap;
  }
}
