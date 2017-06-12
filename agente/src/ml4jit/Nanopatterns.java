package ml4jit;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Nanopatterns {

  private String nome;
  private String superClasse;
  private String retorno;
  private List<String> codigo;
  private List<Integer> posicao;
  private int instrsSinceLastALOAD0 = Integer.MAX_VALUE;
  private int bytecodes = 0;

  // calling
  public boolean noParams = false;
  public boolean noReturn = false;
  public boolean recursive = false;
  public boolean sameName = false;
  public boolean isLeaf = true;
  // OO
  public boolean objectCreator = false;  
  public boolean fieldReader = false;
  public boolean fieldWriter = false;
  public boolean staticFieldReader = false;
  public boolean staticFieldWriter = false;
  public boolean typeManipulator = false;
  // control flow
  public boolean straightLine = true;
  public boolean looping = false;
  public boolean multilooping = false;
  public boolean throwsExceptions = false;
  // data flow
  public boolean localReader = false;
  public boolean localWriter = false;
  public boolean arrayCreator = false;
  public boolean arrayReader = false;
  public boolean arrayWriter = false;
  // arithmetic
  public boolean arithmetic = false;

  public Nanopatterns(String nome, String retorno, String superClasse) {
    this.nome = nome;
    this.retorno = retorno;
    this.superClasse = superClasse;
    this.codigo = new ArrayList<String>();
    this.posicao = new ArrayList<Integer>();
    this.noParams();
    this.noReturn();
  }
  
  public String getNome() {
    return nome;
  }
  
  public boolean isThread() {
    // TODO Verificar hierarquia
    if (superClasse == null) return false;
    return (superClasse.equals("java.lang.Thread") && nome.contains("run("));
  }
  
  public void setBytecodes (int bc) {
    this.bytecodes = bc;
  }

  public void throwsExceptions(int i) {
    if (i > 0) {
      throwsExceptions = true;
    }
  }

  public void adicionarCodigo(int pos, String codigo) {
    this.posicao.add(pos);
    this.codigo.add(codigo);
    if (isVarInsn(codigo) || isInsn(codigo)) {
      instrsSinceLastALOAD0++;
    }
    if (codigo.startsWith("aload_0")) {      
      instrsSinceLastALOAD0 = 0;
    }    
    
    
    if (arithmetic(codigo)) {
      arithmetic = true;
    }
    
    if (codigo.startsWith("invoke")) {
      isLeaf = false;
      recursive(codigo);
    } else if (isJumpInsn(codigo) || isSwitcher(codigo)) {
      straightLine = false;
      // looping
      looping(codigo);
    } else if (isLocalReader(codigo)) {
      localReader = true;
    } else if (isLocalWriter(codigo)) {
      localWriter = true;
    } else if (codigo.startsWith("newarray") || codigo.startsWith("anewarray")) {
      arrayCreator = true;
    } else if (isArrayReader(codigo)) {
      arrayReader = true;
    } else if (isArrayWriter(codigo)) {
      arrayWriter = true;
    } else if (codigo.startsWith("new")) {
      objectCreator = true;
    } else if (isFieldInsn(codigo)) {
      instrsSinceLastALOAD0++;
      processField(codigo);
    } else if (codigo.startsWith("checkcast") || codigo.startsWith("instanceof")) {
      typeManipulator = true;
    }
  }

  private void noParams() {
    if (nome.endsWith("()")) {
      noParams = true;
    }
  }

  private void noReturn() {
    if (retorno.equals("void")) {
      noReturn = true;
    }
  }

  private void recursive(String codigo) {
    String s = codigo.substring(codigo.indexOf("Method ") + 7);
    //System.out.println("REC: " + s);
    String dono = s.substring(0, s.lastIndexOf("."));
    //System.out.println("REC: " + dono);
    String m = s.substring(s.lastIndexOf(".") + 1, s.indexOf("("));
    //System.out.println("REC: " + m);
    String desc = s.substring(s.indexOf("(") + 1);
    desc = desc.substring(0, desc.indexOf(")") + 1);
    //System.out.println("REC: " + desc);
    String t = dono + "." + m + desc;
    //System.out.println("REC: N " + nome);
    //System.out.println("REC: T " + t);

    if (nome.equals(t)) {
      recursive = true;
    } else if (nome.contains(m + "(")) {
      sameName = true;
    }
  }

  public synchronized void imprimir() {
    StringBuilder sb = new StringBuilder();
    sb.append("### " + nome + " " + retorno + " : " + bytecodes + " ===\n");
    sb.append(texto());
    sb.append("\n");
    sb.append(codigo()); 
    sb.append("HEURISTICAS: " + Heuristicas.processar(this) + "\n");
    sb.append("======\n");
    try {
      PrintWriter pw = new PrintWriter(new FileWriter("codigos.txt", true));
      pw.write(sb.toString());
      pw.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  
  public String texto() {
    StringBuilder sb = new StringBuilder();    
    sb.append(valor(noParams));
    sb.append(valor(noReturn));
    sb.append(valor(recursive));
    sb.append(valor(sameName));
    sb.append(valor(isLeaf));
    sb.append(" ");
    sb.append(valor(objectCreator));
    sb.append(valor(fieldReader));
    sb.append(valor(fieldWriter));
    //sb.append(valor(staticFieldReader));
    //sb.append(valor(staticFieldWriter));
    sb.append(valor(typeManipulator));
    sb.append(" ");
    sb.append(valor(straightLine));
    sb.append(valor(looping));
    sb.append(valor(throwsExceptions));
    sb.append(" ");
    sb.append(valor(localReader));
    sb.append(valor(localWriter));
    sb.append(valor(arrayCreator));
    sb.append(valor(arrayReader));
    sb.append(valor(arrayWriter));
    sb.append(" ");
    sb.append(valor(isThread()));
    sb.append(valor(arithmetic));
    return sb.toString();
  }

  private String codigo() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < codigo.size(); i++) {
      sb.append("\t" + posicao.get(i) + ": " + codigo.get(i) + "\n");
    }
    return sb.toString();
  }

  private String valor(boolean p) {
    return p ? "1" : "0";
  }
  
  private boolean arithmetic(String codigo) {
    return codigo.startsWith("fadd") || codigo.startsWith("fdiv") || codigo.startsWith("frem")
        || codigo.startsWith("fmul") || codigo.startsWith("fneg") || codigo.startsWith("fsub")
        || codigo.startsWith("dadd") || codigo.startsWith("ddiv") || codigo.startsWith("drem")
        || codigo.startsWith("dmul") || codigo.startsWith("dneg") || codigo.startsWith("dsub")        
        || codigo.startsWith("ladd") || codigo.startsWith("ldiv") || codigo.startsWith("lrem")
        || codigo.startsWith("lmul") || codigo.startsWith("lneg") || codigo.startsWith("lsub")        
        || codigo.startsWith("iadd") || codigo.startsWith("idiv") || codigo.startsWith("iinc")
        || codigo.startsWith("imul") || codigo.startsWith("ineg") || codigo.startsWith("irem")        
        || codigo.startsWith("isub");
  }
  

  private boolean isJumpInsn(String codigo) {
    if (codigo.startsWith("if") || codigo.startsWith("goto") || codigo.startsWith("jsr"))
      return true;
    return false;
  }

  private boolean isSwitcher(String codigo) {
    if (codigo.startsWith("lookupswitch") || codigo.startsWith("tableswitch"))
      return true;
    return false;
  }

  static int ul = 0;
  private void looping(String codigo) {
    if (isSwitcher(codigo)) return;
    int label = Integer.valueOf(codigo.substring(codigo.indexOf(" ") + 1).trim());
    if (!looping) {
      looping = posicao.contains(label);
      ul = label;
    }
    else {
      if (posicao.contains(label) && label < ul) {
        multilooping = true;
        ul = label;
      }
    }
  }

  private boolean isLocalReader(String codigo) {
    return codigo.startsWith("iload") || codigo.startsWith("lload") || codigo.startsWith("fload")
        || codigo.startsWith("dload") || codigo.startsWith("aload");
  }

  private boolean isLocalWriter(String codigo) {
    return codigo.startsWith("istore") || codigo.startsWith("lstore")
        || codigo.startsWith("fstore") || codigo.startsWith("dstore")
        || codigo.startsWith("astore");
  }

  private boolean isArrayReader(String codigo) {
    return codigo.startsWith("iaload") || codigo.startsWith("v1_2") || codigo.startsWith("laload")
        || codigo.startsWith("v1_3") || codigo.startsWith("faload") || codigo.startsWith("v1_4")
        || codigo.startsWith("daload") || codigo.startsWith("v1_5") || codigo.startsWith("aaload")
        || codigo.startsWith("v1_6") || codigo.startsWith("baload") || codigo.startsWith("v1_7")
        || codigo.startsWith("caload") || codigo.startsWith("saload");
  }

  private boolean isArrayWriter(String codigo) {
    return codigo.startsWith("iastore") || codigo.startsWith("lastore")
        || codigo.startsWith("fastore") || codigo.startsWith("dastore")
        || codigo.startsWith("aastore") || codigo.startsWith("bastore")
        || codigo.startsWith("castore") || codigo.startsWith("sastore");
  }

  private boolean isVarInsn(String codigo) {
    return codigo.startsWith("iload") || codigo.startsWith("lload") || codigo.startsWith("fload")
        || codigo.startsWith("dload") || codigo.startsWith("aload") || codigo.startsWith("istore")
        || codigo.startsWith("lstore") || codigo.startsWith("fstore")
        || codigo.startsWith("dstore") || codigo.startsWith("astore") || codigo.startsWith("ret")
        || codigo.startsWith("sastore");
  }

  private boolean isInsn(String codigo) {
    String [] lista = {"NOP", "ACONST_NULL", "ICONST_M1", "ICONST_0", "ICONST_1", "ICONST_2", "ICONST_3", 
        "ICONST_4", "ICONST_5", "LCONST_0", "LCONST_1", "FCONST_0", "FCONST_1", "FCONST_2", "DCONST_0", 
        "DCONST_1", "IALOAD", "LALOAD", "FALOAD", "DALOAD", "AALOAD", "BALOAD", "CALOAD", "SALOAD", "IASTORE", 
        "LASTORE", "FASTORE", "DASTORE", "AASTORE", "BASTORE", "CASTORE", "SASTORE", "POP", "POP2", "DUP", "DUP_X1",
        "DUP_X2", "DUP2", "DUP2_X1", "DUP2_X2", "SWAP", "IADD", "LADD", "FADD", "DADD", "ISUB", "LSUB", "FSUB", 
        "DSUB", "IMUL", "LMUL", "FMUL", "DMUL", "IDIV", "LDIV", "FDIV", "DDIV", "IREM", "LREM", "FREM", "DREM", 
        "INEG", "LNEG", "FNEG", "DNEG", "ISHL", "LSHL", "ISHR", "LSHR", "IUSHR", "LUSHR", "IAND", "LAND", "IOR", 
        "LOR", "IXOR", "LXOR", "I2L", "I2F", "I2D", "L2I", "L2F", "L2D", "F2I", "F2L", "F2D", "D2I", "D2L", "D2F",
        "I2B", "I2C", "I2S", "LCMP", "FCMPL", "FCMPG", "DCMPL", "DCMPG", "IRETURN", "LRETURN", "FRETURN", "DRETURN", 
        "ARETURN", "RETURN", "ARRAYLENGTH", "ATHROW", "MONITORENTER", "MONITOREXIT"};
    for (int i = 0; i < lista.length; i++) {
      if (codigo.startsWith(lista[i].toLowerCase())) {
        return true;
      }
    }
    return false;
  }

  private boolean isFieldInsn(String codigo) {
    return codigo.startsWith("getstatic") || codigo.startsWith("putstatic")
        || codigo.startsWith("getfield") || codigo.startsWith("putfield");
  }

  private void processField(String codigo) {
//    System.out.println("FIELD: " + instrsSinceLastALOAD0);
    if (codigo.startsWith("getfield")) {
      fieldReader = true;      
//      if (instrsSinceLastALOAD0 == 1) {
//        this.thisInstanceFieldReader = true;
//      } else {
//        this.otherInstanceFieldReader = true;
//      }
    } else if (codigo.startsWith("putfield")) {
      fieldWriter = true;
//      if (instrsSinceLastALOAD0 == 2) {
//        this.thisInstanceFieldWriter = true;
//      } else {
//        this.otherInstanceFieldWriter = true;
//      }
    } else if (codigo.startsWith("getstatic")) {
      //this.staticFieldReader = true;
      fieldReader = true;
    } else if (codigo.startsWith("putstatic")) {
      //this.staticFieldWriter = true;
      fieldWriter = true;
    }
  }
}
