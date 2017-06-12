package old;

import ml4jit.Config;
import ml4jit.Modificador;
import ml4jit.Util;
import javassist.CtClass;
import javassist.CtMethod;

/** TODO: Verificar Thread */
public class ModificadorTempoCodigo extends Modificador {

  @Override
  public void modificar(CtMethod m) throws Exception {
    if (m.isEmpty()) {
      return;
    }
    String nomeMetodo = Util.getNome(m);
    if (nomeMetodo.contains(Config.MAIN)) {      
      modificarMain(m);
      return;
    }
    
    if (Config.AMOSTRA) {
      if (!nomeMetodo.contains("_COPY"))
        return;
    }    
    
    m.addLocalVariable("___ini___", CtClass.longType);
    m.addLocalVariable("___fim___", CtClass.longType);
    m.addLocalVariable("___id___", CtClass.intType);
  
    int id = nomeMetodo.hashCode();
    
    m.insertBefore(getInsertBefore(m, id));
    m.insertAfter(getInsertAfter(m));    
  }
  
  private String getInsertBefore(CtMethod m, int id) {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("___ini___ = System.nanoTime();");
    sb.append("___id___ = " + id + ";");    
    sb.append("}");
    return sb.toString();
  }
  
  private String getInsertAfter(CtMethod m) throws Exception {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("___fim___ = System.nanoTime() - ___ini___;");
    sb.append("agente.GravadorTempo.add(___id___, ___fim___);");
    sb.append("}");
    return sb.toString();
  }
  
//  private void modificarMain (CtMethod m) throws Exception {
//    StringBuilder sb = new StringBuilder();
//    sb.append("{");
//    sb.append("agente.Finalizador.finalizar();");
//    sb.append("}");
//    m.insertAfter(sb.toString());
//  }
}