package ml4jit;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class ModificadorMain {
  
  public void modificar (CtMethod m, CtClass c) throws Exception {
    // copia o método main
    CtMethod n = CtNewMethod.copy(m, m.getName() + "_COPY", c, null);
    c.addMethod(n);
    // remove o metodo main
    c.removeMethod(m);
    // cria um novo metodo main
    CtMethod nm = CtNewMethod.make(codigo(), c);
    c.addMethod(nm);
    
  }
  
  private String codigo() {
    StringBuilder sb = new StringBuilder();
    sb.append("public static void main(String [] args) {");
    
    sb.append("System.out.println(\"Novo main...\");");
    sb.append("for (int i = 0; i < 3; i++)");
    sb.append("main_COPY(args);");
    
    sb.append("}");
    return sb.toString();
  }

}
