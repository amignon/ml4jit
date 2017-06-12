package ml4jit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * Baseado em JSinger (nanopatterns)
 * @author ale
 *
 */
public class FeaturesExtractor {
  
  private static Logger log;
  
  private static List<String> classesToSkip = new ArrayList<String>();
  private static List<CtClass> classes = new ArrayList<CtClass>();  

  
  public static void main(String [] args) {
    log = Util.initLog();
    log.setLevel(Level.FINE);
    log.fine("Inicializando...");
    init();
    scanClassPath();    
    for (CtClass cc : classes) {
      log.info("Classe: " + cc.getName());
      for (CtMethod m : cc.getDeclaredMethods()) {
        if (!m.getLongName().startsWith("java."))
          Util.processarCaracteristicas(m);
      }
    }
    Mapeador.gravarCaracteristicas();
  }
  
  private static void scanClassPath() {
    String list = System.getProperty("java.class.path");
    for (String path : list.split(":")) {
      log.fine("Analisando: " + path);
      File thing = new File(path);
      if (thing.isDirectory()) {
        scanDirectory(thing);
      } else if (path.endsWith(".class")) {
        analyseClassFile(path);
      } else if (path.endsWith(".jar")) {        
        scanJar(path);
      }      
    }
  }
  
  private static void scanDirectory(File directory) {
    for (String entry : directory.list()) {
      String path = directory.getPath() + "/" + entry;
      File thing = new File(path);
      if (thing.isDirectory()) {
        scanDirectory(thing);
      } else if (thing.isFile() && path.endsWith(".class")) {
        analyseClassFile(path);
      } else if (thing.isFile() && path.endsWith(".jar")) {
        scanJar(path);
      }
    }
  }
  
  private static void scanJar(String path) {
    log.fine("Analisando Jar: " + path);
    boolean ok = true;
    try {
      JarFile jar = new JarFile(path);
      Enumeration<JarEntry> enums = jar.entries();
      while (enums.hasMoreElements()) {
        JarEntry file = enums.nextElement();
        if (!file.isDirectory() && file.getName().endsWith(".class")) {
          ok = true;
          for (String classToSkip : classesToSkip) {
            if (file.getName().startsWith(classToSkip)) {        
              ok = false;
            }
          }
          if (ok) {
            log.fine("\tAnalisando Arquivo: " + file);
            analyseInputStream(jar.getInputStream(file));
          }
        }
      }
      jar.close();
    } catch (IOException e) {      
      System.out.println("Erro ao abrir o arquivo jar: " + path);
    }
  }
  
  private static void analyseClassFile(String path) {
    try {
      FileInputStream f = new FileInputStream(path);
      analyseInputStream(f);
    } catch (IOException e) {
      System.out.println("Arquivo nao encontrado: " + path);
    }
  }
  
  private static void analyseInputStream(InputStream is) {
    try {
      ClassPool cp = ClassPool.getDefault();
      classes.add(cp.makeClass(is));
    } catch (IOException e) {
      System.out.println("Erro: " + e);
    }
  }
  
  private static void init() {
/*    classesToSkip.add("javax/");
    classesToSkip.add("java/");
    classesToSkip.add("sun/");
    classesToSkip.add("com/sun/");
    classesToSkip.add("org.jikesrvm/");
    classesToSkip.add("javassist/");
    classesToSkip.add("agente/");*/
  }
}
