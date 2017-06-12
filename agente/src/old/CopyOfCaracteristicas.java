package old;

import java.util.*;

import ml4jit.Config;
import ml4jit.Heuristicas;
import ml4jit.Nanopatterns;

public class CopyOfCaracteristicas {

  private String nome;

  private boolean isStatic;
  private boolean isFinal;
  private boolean isSynch;
  private boolean isLeaf = true;
  private boolean isPrivate;

  private int bytecodes;
  private int primitive = 0;
  private int invoke = 0;
  private int array = 0;
  private int arrayLength = 0;
  private int compare = 0;
  private int branch = 0;
  private int ireturn = 0;

  List<String> codigo;
  private Nanopatterns np;

  public CopyOfCaracteristicas(String nome, String sc, Nanopatterns np) {    
    this.nome = nome;
    this.sc = sc;
    this.np = np;
    codigo = new ArrayList<String>();
  }

  public void addCode(String c) {
    codigo.add(c);
    processar(c);
  }

  public void setIsStatic(boolean b) {
    isStatic = b;
  }

  public void setIsFinal(boolean b) {
    isFinal = b;
  }

  public void setIsSynch(boolean b) {
    isSynch = b;
  }

  public void setIsPrivate(boolean b) {
    isPrivate = b;
  }

  public void setBytecodes(int bytecodes) {
    this.bytecodes = bytecodes;
  }

  public void addPrimitive() {
    primitive++;
  }

  public void addInvoke() {
    invoke++;
    isLeaf = false;
  }
  
  public void addArray() {
    array++;
  }
  
  public void addArrayLength() {
    arrayLength++;
  }
  
  public void addCompare() {
    compare++;
  }
  
  public void addBranch() {
    branch++;
  }
  
  public void addIReturn () {
    ireturn++;
  }

  public void imprimir() {
    System.out.println("Metodo: " + nome);
    System.out.println("\tBytecodes: " + bytecodes);
    System.out.println("\tStatic: " + isStatic);
    System.out.println("\tFinal: " + isFinal);
    System.out.println("\tSynch: " + isSynch);
    printPorc("Primitive", primitive);
    printPorc("Invoke", invoke);
    printCode();
  }

  public String getCaracteristicas() {
    StringBuilder sb = new StringBuilder();
    sb.append(nome);
    sb.append("|");
    sb.append(bytecodes);
    sb.append("|");
    sb.append(binario(isSynch));
    sb.append(binario(isLeaf));
    sb.append(binario(isFinal));
    sb.append(binario(isPrivate));
    sb.append(binario(isStatic));
    sb.append("|");
    sb.append(array + "," + porcentagem(array));
    sb.append("|");
    sb.append(compare + "," +porcentagem(compare));
    sb.append("|");
    sb.append(branch + "," +porcentagem(branch));
    sb.append("|");
    sb.append(primitive + "," +porcentagem(primitive));
    sb.append("|");
    sb.append(invoke + "," + porcentagem(invoke));
    //printCode();
    return sb.toString();
  }

  public void printPorc(String texto, int a) {
    System.out.println("\t" + texto + ": " + a + "  %: " + porcentagem(a));
  }

  public void printCode() {
    System.out.println("\tCodigo: " + nome);
    for (String s : codigo) {
      System.out.println("\t\t" + s);
    }
  }

  public void processar(String op) {
    if (isPrimitive(op)) {
      this.addPrimitive();
    } else if (op.startsWith("invoke")) {
      this.addInvoke();
    }
    else if (op.startsWith("aload") || op.startsWith("astore") ) {
      this.addArray();
    }
    else if (op.startsWith("fcmpg") || op.startsWith("fcmpl")) {
      this.addCompare();
    }
    else if (op.startsWith("xxx")) { // compare e branch
      
    }
    else if (op.startsWith("if")) { // branch
      this.addBranch();
    }
    else if (op.startsWith("goto")) {
      this.addBranch();
    }
    else if (op.startsWith("arraylength")) {
      this.addArrayLength();
    } 
    else if (isIReturn(op)) {
      this.addIReturn();
    }
  }
  
  private boolean isPrimitive (String op) {
    return (op.startsWith("iadd") 
        || op.startsWith("isub"));
  }
  
  private boolean isIReturn (String op) {
    return op.contains("return");
  }

  private float porcentagem(int i) {
    return (float) i / bytecodes;
  }
  
  private String binario (boolean b) {
    return (b)?"1":"0";
  }
 
  /**
   * Ignora se a quantidade de return for maior que 20%. Usado para get e set.
   * @return
   */
  public boolean ignorar () {
    if (Config.FULL) {
      return false;
    }
//    np.imprimir(isThread());
    if (nome.contains(Config.MAIN)) return false;
    if (isThread()) return true;
    if (Heuristicas.isGet(np)) return true;
    if (Heuristicas.isSet(np)) return true;
//    if (Heuristicas.isGet2(np)) return true;
    if (np.sameName && np.straightLine) return true;
    return bytecodes < 1000;
    /**
    if (branch == 0 && isLeaf) {
      System.out.println("[CARAC] PADRAO: " + this.nome + " ");
      return true;
    }
    //System.out.println("[CARAC] " + this.nome + " " + porcentagem(ireturn));
    if (nome.contains("main")) return false;
    return bytecodes < 50;
    //return (porcentagem(ireturn) >= 0.19999);
    //return false;
     */
  }
  
  private boolean isThread () {
    // TODO Verificar hierarquia
    if (sc == null) return false;
    return (sc.equals("java.lang.Thread") && nome.contains("run("));
  }
  
  public boolean recursao () {
    String str = nome.substring(0, nome.indexOf("("));
    String str2 = nome.substring(nome.indexOf("("));
//    System.out.println("[CARAC] Testando: " + str + "  " + str2);
    for (String s : codigo) {
      if (s.contains("invoke")) {
        if (s.contains(str + "(" + str2)) return true;
      }
    }
    return false;
  }

  
  public String getNome () {
    return nome;
  }
}
