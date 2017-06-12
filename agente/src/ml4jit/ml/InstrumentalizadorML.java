package ml4jit.ml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import ml4jit.Codigo;
import ml4jit.Mapeador;
import ml4jit.Util;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.InstructionPrinter;
import javassist.bytecode.MethodInfo;

public class InstrumentalizadorML implements ClassFileTransformer {

  private static List<String> classesToSkip = new ArrayList<String>();
  private static List<String> methodsToSkip = new ArrayList<String>();
  private static boolean init = false;
  private static final Logger log = Logger.getLogger("AGENTE");

  public InstrumentalizadorML() {
    classesToSkip.add("javax.");
    classesToSkip.add("java.");
    classesToSkip.add("sun.");
    classesToSkip.add("com.sun.");
    classesToSkip.add("org.jdom");
    classesToSkip.add("org.apache.");
    classesToSkip.add("org.jikesrvm.");
    classesToSkip.add("javassist.");
  }

  public InstrumentalizadorML(File f) {
    if (init) {
      return;
    }    
    init = true;    
    initSkip(f);
  }

  public byte[] transform(ClassLoader loader, String className, 
      @SuppressWarnings("rawtypes") Class classBeingRedefined,
      ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

    long ini = System.currentTimeMillis();
    
    byte[] byteCode = classfileBuffer;
    String dotClassName = className.replace('/', '.');
    for (String classToSkip : classesToSkip) {
      if (dotClassName.startsWith(classToSkip)) {        
        return byteCode;
      }
    }

    log.fine("Instrumentalizando Classe " + dotClassName);
    try {
      ClassPool cp = ClassPool.getDefault();
      CtClass cc = cp.get(dotClassName);
      CtMethod[] m = cc.getDeclaredMethods();

      for (int i = 0; i < m.length; i++) {
        processarCaracteristicas(m[i]);       
      }

      imprimirClasse(cc);
      
      byteCode = cc.toBytecode();
      cc.detach();
    } catch (Exception e) {
      log.warning(e.getMessage());
      System.err.println(e);
    }
    
    System.out.println("Tempo: " + (System.currentTimeMillis() - ini));
    return byteCode;
  }
  
  
  private void processarCaracteristicas(CtMethod m) {    
    String nome = Util.getNome(m);
    System.out.println("Processando  " + nome);
    Codigo c = new Codigo(nome);
    if (c.processar(m)) {
      c.imprimir();
      Mapeador.add(nome.hashCode(), nome);
      Mapeador.adicionarCaracteristica(c.getCaracteristicas());
    }
  }
   
  private void imprimirClasse(CtClass c) throws Exception {
    CtMethod[] m = c.getDeclaredMethods();
    for (int i = 0; i < m.length; i++) {
      imprimirCodigo(m[i]);
    }
  }
  
  private void imprimirCodigo(CtMethod m) throws Exception {   
    PrintWriter pw = new PrintWriter(new FileOutputStream(new File ("codigo.txt"), true));   
    // percorre o codigo
    MethodInfo info = m.getMethodInfo2();
    ConstPool pool = info.getConstPool();
    CodeAttribute code = info.getCodeAttribute();
    if (code == null) {
      pw.close();
      return;
    }
    pw.write("*** " + Util.getNome(m) + " ***\n");
    pw.write("\tTAM: " + code.getCodeLength() + "\n");
    pw.write("\tLOCALS: " + code.getMaxLocals() + "\n");
    CodeIterator iterator = code.iterator();
    while (iterator.hasNext()) {
      int pos;
      try {
        pos = iterator.next();
      } catch (BadBytecode e) {    
        pw.close();
        throw new RuntimeException(e);
      }
      pw.write("\t" + pos + " " + InstructionPrinter.instructionString(iterator, pos, pool) + "\n");      
    }
    pw.close();
  }  

  private void initSkip(File f) {
    try {
      Scanner in = new Scanner(f);
      while (in.hasNext()) {
        String s = in.next();
        if (s.equals("METODOS"))
          break;
        classesToSkip.add(s);
      }
      while (in.hasNext()) {
        methodsToSkip.add(in.next());
      }
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
  }
}

