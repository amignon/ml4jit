package teste;

import java.util.ArrayList;
import java.util.List;

public class OutroTeste {
  
  public static void main (String [] args) {
    Foo f = new Foo();
        
    for (int i = 0; i < 10000 * 1000; i++ ) {
      int a = get() + 20;
      int b = a + 5;
    }
    t();
    
    System.out.println("FIM");
  }
  
  static int get () {
    return 10;
  }
  
  static int get2 ()  throws Exception {
    int x = 10;
    if (x > 10) return 0;
    return 10;
  }
  
  static int rec (int n) {
    if (n < 1) return 1;
    return rec (n-1);
  }
  
  
  static int rec (int n, int m) {
    //if (n < 1) return 1;
    return rec (n-1);
  }
  
  static int r () {
    return rec(10);
  }
  
  static void w() {
    int [] v = new int[5];
    int i = 0;
    while (i < 5) {
      v[i] = v[i] + i++;
    }
  }
  
  static void o () {
    List l = new ArrayList();
  }
  
  private int testField; 
  
  int l() {
    Foo f = new Foo();
    return f.x;
  }
  
  void j() {
    int i = 10;
    switch (i) {
    case 1:
      
      break;

    default:
      break;
    }    
  }
  
  static void t() {
    new TT().start();
  }
  
  static void ma () {
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 3; j++) {
        System.out.println("Teste...");
      }
    }
  }
  
  

}
