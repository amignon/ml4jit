# -*- coding: utf-8 -*-
"""
Created on Wed Feb 15 13:26:42 2017

@author: ale
"""

import sys
import time
import subprocess
from numpy import binary_repr
import numpy as np
import socket

import config
import servidor
import featuresML
from experimento import bench_exp
from experimento import lab_exp
        
def executarComando(bench):
    for prog in bench.benchmarks():        
        ml = True;
        tempoML = []
        tempoNML = []
        # Extrai as caracteristicas do programa        
        featuresML.executar(bench, prog)
        config.removerArquivo(config.DIR_BASE + "/tempo_execucao.txt")
        config.removerArquivo(config.DIR_BASE + "/log_rvm.txt")
        for x in range(2):
            print "Executando: " + prog + " " + str(x)
            for i in range(config.EXP_SIZE):
                config.remover()
                comando = bench.comando(prog, ml)
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
            ml = not ml            
        print "*** Resultado " + prog + " ***"
        mml = np.mean(np.array(tempoML))
        mnml = np.mean(np.array(tempoNML))
        print "\t Com ML: " + str(np.array(tempoML)) + " : " + str(mml)
        print "\t Sem ML: " + str(np.array(tempoNML)) + " : " + str(mnml)
        print "\t %: " + str(1-(mml/mnml))
        processarTempoExecucao()
        
def processarTempoExecucao():
    tempoML = []
    tempoNML = []
    f = open(config.DIR_BASE + "/tempo_execucao.txt", "r") 
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
        

def inicializarServidor():
    servidor.iniciar()   

def finalizarServidor():
   
   HOST = '127.0.0.1'     # Endereco IP do Servidor
   PORT = 10002            # Porta que o Servidor esta
   tcp = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
   dest = (HOST, PORT)
   tcp.connect(dest)
   tcp.send("EXIT");
   tcp.close()
   
def processarArgumentos():
    """
    Retorna opcao de benchmarks, opcao de laboratorio
    """
    args = sys.argv
    if len(args) > 1:    
        ben = int(args[1])
    else:
        ben = 0
    if len(args) > 2:
        lab = int(args[2])
    else:
        lab = 0
    return ben, lab
    
def criarLab(op):
    return lab_exp.ExperimentoLab02()
    
def criarBenchmark(op, opLab):
    lab = criarLab(opLab)    
    if op == 1:
        return bench_exp.BenchExpMLRVM(bench_exp.BenchExpDaCapo(), lab)
    elif op == 2:
        return bench_exp.BenchExpMLRVM(bench_exp.BenchExpSPEC(), lab)
    elif op == 3:
        return bench_exp.BenchExpMLRVM(bench_exp.BenchExpJGFS2(), lab)
    else:
        return bench_exp.BenchExpMLRVM(bench_exp.BenchExpOne(), lab)
    
if __name__ == '__main__':    
    print ("Experimento")       
    config.criarDiretorios()
    b, l = processarArgumentos()
    bench = criarBenchmark(b, l)
    try:
        #inicializarServidor()
        executarComando(bench)
    except:
        raise
    finally:
        pass
        #finalizarServidor()
