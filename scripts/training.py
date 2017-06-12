# -*- coding: utf-8 -*-
"""
Created on Mon Jan 30 12:16:36 2017

@author: ale
"""

import time, argparse, subprocess

from shutil import copyfile
from argparse import RawTextHelpFormatter

import config, benchmarks, training_opt
import tempos, features


class Treinamento():
    def __init__(self, benchmark, opt):
        self.opt = opt
        self.benchmark = benchmark
        
    def comandoRVM(self, comando, prog):
        comando.append(config.RVM)
        comando.append("-Xms1024M")
        comando.append("-Xbootclasspath/p:" + config.JAVASSIST 
            + ":" + config.AGENTE + self.benchmark.bootClassPath(prog))        

    def comandoAgente(self, comando, prog):
        cmd = "-javaagent:" + config.AGENTE + "="
        cmd += "mode=training"        
        cmd += ";path=" + config.DADOS + "/"
        cmd += ";skip=" + config.SKIP
        cmd += ";classpath=" + self.benchmark.classPathAgent(prog)
        cmd += ";sampling=" + config.SAMPLING
        cmd += ";sample_size=" + str(config.SAMPLE_SIZE)
        cmd += ";w_sampling=" + str(config.WITH_SAMPLING)
        cmd += ";wo_sampling=" + str(config.WITHOUT_SAMPLING)
        comando.append(cmd)
        
    def comando(self, prog):
        comando = []
        self.comandoRVM(comando, prog)        
        self.benchmark.classPath(comando, prog)
        self.comandoAgente(comando, prog)
        self.opt.comando(comando, self.opt.proximoVetor())
        self.benchmark.programCommand(comando, prog)
        return comando
        
    def programs(self):
        return self.benchmark.programs()

    
def execute(train):
    for prog in train.programs():
        print "Running: " + prog
        #config.removerTempos()
        features.extrair(train.benchmark, prog)
        for i in range(train.opt.length()):            
            config.remover()
            time.sleep(config.EXP_SLEEP)
            comando = train.comando(prog)   
            ini = time.time()
            subprocess.call(comando)        
            fim = time.time()            
            print "    Plano " + train.opt.vetor() + ": " + str(fim - ini)
            copyFiles(train.opt.vetor(), prog, train.opt.plano())            
        process(prog, train.opt)
        
def copyFiles(vector, prog, level):
    pattern = level + "_" + prog + "_" + vector + ".txt"
    copyfile(config.DADOS + "/tempos.txt", 
             config.TEMPOS + "/tempos_" + pattern)
    copyfile("compilationTime.txt",  
             config.TEMPOS + "/compilacao_" + pattern)    
    
def process(prog, opt):
    t = tempos.processar(opt.ref(), prog, opt.plano())
    features.processar(t, prog, opt.plano())
    
  
def processArgs():
    parser = argparse.ArgumentParser(description = 'Training Script', 
                                     formatter_class=RawTextHelpFormatter)
    parser.add_argument("-b", default=0, type=int, help=benchmarks.help())
    parser.add_argument("-o", default=-1, type=int, help=training_opt.help())
    parser.add_argument("-p", required=False, help="Nome do programa")    
    return parser.parse_args()
    
if __name__ == '__main__':
    args = processArgs()
    print "*** Training ***"
    config.criarDiretorios()
    bench = benchmarks.create(args.b)
    opt = training_opt.create(args.o)
    train = Treinamento(bench, opt)
    execute(train)