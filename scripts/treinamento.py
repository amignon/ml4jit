# -*- coding: utf-8 -*-
"""
Created on Mon Jan 30 12:16:36 2017

@author: ale
"""

import time, argparse, subprocess

from shutil import copyfile
from argparse import RawTextHelpFormatter

import config, benchmarks, trainingOptimizations
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

    
def executarComando(train):
    for prog in train.programs():
        print "Executando: " + prog
        #config.removerTempos()
        # Extrai as caracteristicas dos metodos dos programas
        features.extrair(train.benchmark, prog)
        for i in range(train.opt.length()):            
            config.remover()
            time.sleep(config.EXP_SLEEP)
            comando = train.comando(prog)   
            ini = time.time()
            subprocess.call(comando)        
            fim = time.time()            
            print "    Plano " + train.opt.vetor() + ": " + str(fim - ini)
            copiarArquivos(train.opt.vetor(), prog)            
        processar(prog, train.opt)
        
def copiarArquivos(vetor, prog):
    copyfile(config.DADOS + "/tempos.txt", config.TEMPOS + "/tempos_" + prog + "_" + vetor + ".txt" )
    copyfile("compilationTime.txt",  config.TEMPOS + "/compilacao_" + prog + "_" + vetor + ".txt" )
    
    
def processar(prog, opt):
    t = tempos.processar(opt.ref(), prog)
    features.processar(t, prog, opt)
    
  
def processarArgumentos():
    parser = argparse.ArgumentParser(description = 'Script para o Treinamento', formatter_class=RawTextHelpFormatter)
    parser.add_argument("-b", default=0, type=int, help=benchmarks.help())
    parser.add_argument("-o", default=-1, type=int, help=trainingOptimizations.help())
    parser.add_argument("-p", required=False, help="Nome do programa")    
    return parser.parse_args()
    
if __name__ == '__main__':
    args = processarArgumentos()
    print "*** Training ***"
    config.criarDiretorios()
    bench = benchmarks.create(args.b)
    opt = trainingOptimizations.create(args.o)
    train = Treinamento(bench, opt)
    executarComando(train)