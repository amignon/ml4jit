package teste;

public class Exp1 {
  public static void main (String [] args) {

    for (int i = 0; i < 100; i++) {
      f2(i);      
    }
      
    //Teste.testar();
    for (int i = 0; i < 100; i++) {
      f2(i);
    }
    //Teste.testar();
    for (int i = 0; i < 100; i++) {
      f2(i);
    }
   /*
    O1 o = new O1();
    long ini = System.nanoTime();
    for (int i = 0; i < 100000; i++) {
      o.setX(10);
      o.getX();
    }
    System.out.println(System.nanoTime() - ini + " ns");
    Sampler.SAMPLE = true;
    System.out.println("R: " + f());
    */
  }
  
  public static int f() {
    int c = 0;
    for (int i = 0; i < 1000 * 1000; i++) {
      for (int j = 0; j < 1000; j++) {
        for (int k = 0; k < 1; k++) {
          int a = 10 + 20;
          int b = a * 3;
          c = a + b;
        }
      }
    }
    return c;
  }
  
  public static int f2(int i) {
    return i + 10;
  }
  
  public static int x() {
    return 10;
    /*
    int c = 0;
    for (int i = 0; i < 100 * 100; i++) {
      for (int j = 0; j < 100; j++) {
        for (int k = 0; k < 1; k++) {
          int a = 10 + 20;
          int b = a * 3;
          c = a + b;
        }
      }
    }
    return c;
    */
  }
}
