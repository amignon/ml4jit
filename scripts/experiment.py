# -*- coding: utf-8 -*-
"""
Created on Tue Apr 11 15:13:06 2017

@author: ale
"""

import sys, time, subprocess, argparse
import numpy as np
from argparse import RawTextHelpFormatter

import config, benchmarks, featuresML

EXP1 = 0;


class Experiment():
    def __init__(self, benchmark):
        self.benchmark = benchmark
        
    def rvmCommand(self, cmd, prog, ml):
        raise NotImplementedError()        

    def agentCommand(self, cmd):
        pass
        
    def command(self, prog, ml):
        comando = []
        self.rvmCommand(comando, prog, ml)        
        self.benchmark.classPath(comando, prog)
        self.agentCommand(comando)
        self.benchmark.programCommand(comando, prog)
        return comando
        
    def programs(self):
        return self.benchmark.programs()

class ExperimentMLRVM(Experiment):
        
    def rvmCommand(self, cmd, prog, ml):
        cmd.append(config.RVM)
        cmd.append("-Xms1024M")
        cmd.append("-Xbootclasspath/p:" + self.benchmark.bootClassPath(prog))        
        cmd.append("-X:aos:enable_recompilation=false")
        cmd.append("-X:aos:initial_compiler=opt")
        cmd.append("-X:irc:" + config.ML_PLAN) 
        if ml == True:
            cmd.append("-X:opt:mode=ml")
            cmd.append("-X:opt:selector=ml4jit.rvm.SelectorML")
        # desabilita as otimizacoes de inline e reorder code
        cmd.append("-X:opt:reorder_code=false")
        cmd.append("-X:opt:reorder_code_ph=false")
        cmd.append("-X:opt:inline=false")
        cmd.append("-X:opt:inline_guarded=false")
        cmd.append("-X:opt:inline_guarded_interfaces=false")
        cmd.append("-X:opt:inline_preex=false")
        cmd.append("-X:opt:h2l_inline_new=false")
        cmd.append("-X:opt:h2l_inline_write_barrier=false")
        cmd.append("-X:opt:h2l_inline_primitive_write_barrier=false")
        cmd.append("-X:opt:osr_guarded_inlining=false")
        cmd.append("-X:opt:osr_inline_policy=false")    
        
def executeAll(exp):
    for prog in exp.programs():
        execute(exp, prog)
        
def execute(exp, prog):
    ml = True;
    tempoML = []
    tempoNML = []
    # Extrai as caracteristicas do programa        
    featuresML.execute(exp.benchmark, prog)
    config.removerArquivo(config.DIR_BASE + "/executionTime.txt")    
    config.removerArquivo(config.DIR_BASE + "/compilacao.txt")    
    for x in range(2):
        print "Executando: " + prog + " " + str(x)
        for i in range(config.EXP_SIZE):
            config.remover()
            config.removerArquivo(config.DIR_BASE + "/compilationTime.txt")
            comando = exp.command(prog, ml)
            time.sleep(config.EXP_SLEEP)
            ini = time.time()
            subprocess.call(comando)        
            fim = time.time()
            dif = fim - ini
            print "\t Tempo: " + str (dif)
            if ml:
                tempoML.append(dif)
            else:
                tempoNML.append(dif)
            processarCompilacao()
        ml = not ml                 
    print "*** Resultado " + prog + " ***"
    mml = np.mean(np.array(tempoML))
    mnml = np.mean(np.array(tempoNML))
    print "\t Com ML: " + str(np.array(tempoML)) + " : " + str(mml)
    print "\t Sem ML: " + str(np.array(tempoNML)) + " : " + str(mnml)
    print "\t %: " + str(1-(mml/mnml))
    process(prog)
    
    
def process(prog):
    out = open(config.RESULTS + "/result_" + config.ML_PLAN + "_"  + str(EXP1) + "_" + prog + ".txt", "w")
    ml, nml = processarTempoExecucao()
    out.write(str(ml) + "\n")
    out.write(str(nml) + "\n")
    ml, nml = processarTempoCompilacao()
    out.write(str(ml) + "\n")
    out.write(str(nml) + "\n")
    out.close()
        
def processarTempoExecucao():
    tempoML = []
    tempoNML = []
    f = open(config.DIR_BASE + "/executionTime.txt", "r") 
    for i in range(2):
        for j in range(config.EXP_SIZE):
            if i == 0:
                tempoML.append(long(next(f)))
            else:
                tempoNML.append(long(next(f)))
                
    mml = np.mean(np.array(tempoML))
    mnml = np.mean(np.array(tempoNML))
    print "\t Com ML: " + str(np.array(tempoML)) + " : " + str(mml)
    print "\t Sem ML: " + str(np.array(tempoNML)) + " : " + str(mnml)
    print "\t %: " + str(1-(mml/mnml))
    return tempoML, tempoNML
        
        
def processarTempoCompilacao():
    tempoML = []
    tempoNML = []
    f = open(config.DIR_BASE + "/compilacao.txt", "r") 
    for i in range(2):
        for j in range(config.EXP_SIZE):
            if i == 0:
                tempoML.append(long(next(f)))
            else:
                tempoNML.append(long(next(f)))
                
    mml = np.mean(np.array(tempoML))
    mnml = np.mean(np.array(tempoNML))
    print "*** Compilacao ***"
    print "\t Com ML: " + str(mml)
    print "\t Sem ML: " + str(mnml)
    print "\t %: " + str(1-(mml/mnml))
    return tempoML, tempoNML
    
    
def processarCompilacao():
    f = open(config.DIR_BASE + "/compilationTime.txt", "r") 
    dif = long(0)
    for line in f:
        dif += long (line.split("|")[1])
    f.close()
    out = open(config.DIR_BASE + "/compilacao.txt", "a")
    out.write(str(dif) + "\n")
    out.close()
    
  
def processarArgumentos():
    parser = argparse.ArgumentParser(description = 'Script para o Experimento', formatter_class=RawTextHelpFormatter)
    parser.add_argument("-b", default=0, type=int, help=benchmarks.help())
    parser.add_argument("-p", required=False, help="Nome do programa")   
    parser.add_argument("-o", required=False, help="Plano") 
    return parser.parse_args()
    
if __name__ == '__main__':
    args = processarArgumentos()
    print "*** Experimento ***"
    config.criarDiretorios()
    bench = benchmarks.create(args.b)
    EXP1 = args.b
    exp = ExperimentMLRVM(bench)
    #config.ML_PLAN = args.o
    if args.p is None:
        executeAll(exp)
    else:
        execute(exp, args.p)
    
