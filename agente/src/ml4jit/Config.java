package ml4jit;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Classe que armazena as configuracoes do agente.
 * Os dados de configuracao podem ser informados via arquivo ou via linha de comando.
 * O arquivo deve se chamar config.prop e deve estar no pasta de inicio da aplicacao.
 * Os dados informados via linha de comando sao definidos no comando -javaagent e sao separadas por :
 * 
 * Na classe Agente, o metodo processar deve ser chamado para definir os valores das configuracoes.
 * As configuracoes informadas via linha de comando tem prioridade sobre as definidas em arquivo.
 * 
 * @author ale
 *
 */
public class Config {
  
  // public static boolean FULL;
  
  // Todas as propriedades sao publicas e estáticas para facilitar o seu uso.
  public static boolean DEBUG = false;  
  public static boolean RECORD_CODE = false;
  public static boolean SAMPLING = false;
  public static String MODE = "counter";
  public static String SKIP;
  public static String PATH = ".";
  public static String FEATURES = "./features.csv";
  public static String CLASSPATH = null;
  public static int SAMPLE_SIZE = 100;
  public static int WITH_SAMPLING = 10;
  public static int WITHOUT_SAMPLING = 90;
  
  private static Logger log = Logger.getLogger("AGENTE");
  
  /** 
   * Processar as configuracoes.
   * @param args Dados recebidos via linha de comando.
   */
  public static void processar(String [] args) {
    //processarArquivo();
    processarLinhaComando(args);
    imprimirPropriedades();
  }
  
  private static void processarArquivo() {
    Properties p = getProperties();
    DEBUG = Boolean.parseBoolean(p.getProperty("debug", "false"));
    if (DEBUG) {
      log.setLevel(Level.FINER);
    }
    // FULL = Boolean.parseBoolean(p.getProperty("full", "false"));
    // CONTADOR = Boolean.parseBoolean(p.getProperty("contador", "false"));
    //SAMPLE = Boolean.parseBoolean(p.getProperty("amostra", "false"));
    // MAIN = p.getProperty("main", "main");
    SKIP = p.getProperty("skip", "skip.txt");    
  }
  
  private static void processarLinhaComando(String [] args) {
    if (args == null) return;
    for (int i = 0; i < args.length; i++) {
      if (args[i].startsWith("debug")) {
        DEBUG = Boolean.valueOf(getValor(args[i]));
        log.setLevel(Level.FINER);
      }
      else if (args[i].startsWith("record_code")) {
        RECORD_CODE = Boolean.valueOf(getValor(args[i]));
      }
      else if (args[i].startsWith("sampling")) {
        SAMPLING = Boolean.valueOf(getValor(args[i]));
      }
      else if (args[i].startsWith("mode")) {
        MODE = getValor(args[i]).toLowerCase();
      }
      else if (args[i].contains("skip")) {
        SKIP = getValor(args[i]);
      }
      else if (args[i].startsWith("path")) {
        PATH = getValor(args[i]);
      }
      else if (args[i].startsWith("features")) {
        FEATURES = getValor(args[i]);
      }
      else if (args[i].startsWith("classpath")) {
        CLASSPATH = getValor(args[i]);
      }
      else if (args[i].startsWith("sample_size")) {
        SAMPLE_SIZE = Integer.parseInt(getValor(args[i]));
      }
      else if (args[i].startsWith("w_sampling")) {
        WITH_SAMPLING = Integer.parseInt(getValor(args[i]));
      }
      else if (args[i].startsWith("wo_sampling")) {
        WITHOUT_SAMPLING = Integer.parseInt(getValor(args[i]));
      }
    }
  }
  
  private static void imprimirPropriedades() {
    log.info("debug            = " + DEBUG);
    log.info("record code      = " + RECORD_CODE);
    log.info("sampling         = " + SAMPLING);
    log.info("mode             = " + MODE);    
    log.info("skip             = " + SKIP);
    log.info("path             = " + PATH);
    log.info("features         = " + FEATURES);
    log.info("classpath        = " + CLASSPATH);
    log.info("sample size      = " + SAMPLE_SIZE);
    log.info("with sampling    = " + WITH_SAMPLING);
    log.info("without sampling = " + WITHOUT_SAMPLING);
  }
  
  private static String getValor(String arg) {
    return arg.substring(arg.indexOf("=") + 1);
  }
  
  private static Properties getProperties() {
    Properties prop = new Properties();
    FileInputStream file;
    try {
      file = new FileInputStream("config.prop");
      prop.load(file);
    } catch (Exception e) {      
      log.warning(e.getMessage());
    }
    return prop;    
  }
}
