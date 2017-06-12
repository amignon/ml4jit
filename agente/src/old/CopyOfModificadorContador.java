package old;

import ml4jit.Config;
import ml4jit.Modificador;
import ml4jit.Util;
import ml4jit.counter.Counter;
import javassist.CtMethod;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.InstructionPrinter;
import javassist.bytecode.MethodInfo;

public class CopyOfModificadorContador extends Modificador {

  @Override
  public void modificar(CtMethod m) throws Exception {

    if (m.isEmpty()) {
      return;
    }
    String nomeMetodo = Util.getNome(m);
    if (nomeMetodo.startsWith(Config.MAIN + "(")) {    
      int p = exit(m);
      if (p >= 0) {      
        m.insertAt(p, "agente.Finalizador.finalizar();");
      }
      else {
        m.insertAfter("agente.Finalizador.finalizar();");
      }
      return;
    }
    
    int id = nomeMetodo.hashCode();    
    Counter.addId(id, nomeMetodo);    
    m.insertBefore("agente.Contador.gravar(" + id + ");"); 
  }
  
  
  private int exit(CtMethod m) throws Exception {       
    // percorre o codigo
    MethodInfo info = m.getMethodInfo2();
    ConstPool pool = info.getConstPool();
    CodeAttribute code = info.getCodeAttribute();
    if (code == null) {
      return -1;
    }
    CodeIterator iterator = code.iterator();
    while (iterator.hasNext()) {
      int pos;
      try {
        pos = iterator.next();
      } catch (BadBytecode e) {        
        throw new RuntimeException(e);
      }
      String s = InstructionPrinter.instructionString(iterator, pos, pool);
      if(s.contains("Method java.lang.System.exit((I)V)")) {    
        return info.getLineNumber(pos);
      }
    }
    return -1;
  }
}
