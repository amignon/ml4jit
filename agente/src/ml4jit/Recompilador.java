package ml4jit;

import ml4jit.rvm.RVMRecompiler;

public class Recompilador {
  
  public static boolean recompilar(int mid) {
    System.out.println("[REC] " + mid + ": " + Mapeador.getNomeMetodo(mid));
    return RVMRecompiler.recompilar(Mapeador.getNomeMetodo(mid));
  }
}
