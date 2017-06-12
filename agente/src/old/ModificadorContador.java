package old;

import ml4jit.Modificador;
import ml4jit.Util;
import ml4jit.counter.Counter;
import javassist.CtMethod;

public class ModificadorContador extends Modificador {

  @Override
  public void modificar(CtMethod m) throws Exception {
    if (m.isEmpty()) {
      return;
    }
    String nomeMetodo = Util.getNome(m);    
//    if (nomeMetodo.startsWith(Config.MAIN + "(")) {    
//      //this.modificarMain(m);
//      return;
//    }
    
    int id = nomeMetodo.hashCode();    
    Counter.addId(id, nomeMetodo);    
    m.insertBefore("agente.contador.Contador.gravar(" + id + ");"); 
  }
}
