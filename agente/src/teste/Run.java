package teste;

public class Run { 
  public static void main (String [] args) {
    long ini, fim;
    ini = System.nanoTime();
    f();
    fim = System.nanoTime() - ini;
    System.out.println ("Tempo: " + fim);
    f1();
    ini = System.nanoTime();
    f1();
    fim = System.nanoTime() - ini;
    System.out.println ("Tempo3: " + fim);
  }
  
  public static int f () {
    int r = 10;
    return r;
  }

  public static int f1() {
    long ini, fim;
    ini = System.nanoTime();
    int r = 10;
    fim = System.nanoTime() - ini;
    System.out.println ("Tempo2: " + fim);
    return r;
  }
}
