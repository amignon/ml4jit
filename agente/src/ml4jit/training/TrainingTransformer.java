package ml4jit.training;

import java.io.File;

import javassist.CtClass;
import javassist.CtMethod;
import ml4jit.Transformer;
import ml4jit.Util;

public class TrainingTransformer extends Transformer {

  public TrainingTransformer(File f) {
    init(f);
  }
  
  @Override
  protected void transform(CtMethod m) throws Exception {
    if (m.isEmpty()) {
      return;
    }
    
    m.addLocalVariable("___ini___", CtClass.longType);
    m.addLocalVariable("___fim___", CtClass.longType);
  
    int id = Util.getId(m);
    
    m.insertBefore(getInsertBefore());
    m.insertAfter(getInsertAfter(id));    
  }  
  
  private String getInsertBefore() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("___ini___ = System.nanoTime();");
    sb.append("}");
    return sb.toString();
  }
  
  private String getInsertAfter(int id) throws Exception {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("___fim___ = System.nanoTime() - ___ini___;");
    sb.append("ml4jit.training.TimeRecorder.record(" + id + ", ___fim___);");
    sb.append("}");
    return sb.toString();
  }
}
