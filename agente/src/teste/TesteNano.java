package teste;

import java.util.Arrays;

public class TesteNano {
  public static void main (String [] args) {
    long [] results = new long[1000];
    for (int i = 0; i < results.length; i++) {
      long ini = System.nanoTime();
      long fim = System.nanoTime();
      results[i] = fim - ini;
    }
    Arrays.sort(results);
    System.out.println("Menor: " + results[0]);
    System.out.println("Maior: " + results[results.length-1]);
    System.out.println("Mediana: " + results[results.length/2]);
  }
}
