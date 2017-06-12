package ml4jit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ml4jit.rvm.Selector;

public class SelecionadorAgente extends Selector {
  
  public static Map<Integer, Integer> ultimo = new HashMap<Integer, Integer>();
  public static Map<Integer, ArrayList<Integer>> todos = new HashMap<Integer, ArrayList<Integer>>();

  @Override
  public int select(String metodo) {
    int b = new Random().nextInt(2048);
    System.out.println("Selecionador Agente: " + metodo + " : " + b);
    int id = metodo.hashCode();
    atualizar(id, b);
    ultimo.put(id, b);
    return b;
  }
  
  public void atualizar(int mid, int b) {
    ArrayList<Integer> l = todos.get(mid);
    if (l == null) {
      l = new ArrayList<Integer>();
      l.add(b);
      todos.put(mid, l);
    }
    else {
      l.add(ultimo.get(mid));
    }    
  }
  
  public static int obterUltimo(int mid) {
    ArrayList<Integer> l = todos.get(mid);
    if (l == null) {
      return -1;
    }
    else {
      return l.get(l.size()-1);
    }
  }
}
