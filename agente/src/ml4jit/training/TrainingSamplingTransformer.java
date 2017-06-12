package ml4jit.training;

import java.io.File;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import ml4jit.Sampler;
import ml4jit.Transformer;
import ml4jit.Util;

public class TrainingSamplingTransformer extends Transformer {

  public TrainingSamplingTransformer(File f) {
    init(f);
    Sampler.inicializar();
  }
  
  @Override
  protected void transform(CtMethod m) throws Exception {
    if (m.isEmpty()) {
      return;
    }
    
    // cria uma copia do metodo e instrumentaliza a copia
    CtMethod cop = copy(m);
    
    cop.addLocalVariable("___ini___", CtClass.longType);
    cop.addLocalVariable("___fim___", CtClass.longType);
    cop.addLocalVariable("___id___", CtClass.intType);
  
    int id = Util.getId(m);
    
    cop.insertBefore(getInsertBefore(cop, id));
    cop.insertAfter(getInsertAfter(cop));    
  }
  
  private CtMethod copy(CtMethod m) throws Exception {
    CtClass c = m.getDeclaringClass();
    CtMethod n = CtNewMethod.copy(m, m.getName() + "_COPY", c, null);
    c.addMethod(n);
    // instrumentaliza o original
    instOriginal(m);   
    return n;
  }
  
  private void instOriginal(CtMethod m) throws Exception {
    StringBuilder sb = new StringBuilder();
    sb.append("{");  
    sb.append("if(ml4jit.Sampler.SAMPLE) {");
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
    sb.append("ml4jit.training.TimeRecorder.record(___id___, ___fim___);");
    sb.append("}");
    return sb.toString();
  }
}
