package ml4jit;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.InstructionPrinter;
import javassist.bytecode.MethodInfo;

public class Codigo {
  
  private String nomeMetodo;
  private Features carac;
  
  private List<String> codigo;
  private List<Integer> posicao;
  
  public Codigo(String nomeMetodo) {
    this.nomeMetodo = nomeMetodo;
    this.carac = new Features(nomeMetodo);
    this.codigo = new ArrayList<String>();
    this.posicao = new ArrayList<Integer>();
  }
  
  public Features getCaracteristicas() {
    return carac;
  }
  
  public boolean processar(CtMethod m) {
    MethodInfo info = m.getMethodInfo2();
    ConstPool pool = info.getConstPool();
    CodeAttribute code = info.getCodeAttribute();
    if (code == null) {
      return false;
    }
    // bytecodes, locals e exceptions
    carac.setBytecodes(code.getCodeLength());
    carac.setMaxLocals(code.getMaxLocals());
    carac.setHasExceptions(code.getExceptionTable().size() > 0);
    // modificadores do metodo
    processarModificadores(m);
    // analisa cada instrucao do codigo
    CodeIterator iterator = code.iterator();
    while (iterator.hasNext()) {
      int pos;
      try {
        pos = iterator.next();
      } catch (BadBytecode e) {
        System.out.println(e.getMessage());
        return false;
      }
      String inst = InstructionPrinter.instructionString(iterator, pos, pool);
      this.codigo.add(inst);
      this.posicao.add(pos);
      processarInstrucao(inst);
    }
    return true;
  }
  
  public void imprimir() {
    StringBuilder sb = new StringBuilder();
    sb.append("### " + nomeMetodo + " ===\n");
    sb.append(carac.getCaracteristicas());
    sb.append("\n");
    sb.append(codigo()); 
    sb.append("======\n");
    try {
      PrintWriter pw = new PrintWriter(new FileWriter("codigos.txt", true));
      pw.write(sb.toString());
      pw.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  
  private String codigo() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < codigo.size(); i++) {
      sb.append("\t" + posicao.get(i) + ": " + codigo.get(i) + "\n");
    }
    return sb.toString();
  }  
  
  private void processarInstrucao(String inst) {
    
    if (isArithmetic(inst)) {
      carac.arithmetic();
    }    
    if (isArrayReader(inst)) {
      carac.arrayLoad();
    } else if (isArrayWriter(inst)) {
      carac.arrayStore();
    } else if (isCompare(inst)) {
      carac.compare();
    } else if (isBranch(inst)) {
      carac.branch();
    } else if (isJSR(inst)) {
      carac.jsr();
    } else if (isSwitcher(inst)) {
      carac.switcher();
    } else if (isPut(inst)) {
      carac.put();
    } else if (isGet(inst)) {
      carac.get();
    } else if (inst.startsWith("invoke")) {
      carac.invoke();
    } else if (inst.startsWith("new")) {
      carac.numNew();
    } else if (inst.startsWith("arrayLength")) {
      carac.arrayLength();
    } else if (inst.startsWith("athrow")) {
      carac.athrow();
    } else if (inst.startsWith("checkcast")) {
      carac.checkcast();
    } else if (inst.startsWith("monitor")) {
      carac.monitor();
    } else if (inst.startsWith("multianewarray")) {
      carac.multi_newarray();
    } else if (isConversion(inst)) {
      carac.conversao();
    }     
  }
  
  private void processarModificadores(CtMethod m) {
    int mod = m.getModifiers();
    carac.setIsSynch(Modifier.isSynchronized(mod));
    carac.setIsFinal(Modifier.isFinal(mod));
    carac.setIsPrivate(Modifier.isPrivate(mod));
    carac.setIsStatic(Modifier.isStatic(mod));
  }
  
  private boolean isArithmetic(String codigo) {
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
  
  private boolean isCompare(String codigo) {
    return codigo.startsWith("dcmpg") || codigo.startsWith("dcmpl")
        || codigo.startsWith("fcmpg") || codigo.startsWith("fcmpl")
        || codigo.startsWith("lcmp");
  }
  
  private boolean isBranch(String codigo) {
    return codigo.startsWith("if") || codigo.startsWith("goto");
  }
  
  private boolean isJSR(String codigo) {
    return codigo.startsWith("jsr");
  }
  
  private boolean isSwitcher(String codigo) {
    return codigo.startsWith("lookupswitch") || codigo.startsWith("tableswitch");
  }
  
  private boolean isPut(String codigo) {
    return codigo.startsWith("putstatic") || codigo.startsWith("putfield");
  }
  
  private boolean isGet(String codigo) {
    return codigo.startsWith("getstatic") || codigo.startsWith("getfield");
  }
  
  private boolean isConversion(String codigo) {
    return codigo.startsWith("d2f") || codigo.startsWith("d2i") || codigo.startsWith("d2l")
        || codigo.startsWith("f2d") || codigo.startsWith("f2i") || codigo.startsWith("f2l")
        || codigo.startsWith("i2b") || codigo.startsWith("i2c") || codigo.startsWith("i2d")
        || codigo.startsWith("i2f") || codigo.startsWith("i2l") || codigo.startsWith("i2s")
        || codigo.startsWith("l2d") || codigo.startsWith("l2f") || codigo.startsWith("l2i");
  }
}
