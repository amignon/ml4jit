package ml4jit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class Heuristicas {
  
  static Map<String,String> padroes = new HashMap<String,String>();
  
  static {   
    padroes.put("??1?? ???? ??? ????? ??", "#RECURSIVE# ");
    padroes.put("???1? ???? 1?? ????? ??", "#SAME NAME# ");
    padroes.put("????? ???? ??? ????? 1?", "#THREAD# ");
    
    padroes.put("?0??? ?1?? ?0? 1???? ?0", "#ACCESS# ");    
    padroes.put("?1??1 ??1? 10? 1???? ?0", "#MODIFY# ");
  }
  
  public static String processar (Nanopatterns np) {
    
    
//    padroes.put("11??? ???? 1?? ????? ?0", "#PROCEDURE# ");
    
//    padroes.put("00001 0100 100 10000 ?0", "#ACCESS# ");
//    padroes.put("?0??1 ?1?? 1?? 1???? ?0", "#GET# ");
//    padroes.put("?0??1 ?1?? 00? 1???? ?0", "#GET WITH IF# ");
//    padroes.put("10??0 ?1?? 1?? 1???? ?0", "#GET CALL# ");
    
    StringBuilder sb = new StringBuilder();
    Iterator<String> it = padroes.keySet().iterator();
    while (it.hasNext()) {
      String p = it.next();
      if (igual(p, np.texto())) {      
        sb.append(padroes.get(p));
      }
    }   
    
    // if (isGet(np)) sb.append("#GET# ");
    // if (isGet2(np)) sb.append("#GET2# ");
    // if (isGet3(np)) sb.append("#GET3# ");
    // if (isGetCall(np)) sb.append("#GETCALL# ");
    // if (isSet(np)) sb.append("#SET# ");
    // if (looping(np)) sb.append("#LOOPING# ");
    // if (np.multilooping) sb.append("#MULTI LOOPING# ");
    // if (np.recursive) sb.append("#RECURSIVE# ");
    // if (np.sameName && np.straightLine) sb.append("#SAME NAME# ");
    // if (np.isThread()) sb.append("#THREAD# ");
    // if (np.straightLine && !np.isLeaf && np.localReader) sb.append("#CALL# ");    
    return sb.toString();
  }
  
  public static boolean igual (String padrao, String texto) {
    for (int i = 0; i < padrao.length(); i++) {
      if (padrao.charAt(i) == '?') continue;
      if (padrao.charAt(i) == texto.charAt(i)) continue;
      return false;
      
    }
    return true;
  }
  
  public static boolean ignorar(Nanopatterns np) {
    Logger log = Logger.getLogger("AGENTE");
    String s = processar(np);
    if (s.length() > 0) {
      log.info(np.getNome() + " : " + s);
      return true;
    }
    return false;
  }
  
  
  
  public static boolean isGet (Nanopatterns np) {
    // TODO noReturn = false?
    // 1???1 ?1?? 1?? ?????
    if (np.noParams && np.isLeaf && np.fieldReader && np.straightLine)
      return true;
    return false;
  }
  
  public static boolean isGet2 (Nanopatterns np) {
    if (np.noParams && !np.isLeaf && np.fieldReader && !np.straightLine && !np.looping)
      return true;
    return false;
  }
  
  public static boolean isGet3 (Nanopatterns np) {
    if (np.noParams && np.isLeaf && np.fieldReader && !np.straightLine && !np.looping)
      return true;
    return false;
  }
  
  
  public static boolean isGetCall (Nanopatterns np) {
    if (np.noParams && np.fieldReader && np.straightLine && np.localReader && !np.isLeaf) {
      return true;
    }
    return false;
  }
  
  
  public static boolean isSet (Nanopatterns np) {
    // TODO arrayCreator = false?
    if (np.noReturn && np.isLeaf && np.fieldWriter && np.straightLine)
      return true;
    return false;
  }
  
  public static boolean looping (Nanopatterns np) {
    return np.looping;
  }

}
