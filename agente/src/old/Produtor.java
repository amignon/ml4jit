package old;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Produtor {
  private final BlockingQueue<Integer> queue;
  private final Consumidor consumidor;
  private static Produtor prod = null;
  
  private Produtor(BlockingQueue<Integer> queue) {
    this.queue = queue;
    this.consumidor = new Consumidor(this.queue);
  }
  
  public static Produtor instance() {
    if (prod == null) {
      prod = new Produtor(new LinkedBlockingQueue<Integer>());
    }
    return prod;
  }
  
  public void notificar(int mid) {
    try {
      this.queue.put(mid);
    } catch (Exception e) {
      System.out.println("[PRODUTOR] " + e);
    }
  }
}
