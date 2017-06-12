package ml4jit;

import java.util.ArrayList;

public class Tempo {
  
  public ArrayList<Long> lista;
  
  public Tempo() {
    lista = new ArrayList<Long>();
  }
  
  public void adicionar(long t) {
    lista.add(t);
  }
  
  public int getQuantidade() {
    return lista.size();
  }
  
  public float getMedia () {
    long soma = 0;
    for (Long l : lista) {
      soma += l.longValue();
    }
    return (float) soma / lista.size();
  }
  
  public float variancia () {
    float m = getMedia();
    float soma = 0;
    for (Long l : lista) {
      soma += ((l - m) * (l - m));
    }
    return soma / lista.size();
  }
  
  public float desvioPadrao () {
    return (float) Math.sqrt(variancia());
  }
  
  public void imprimir () {
    StringBuilder sb = new StringBuilder();
    for (Long l : lista) {
      sb.append(l + " ");
    }
    System.out.println(sb);
    System.out.println(getQuantidade());
    System.out.println(getMedia());
    System.out.println(variancia());
    System.out.println(desvioPadrao());
  }
}
