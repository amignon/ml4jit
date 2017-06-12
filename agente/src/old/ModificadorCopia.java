package old;

import ml4jit.Modificador;
import ml4jit.Util;
import javassist.CtClass;
import javassist.CtMethod;

/** TODO: Verificar Thread */
public class ModificadorCopia extends Modificador {

  @Override
  public void modificar(CtMethod m) throws Exception {
    if (m.isEmpty()) {
      return;
    }
    String nomeMetodo = Util.getNome(m);
//    if (nomeMetodo.contains(ml4jit.Config.MAIN)) {      
//      modificarMain(m);
//      return;
//    }
    
    // cria uma copia do metodo e instrumentaliza a copia
    // TODO: Colocar o ID na copia como sendo do metodo original
    CtMethod cop = copiar(m);
    
    cop.addLocalVariable("___ini___", CtClass.longType);
    cop.addLocalVariable("___fim___", CtClass.longType);
    cop.addLocalVariable("___id___", CtClass.intType);
  
    int id = nomeMetodo.hashCode();
    
    cop.insertBefore(getInsertBefore(cop, id));
    cop.insertAfter(getInsertAfter(cop));    
  }
  
  private CtMethod copiar(CtMethod m) throws Exception {
    Copier cop = new Copier();
    return cop.copy(m, m.getDeclaringClass());
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
    sb.append("agente.treinamento.GravadorTempoTreinamento.add(___id___, ___fim___);");
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