package ml4jit.ml;

import java.util.logging.Logger;

import ml4jit.Mapeador;
import ml4jit.SocketClient;
import ml4jit.rvm.Selector;

public class SelecionadorNull extends Selector {
  
  private static final Logger log = Logger.getLogger("AGENTE");

  @Override
  public int select(String m) {
    log.info("Selecionador Null..." + m);
    // obtem a caracteristica do metodo
    String carac = Mapeador.getCaracteristica(m);
    if (carac == null) return -1;
    String r = SocketClient.processar(carac);    
    if (r != null) {
      Integer.parseInt(r);
    }
    return -1;
  }
}
