package teste;

public class Funcoes {
  
  public static void executar () {
    f1();
  }
  
  public static void f1() {
    
    // randomly sleeps between 500ms and 1200s
    // long randomSleepDuration = (long) (500 + Math.random() * 700);
    // System.out.printf("Sleeping for %d ms ..\n", randomSleepDuration);
    // Thread.sleep(randomSleepDuration);
//System.out.println ("INICIO");
  for (int i = 0; i < 1000 * 1000; i++) {
    for (int j = 0; j < 1000; j++) {
      for (int k = 0; k < 1; k++) {
        int a = 10 + 20;
        int b = a * 3;
        int c = a + b;
      }
    }
  }
//System.out.println ("FIM");
}

}
