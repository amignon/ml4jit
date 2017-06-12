package ml4jit;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.logging.Logger;

import sun.rmi.runtime.Log;

import javassist.ByteArrayClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;

public class Teste implements ClassFileTransformer {
  
  protected static final Logger log = Logger.getLogger("AGENTE");
  
  public Teste() {
    try {
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
    // TODO Auto-generated method stub
    byte[] byteCode = classfileBuffer;
    String dotClassName = className.replace('/', '.');
    
    log.info("Instrumentalizando Classe " + dotClassName);
    try {
      
//      ClassPool cp = ClassPool.getDefault();
//      CtClass cc = cp.get(dotClassName);
      
      
      
      
      ClassPool cp = ClassPool.getDefault();
      //log.info(new ByteArrayClassPath(dotClassName, byteCode).toString());
      //cp.appendClassPath(new ByteArrayClassPath(dotClassName, byteCode));
      cp.appendClassPath("./scratch/jar/*");
      CtClass cc = cp.get(dotClassName);

      
      CtMethod[] m = cc.getDeclaredMethods();
      
      for (int i = 0; i < m.length; i++) {
        if (!m[i].isEmpty())
          m[i].insertAfter("int _X_X_ = 10;");
      }
      
      byteCode = cc.toBytecode();
      cc.detach();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return byteCode;
  }

}

class Trans implements Translator {

  @Override
  public void onLoad(ClassPool arg0, String arg1) throws NotFoundException, CannotCompileException {
    System.out.println("Onload: " + arg1);
  }

  @Override
  public void start(ClassPool arg0) throws NotFoundException, CannotCompileException {
    // TODO Auto-generated method stub
    System.out.println("Start");
    
  }
  
}
