package teste;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import ml4jit.Mapeador;
import ml4jit.Util;


import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class TesteJA {
  
  private static List<String> classesToSkip = new ArrayList<String>();
  private static List<CtClass> classes = new ArrayList<CtClass>();
  
  public static void main(String [] args) {
    classesToSkip.add("javax.");
    classesToSkip.add("java.");
    classesToSkip.add("sun.");
    classesToSkip.add("com.sun.");
    // classesToSkip.add("org.jdom");
    // classesToSkip.add("org.apache.");
    classesToSkip.add("org.jikesrvm.");
    classesToSkip.add("javassist/");
    classesToSkip.add("agente/");
    scanClassPath();    
    for (CtClass cc : classes) {
      System.out.println(cc.getName() + " : " + cc.getMethods().length);
      for (CtMethod m : cc.getMethods()) {
        if (!m.getLongName().startsWith("java."))
          Util.processarCaracteristicas(m);
      }
    }
    Mapeador.gravarCaracteristicas();
  }
  
  public static void scanClassPath() {
    String list = System.getProperty("java.class.path");
    for (String path : list.split(":")) {
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
  
  public static void scanDirectory(File directory) {
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
  
  public static void scanJar(String path) {
    System.out.println("Analisando: " + path);
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
            System.out.println(file.getName());
            analyseInputStream(jar.getInputStream(file));
          }
        }
      }
      jar.close();
    } catch (IOException e) {
      System.out.println("Failed to open following JAR file: " + path);
    }
  }
  
  public static void analyseClassFile(String path) {
    try {
      FileInputStream f = new FileInputStream(path);
      analyseInputStream(f);
    } catch (IOException e) {
      System.out.println("File was not found: " + path);
    }
  }
  
  public static void analyseInputStream(InputStream is) {
    try {
      ClassPool cp = ClassPool.getDefault();
      classes.add(cp.makeClass(is));
    } catch (IOException e) {
      System.out.println("Erro: " + e);
    }
  }
}
