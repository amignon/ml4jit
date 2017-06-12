package ml4jit;

import java.util.logging.Logger;

public class Sampler implements Runnable {
  
  private static final Logger log = Logger.getLogger("AGENTE");
  private volatile static boolean done = false;
  public volatile static boolean SAMPLE = true;
  public static Thread th = null;

  @Override
  public void run() {
    log.fine("Executando o Sampler...");
    while (!done) {
      try {
        Thread.sleep(Config.WITH_SAMPLING);
        SAMPLE = !SAMPLE;
        Thread.sleep(Config.WITHOUT_SAMPLING);
        SAMPLE = !SAMPLE;
      } catch (Exception e) {
        log.warning(e.getMessage());
      }
    }
    log.fine("Sampler Finalizado");
  }   
  
  public static void inicializar() {
    th = new Thread(new Sampler());
    th.setDaemon(true);
    th.start();    
  }
  
  public static void finalizar() {
    done = true;
    if (th != null) {
      th.interrupt();
    }    
  }
}
