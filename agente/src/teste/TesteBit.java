package teste;

import java.util.logging.Logger;

public class TesteBit {
  
  public static void main (String [] args) {
    System.out.println(getBit(5, 1));
  }
  
  
  public static boolean getBit(int n, int p) {
    return ((n >> p) & 1)==1;
  }

}
