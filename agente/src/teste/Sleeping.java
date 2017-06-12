package teste;

public class Sleeping {
    
    public void m1() throws InterruptedException {
        
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

    public void m2() throws InterruptedException {
        
        // randomly sleeps between 500ms and 1200s
        // long randomSleepDuration = (long) (500 + Math.random() * 700);
        // System.out.printf("Sleeping for %d ms ..\n", randomSleepDuration);
        // Thread.sleep(randomSleepDuration);
	//System.out.println ("INICIO");
		for (int i = 0; i < 1000 * 1000; i++) {
			for (int j = 0; j < 100; j++) {
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
