package teste;

public class Teste {

  public static void main(String[] args) throws InterruptedException {
    Sleeping sleeping = new Sleeping();
    // sleeping.m1();
    // sleeping.m2();
    for (int i = 1; i <= 1; i++) {
      sleeping.m1();
      //sleeping.m2();
    }
    //m(10, "Ale");
  }
  
  private static void array (int n) {
    int a[] = new int[n];
    for (int i = 0; i < a.length; i++) {
      a[i] = i * 2;
      int x = a[i];
    }
  }

  private static void cmp () {
    float a = 10, b = 20;
    if (a > b) {
      return;
    }
  }
  
  static int abc () {
    try {
      Sleeping sleeping = new Sleeping();
      sleeping.m1();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 10;
  }
  
  private static void m(int i, String a) throws InterruptedException {
    try {
      Sleeping sleeping = new Sleeping();
      sleeping.m1();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static void Convert() {
    byte imByte = 0;
    int imInt = 125;
    while (true) {
      ++imInt;
      imByte = (byte) imInt;
      imInt *= -1;
      imByte = (byte) imInt;
      imInt *= -1;
    }
  }
  
  static int x = 10;
  public static int getTeste() {
    return x;
  }
  
  public static void setTeste(int y) {
    x = y;
  }
}
