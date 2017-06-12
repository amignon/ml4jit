package ml4jit;

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

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.InstructionPrinter;
import javassist.bytecode.MethodInfo;

/**
 * Classe abstrata que representa o comportamento padrao de um instrumentalizador
 * 
 * @author ale
 *
 */
public abstract class Transformer implements ClassFileTransformer {

  private static boolean init = false;  
  private static List<String> classesToSkip = new ArrayList<String>();
  private static List<String> methodsToSkip = new ArrayList<String>();  
  
  protected static final Logger log = Logger.getLogger("AGENTE");
  
  static boolean append = true;
  
  /**
   * Instrumentaliza um determinado metodo de acordo com a classe que implementa esse metodo.
   * @param m Metodo a ser instrumentalizado
   * @throws Exception
   */
  protected abstract void transform(CtMethod m) throws Exception;
  
  /** 
   * Inicializa o instrumentalizador.
   * @param f Arquivo contendo as classes e metodos que nao devem ser instrumentalizados.
   */
  protected void init(File f) {
    if (init) {
      return;
    }    
    init = true;    
    initSkip(f);    
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

    log.fine("Instrumentalizando Classe: " + dotClassName);
    try {
      ClassPool cp = ClassPool.getDefault();
      // cp.appendPathList(Config.CLASSPATH);
      CtClass cc = null;
      try {
        cc = cp.get(dotClassName);
      } catch(NotFoundException e) {
        cp.appendPathList(Config.CLASSPATH);
        cc = cp.get(dotClassName);
      }
      CtMethod[] m = cc.getDeclaredMethods();

      for (int i = 0; i < m.length; i++) {
        if (ignore(m[i])) continue;        
        transform(m[i]);        
      }
      
      if (Config.RECORD_CODE) {
        imprimirClasse(cc);
      }
      
      byteCode = cc.toBytecode();
      cc.detach();      
    } catch (Exception e) {
      log.warning(e.getMessage());
      //System.err.println(e);
    }
    return byteCode;
  }  
  
  /**
   * Por padrao, sempre processa as caracteristicas dos metodos.
   * @param m 
   */
  protected void processarCaracteristicas(CtMethod m) {
    String nome = Util.getNome(m);
    Codigo c = new Codigo(nome);
    if (c.processar(m)) {
      c.imprimir();
      Mapeador.add(nome.hashCode(), nome);
      Mapeador.adicionarCaracteristica(c.getCaracteristicas());
    }
  }
  
  protected void imprimirClasse(CtClass c) throws Exception {
    CtMethod[] m = c.getDeclaredMethods();
    for (int i = 0; i < m.length; i++) {
      imprimirCodigo(m[i]);
    }
  }
  
  protected void imprimirCodigo(CtMethod m) throws Exception {   
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
  
  private boolean ignore(CtMethod m) {
    if (m.isEmpty()) {
      return true;
    } 
    for (String me : methodsToSkip) {
      if (Util.getNome(m).startsWith(me)) {
        if (Config.DEBUG)
          System.out.println("\tIgnorando: " + Util.getNome(m));
        return true;
      }
    }
    return false;
  }

  protected void initSkip(File f) {
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
      log.warning("Erro: " + e.getMessage());
    }
  }
}

