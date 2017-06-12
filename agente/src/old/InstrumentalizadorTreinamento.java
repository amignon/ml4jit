package old;

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
import ml4jit.Config;
import ml4jit.Heuristicas;
import ml4jit.Mapeador;
import ml4jit.Modificador;
import ml4jit.Nanopatterns;
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

public class InstrumentalizadorTreinamento implements ClassFileTransformer {

  private static List<String> classesToSkip = new ArrayList<String>();
  private static List<String> methodsToSkip = new ArrayList<String>();
  private static boolean init = false;
  private static Modificador modificador = null;
  private static final Logger log = Logger.getLogger("AGENTE");

  public InstrumentalizadorTreinamento() {
    classesToSkip.add("javax.");
    classesToSkip.add("java.");
    classesToSkip.add("sun.");
    classesToSkip.add("com.sun.");
    classesToSkip.add("org.jdom");
    classesToSkip.add("org.apache.");
    classesToSkip.add("org.jikesrvm.");
    classesToSkip.add("javassist.");
  }

  public InstrumentalizadorTreinamento(File f) {
    if (init) {
      return;
    }    
    log.fine("Inicializando Instrumentalizador de Treinamento...");
    init = true;    
    initSkip(f);
    // modificador = new ModificadorTreinamento();
    modificador = new ModificadorCopia();
  }

  public byte[] transform(ClassLoader loader, String className, 
      @SuppressWarnings("rawtypes") Class classBeingRedefined,
      ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
    
    byte[] byteCode = classfileBuffer;
    String dotClassName = className.replace('/', '.');
    for (String classToSkip : classesToSkip) {
      if (dotClassName.startsWith(classToSkip)) {        
        return null;
      }
    }

    log.fine("Instrumentalizando Classe " + dotClassName);
    try {
      ClassPool cp = ClassPool.getDefault();
      CtClass cc = cp.get(dotClassName);
      CtMethod[] m = cc.getDeclaredMethods();

      for (int i = 0; i < m.length; i++) {
        // Util.processarCaracteristicas(m[i]);
        processar(m[i]);
      }
      
      if (Config.RECORD_CODE) {
        imprimirClasse(cc);
      }
      
      byteCode = cc.toBytecode();
      cc.detach();
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
    return byteCode;
  }
  
  private void processar (CtMethod m) throws Exception {
    modificador.modificar(m);
  }
  
  private void processarCaracteristicas(CtMethod m) {
    String nome = Util.getNome(m);
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
    PrintWriter pw = new PrintWriter(new FileOutputStream(new File (ml4jit.Config.PATH + "codigo.txt"), true));   
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
  
  
  
  @SuppressWarnings("unused")
  private void processar1 (CtMethod m) throws Exception {
    if (ignorar(m)) return;

    log.info("[ANALISANDO]: " + Util.getNome(m));
    Nanopatterns np = nanoPatterns(m);
    if (np == null) {
      log.info("NULL!!!!!!");
      return;
    }
    if (Heuristicas.ignorar(np)) {
//      if (!ml4jit.Config.FULL) {
//        log.info("[IGNORANDO]: " + Util.getNome(m));          
//        return;
//      }
    }
    // Instrumentalizacao
    //long ini = System.currentTimeMillis();        
    modificador.modificar(m);
    //log.fine("Instrumentalizou " + dotClassName + "." + m[i].getName()  + " " + (System.currentTimeMillis() - ini) + " ms");
    
  }
  
  private Nanopatterns nanoPatterns(CtMethod m) throws Exception {
    String sc = m.getDeclaringClass().getSuperclass().getName();
    Nanopatterns np = new Nanopatterns(Util.getNome(m), m.getReturnType().getName(), sc);
    np.throwsExceptions(m.getExceptionTypes().length);   
    // percorre o codigo
    MethodInfo info = m.getMethodInfo2();
    ConstPool pool = info.getConstPool();
    CodeAttribute code = info.getCodeAttribute();
    if (code == null) {
      return null;
    }
    np.setBytecodes(code.getCodeLength());
    CodeIterator iterator = code.iterator();
    while (iterator.hasNext()) {
      int pos;
      try {
        pos = iterator.next();
      } catch (BadBytecode e) {        
        throw new RuntimeException(e);
      }
      np.adicionarCodigo(pos, InstructionPrinter.instructionString(iterator, pos, pool));      
    } 
    // np.imprimir();
    return np;
  }
  
  private boolean ignorar(CtMethod m) {
    if (m.isEmpty())
      return true;
    for (String me : methodsToSkip) {
      if (m.getLongName().startsWith(me)) {
        if (ml4jit.Config.DEBUG)
          System.out.println("\tIgnorando: " + m.getLongName());
        return true;
      }
    }
    return false;
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
      e.printStackTrace();
    }
  }
}

