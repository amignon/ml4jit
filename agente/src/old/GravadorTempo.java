package old;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ml4jit.Recompilador;
import ml4jit.SelecionadorAgente;
import ml4jit.Tempo;

public class GravadorTempo {
  
  private static final Logger log = Logger.getLogger("AGENTE");
  
  private static final Map<Integer, Tempo> tempos = new HashMap<Integer, Tempo>();
  
  public static synchronized void add(int mid, long dif) {
    try {
      Tempo t = tempos.get(mid);
      if (t == null) {
        t = new Tempo();
        tempos.put(mid, t);
      }
      t.adicionar(dif);            
      if (t.getQuantidade() >= Integer.MAX_VALUE) {
        if (Recompilador.recompilar(mid)) {   
          Dados.gravar(mid, t, SelecionadorAgente.obterUltimo(mid));
          zerar(mid);
        }
        else {
          System.out.println("ERRO na RECOMPILACAO: " + mid);
        }
      }
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
  } 
  
  
  public static synchronized void zerar(int mid) {
    tempos.remove(mid);
  }
  
  public static void finalizar() {
    log.fine("Executando finalizar()");
  }
}