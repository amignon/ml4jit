package old;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import ml4jit.Recompilador;

public class Consumidor implements Runnable {
  private final BlockingQueue<Integer> queue;
  private static Thread t;
  private static Map<Integer,Integer> contador = new HashMap<Integer, Integer>();
  
  public Consumidor(BlockingQueue<Integer> queue) {
    this.queue = queue;
    t = new Thread(this);
    t.start();
  }

  @Override
  public void run() {
    while(true){
      try {
        contar(queue.take());
      } catch (InterruptedException e) {
        System.out.println("[CONSUMIDOR] " + e);
        return;
      }
    }
  }
  
  private static synchronized void contar(Integer mid) {
    Integer i = contador.get(mid);
    if (i == null) {
      contador.put(mid, 1);
    }
    else {
      if (i > 50) {
        contador.put(mid, 1);
        Recompilador.recompilar(mid);
        float m = GravadorTempo.media(mid);
        System.out.println("Media " + mid + ": " + m);
        GravadorTempo.zerar(mid);
      } else
        contador.put(mid, i.intValue() + 1);
    }
  }
  
  public static void finalizar() {
    Iterator<Integer> it = contador.keySet().iterator();
    while (it.hasNext()) {
      Integer id = it.next();
      Integer v = contador.get(id);
      System.out.println(id + ":" + v);
    } 
    if (t != null) {
      t.interrupt();
    }          
  }
}
