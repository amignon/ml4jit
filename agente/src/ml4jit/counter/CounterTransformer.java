package ml4jit.counter;

import java.io.File;

import ml4jit.Transformer;
import ml4jit.Util;

import javassist.CtMethod;

public class CounterTransformer extends Transformer {
    
  public CounterTransformer(File f) {
    init(f);
  }

  @Override
  protected void transform(CtMethod m) throws Exception {
    if (m.isEmpty()) {
      return;
    }
    int id = Util.getId(m);    
    Counter.addId(id, Util.getNome(m));    
    m.insertBefore("ml4jit.counter.Counter.record(" + id + ");"); 
  }
}
