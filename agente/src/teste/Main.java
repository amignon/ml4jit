package teste;

import java.io.FileOutputStream;

public class Main {
  public static void main (String [] args) {
    System.out.println("Teste");
    f();
    f();
    f1();
  }
  
  public static int f() {
    int x = 10;
    int y = 20;
    return x + 5 + y;
  }
  
  public static void f1() {
    System.out.println("f1");
  }
  
  public static void f2() throws Exception {
      throw new Exception();
  }
}
