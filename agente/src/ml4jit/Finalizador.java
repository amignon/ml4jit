package ml4jit;

import java.util.logging.Logger;

import old.Consumidor;
import old.GravadorTempo;

import ml4jit.counter.Counter;
import ml4jit.training.TimeRecorder;


/**
 * Responsável por finalizar os processos que armazenam os dados das instrumentalizações.
 * @author ale
 */

public class Finalizador {
  
  private static final Logger log = Logger.getLogger("AGENTE");
  
  public static void finalizar() {
    log.fine("Executando Finalizador.finalizar()");
    if (Config.MODE.equals("counter")) {
      Counter.end();
    }
    else if (Config.MODE.equals("training")) {
      TimeRecorder.finalizar();
      if (Config.SAMPLING) {
        Sampler.finalizar();
      }
    } else {
      GravadorTempo.finalizar();
      //Mapeador.gravar();
      Mapeador.gravarCaracteristicas();
      Consumidor.finalizar();
    }
  }
}
