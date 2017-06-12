package ml4jit;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;

import ml4jit.counter.CounterTransformer;
import ml4jit.ml.InstrumentalizadorML;
import ml4jit.training.TrainingSamplingTransformer;
import ml4jit.training.TrainingTransformer;
import ml4jit.training.TrainingTransformerTest;


public class TransformerFactory {
  
  public static ClassFileTransformer create(String mode) {    
    if (mode.equals("counter")) {
      return new CounterTransformer(new File(Config.SKIP));
    }
    else if (mode.equals("training")) {
      if (Config.SAMPLING) {
        return new TrainingSamplingTransformer(new File(Config.SKIP));
      }
      else {
        return new TrainingTransformer(new File(Config.SKIP));
      }
    }
    else if (mode.equals("ml")) {
      return new InstrumentalizadorML(new File(Config.SKIP));
    }
    // return new InstrumentalizadorPadrao(new File(Config.SKIP));
    return new CounterTransformer(new File(Config.SKIP));
  }
}