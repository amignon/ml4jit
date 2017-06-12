package ml4jit;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javassist.CtMethod;
import javassist.bytecode.Descriptor;

public class Util {
  
  /**
   * Retorna o nome padronizado do metodo
   * @param m 
   * @return Nome do metodo
   */
  public static String getNome(CtMethod m) {
    StringBuilder sb = new StringBuilder();
    sb.append(m.getDeclaringClass().getName());
    sb.append(".");
    sb.append(m.getName());
    try {
      sb.append(Descriptor.ofParameters(m.getParameterTypes()));
    } catch (Exception e) {
      sb.append("()");
    }
    return sb.toString();
  }
  
  public static int getId(CtMethod m) {
    return getNome(m).hashCode();
  }
  
  public static void processarCaracteristicas(CtMethod m) {
    String nome = Util.getNome(m);
    Codigo c = new Codigo(nome);
    if (c.processar(m)) {
      if (Config.RECORD_CODE) {
        c.imprimir();
      }
      Mapeador.add(nome.hashCode(), nome);
      Mapeador.adicionarCaracteristica(c.getCaracteristicas());
    }
  }
  
  public static Logger initLog() {
    Logger log = Logger.getLogger("AGENTE");
    log.setUseParentHandlers(false);
    try {
      FileHandler fh = new FileHandler("ml4jit.log", true);
      fh.setFormatter(new VerySimpleFormatter());
      log.addHandler(fh);      
    } catch (Exception e) {
      e.printStackTrace();
    }  
    return log;
  }
}
