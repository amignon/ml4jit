# -*- coding: utf-8 -*-
"""
Created on Thu Mar 23 11:30:15 2017

@author: ale
"""

import argparse, time, subprocess
from shutil import copyfile
from argparse import RawTextHelpFormatter

import config, benchmarks


class Contador():
    def __init__(self, benchmark):
        self.benchmark = benchmark
        
    def comandoRVM(self, comando, prog):
        comando.append(config.RVM)
        comando.append("-Xms1024M")
        comando.append("-X:aos:enable_recompilation=false")
        comando.append("-X:aos:initial_compiler=opt")
        comando.append("-X:irc:O2")
        comando.append("-Xbootclasspath/p:" + config.JAVASSIST 
            + ":" + config.AGENTE + self.benchmark.bootClassPath(prog))        

    def comandoAgente(self, comando, prog):
        cmd = "-javaagent:" + config.AGENTE + "="
        cmd += "mode=counter"        
        cmd += ";path=" + config.DADOS + "/"
        cmd += ";skip=" + config.SKIP
        cmd += ";classpath=" + self.benchmark.classPathAgent(prog)
        comando.append(cmd)
        
    def command(self, prog):
        comando = []
        self.comandoRVM(comando, prog)        
        self.benchmark.classPath(comando, prog)        
        self.comandoAgente(comando, prog)         
        self.benchmark.programCommand(comando, prog)
        return comando

def execute(bench, prog):
    print "Executando: " + prog
    config.remover()
    comando = bench.command(prog)  
    print comando
    ini = time.time()
    subprocess.call(comando)        
    fim = time.time()            
    print "    Tempo: " + str(fim - ini)
    copyfile(config.DADOS + "/counter.txt", config.DADOS + "/counter_" + prog + ".txt")
        
    
def processarArgumentos():
    parser = argparse.ArgumentParser(description = 'Script para o Contador', 
                                     formatter_class=RawTextHelpFormatter)
    parser.add_argument("-b", default=0, type=int, help=benchmarks.help())
    parser.add_argument("-p", required="True", help="Nome do programa")    
    return parser.parse_args()

if __name__ == '__main__':    
    args = processarArgumentos()
    print "*** Counter ***"
    config.criarDiretorios()
    bench = benchmarks.create(args.b)
    execute(Contador(bench), args.p)
