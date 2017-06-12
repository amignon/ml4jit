package teste;

import java.lang.reflect.*;

public class Invoker {
  public static void main(String[] args) {
    for (int i = 0; i < 3; i++)
    invocar();
   }
  
  public static void invocar() {
    Class[] argTypes = new Class[1];
    argTypes[0] = String[].class;
    try {
      Method mainMethod = Class.forName("JGFHeapSortBenchSizeA").getDeclaredMethod("main",argTypes);
      Object[] argListForInvokedMain = new Object[1];
      argListForInvokedMain[0] = new String[0];
      // Place whatever args you
      // want to pass into other
      // class's main here.
      mainMethod.invoke(null, 
      // This is the instance on which you invoke
      // the method; since main is static, you can pass
      // null in.
      argListForInvokedMain);
      }
      catch (ClassNotFoundException ex) {
        System.err.println("Class not found in classpath.");
      }
      catch (NoSuchMethodException ex) {
        System.err.println("Class does not define public static void main(String[])");
      }
      catch (InvocationTargetException ex) {
        System.err.println("Exception while executing "+ex.getTargetException());
      }
      catch (IllegalAccessException ex) {
        System.err.println("main(String[]) in class  is not public");
      }
  }
}
