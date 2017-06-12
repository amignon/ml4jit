package teste;

import java.util.ArrayList;

public class Calculo {
  
  public static ArrayList<Long> lista = new ArrayList<Long>();
  
  public static void main (String [] args) {
    lista.add((long) 196);
    lista.add((long) 200);
    lista.add((long) 138);
    System.out.println(media());
    System.out.println(variancia());
    System.out.println(desvioPadrao());
    
  }
  
  public static float media () {
    long soma = 0;
    for (Long l : lista) {
      soma += l.longValue();
    }
    return (float) soma / lista.size();
  }
  
  public static float variancia () {
    float m = media();
    float soma = 0;
    for (Long l : lista) {
      soma += ((l - m) * (l - m));
    }
    return soma / lista.size();
  }
  
  public static float desvioPadrao () {
    return (float) Math.sqrt(variancia());
  }
}
