package old;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class Copiador {
  
  public CtMethod copiar(CtMethod m, CtClass c) throws Exception {
//    if (Util.getNome(m).startsWith(Config.MAIN + "(")) {      
//      return null;
//    }
 
    CtMethod n = CtNewMethod.copy(m, m.getName() + "_COPY", c, null);
    c.addMethod(n);
    inst(m);   
    return n;
  }
  
  public void inst(CtMethod m) throws Exception {
    StringBuilder sb = new StringBuilder();
    sb.append("{");  
    sb.append("if(agente.Sampler.SAMPLE) {");
    if (!m.getReturnType().getName().equals("void")) {
      sb.append("return ");
    }
    sb.append(m.getName() + "_COPY($$);");
    if (m.getReturnType().getName().equals("void")) {
      sb.append("return;");
    }
    sb.append("}");
    sb.append("}");    
    m.insertBefore(sb.toString());
  }
}
