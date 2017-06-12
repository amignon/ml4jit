package ml4jit;


public class Features {

  private String nome;

  private int bytecodes;
  private int maxLocals;
  
  private boolean isSynch;
  private boolean hasExceptions;
  private boolean isFinal;
  private boolean isPrivate;
  private boolean isStatic;
  
  // array loads 
  private int arrayLoad = 0;
  // array stores
  private int arrayStore = 0;
  // operacoes aritmeticas
  private int arithmetic = 0;
  // compares
  private int compare = 0;
  // branches
  private int branch = 0;
  // jsr
  private int jsr = 0;
  // switch
  private int switcher = 0;
  // put
  private int put = 0;
  // get
  private int get = 0;
  // invoke  
  private int invoke = 0;
  // new 
  private int numNew = 0;
  // array length  
  private int arrayLength = 0;
  // athrow
  private int athrow = 0;
  // checkcast
  private int checkcast = 0;
  // monitor
  private int monitor = 0;  
  // multi newarray
  private int multi_newarray = 0;
  // conversoes
  private int conversao = 0;

  public Features(String nome) {    
    this.nome = nome;
  }
  
  public void setBytecodes(int bytecodes) {
    this.bytecodes = bytecodes;
  }

  public void setMaxLocals(int maxLocals) {
    this.maxLocals = maxLocals;
  }
  
  public void setIsSynch(boolean b) {
    this.isSynch = b;
  }
  
  public void setHasExceptions(boolean hasExceptions) {
    this.hasExceptions = hasExceptions;
  }
  
  public void setIsFinal(boolean b) {
    this.isFinal = b;
  }
  
  public void setIsPrivate(boolean b) {
    this.isPrivate = b;
  }
  
  public void setIsStatic(boolean b) {
    this.isStatic = b;
  }  
  
  public void arrayLoad() {
    this.arrayLoad++;
  }
  
  public void arrayStore() {
    this.arrayStore++;
  }
  
  public void arithmetic() {
    this.arithmetic++;
  }
  
  public void compare() {
    this.compare++;
  }
  
  public void branch() {
    this.branch++;
  }
  
  public void jsr() {
    this.jsr++;
  }
  
  public void switcher() {
    this.switcher++;
  }
  
  public void put() {
    this.put++;
  }
  
  public void get() {
    this.get++;
  }
  
  public void invoke() {
    invoke++;
  }
  
  public void numNew() {
    this.numNew++;
  }
  
  public void arrayLength() {
    this.arrayLength++;
  }
  
  public void athrow() {
    this.athrow++;
  }
  
  public void checkcast() {
    this.checkcast++;
  }
  
  public void monitor() {
    this.monitor++;
  }
  
  public void multi_newarray() {
    this.multi_newarray++;
  }
  
  public void conversao() {
    this.conversao++;
  }
  

  public String getCaracteristicas() {
    StringBuilder sb = new StringBuilder();
    sb.append(nome.hashCode()).append(",");
    sb.append(nome).append(",");
    sb.append(bytecodes).append(",");
    sb.append(maxLocals).append(",");
    sb.append(binario(isSynch)).append(",");
    sb.append(binario(hasExceptions)).append(",");
    sb.append(binario(invoke == 0)).append(",");
    sb.append(binario(isFinal)).append(",");
    sb.append(binario(isPrivate)).append(",");
    sb.append(binario(isStatic)).append(",");
    sb.append(porcentagem(arrayLoad)).append(",");
    sb.append(porcentagem(arrayStore)).append(",");
    sb.append(porcentagem(arithmetic)).append(",");
    sb.append(porcentagem(compare)).append(",");
    sb.append(porcentagem(branch)).append(",");
    // sb.append(porcentagem(jsr)).append(",");
    sb.append(porcentagem(switcher)).append(",");
    sb.append(porcentagem(put)).append(",");
    sb.append(porcentagem(get)).append(",");
    sb.append(porcentagem(invoke)).append(",");
    sb.append(porcentagem(numNew)).append(",");
    sb.append(porcentagem(arrayLength)).append(",");
    sb.append(porcentagem(athrow)).append(",");
    sb.append(porcentagem(checkcast)).append(",");
    sb.append(porcentagem(monitor)).append(",");
    sb.append(porcentagem(multi_newarray)).append(",");
    sb.append(porcentagem(conversao));
    return sb.toString();
  }
  
  public static String getCabecalho() {
    StringBuilder sb = new StringBuilder();
    sb.append("ID").append(",");
    sb.append("Nome").append(",");
    sb.append("Bytecodes").append(",");
    sb.append("Locals").append(",");
    sb.append("Synch").append(",");
    sb.append("Exceptions").append(",");
    sb.append("Leaf").append(",");
    sb.append("Final").append(",");
    sb.append("Private").append(",");
    sb.append("Static").append(",");
    sb.append("Array_Load").append(",");
    sb.append("Array_Store").append(",");
    sb.append("Arithmetic").append(",");
    sb.append("Compare").append(",");
    sb.append("Branch").append(",");
    // sb.append("JSR").append(",");
    sb.append("Switch").append(",");
    sb.append("Put").append(",");
    sb.append("Get").append(",");
    sb.append("Invoke").append(",");
    sb.append("New").append(",");
    sb.append("ArrayLength").append(",");
    sb.append("Athrow").append(",");
    sb.append("Checkcast").append(",");
    sb.append("Monitor").append(",");
    sb.append("Multi_newarray").append(",");
    sb.append("Conversion");
    return sb.toString();
  }

  private float porcentagem(int i) {
    return (float) i / bytecodes;
  }
  
  private String binario (boolean b) {
    return (b)?"1":"0";
  }
 
  public String getNome () {
    return nome;
  }

}
