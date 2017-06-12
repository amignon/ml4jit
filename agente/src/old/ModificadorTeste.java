package old;

import ml4jit.Config;
import ml4jit.Mapeador;
import ml4jit.Modificador;
import ml4jit.Util;
import javassist.CannotCompileException;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class ModificadorTeste extends Modificador {

  @Override
  public void modificar(CtMethod m) throws Exception {
    System.out.println("Entrou: " + m.getName());
    if (m.isEmpty()) {
      return;
    }
    String nomeMetodo = Util.getNome(m);
    if (nomeMetodo.contains(Config.MAIN)) {      
      modificarMain(m);
    }    
    
    m.instrument(new ExprEditor() {
      public void edit(MethodCall m) throws CannotCompileException {
        try {
          if (!m.getClassName().startsWith("java"))
            m.replace(modificar(m));          
        } catch (Exception e) {
          System.out.println("ERRO!!!" + e);
        }        
      }
    });    
  }
  
  public static String modificar(MethodCall m) throws Exception {
    int id = Util.getNome(m.getMethod()).hashCode();
    Mapeador.add(id, Util.getNome(m.getMethod()));
    StringBuilder sb = new StringBuilder();
    sb.append("{")
      .append("int id = " + id + ";" )
      .append("long ini = System.nanoTime();")
      .append("$_ = $proceed($$);")
      .append("long fim = System.nanoTime() - ini;")
      .append("agente.GravadorTempo.add(id, fim);")
      .append("}");
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
