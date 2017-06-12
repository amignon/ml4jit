package ml4jit;

import java.lang.instrument.Instrumentation;
import java.util.logging.Logger;


/**
 * Classe principal do Agente contendo o metodo premain.
 * 
 * Processa as opcoes de linha de comando.
 * 
 * Adiciona o instrumentalizador dos metodos.
 * 
 * @author ale
 *
 */
public class Agent {

  private static Logger log = null;

  public static void premain(String agentArgs, Instrumentation inst) {
    log = Util.initLog();
    String[] args = agentArgs.split(";");
    Config.processar(args);
    if (Config.MODE.equalsIgnoreCase("ml")) {
      log.fine("Executando em modo ML");
      Mapeador.lerCaracteristicas("features.csv");
    }
    else {      
      inst.addTransformer(TransformerFactory.create(Config.MODE));   
    }
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        log.fine("Finalizando a execucao...");
        Finalizador.finalizar();
      }
    });
  }
}
