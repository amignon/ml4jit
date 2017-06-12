# -*- coding: utf-8 -*-
"""
Created on Wed Apr  5 15:46:55 2017

@author: ale
"""


import time, sys, argparse
import subprocess
from numpy import binary_repr
import numpy as np

from argparse import RawTextHelpFormatter


import config, benchmarks, trainingOptimizations, features
from treinamento import Treinamento

    
def executarComando(train, prog):
    print "Executando: " + prog
    config.removerTempos()
    # Extrai as caracteristicas dos metodos do programa
    features.extrair(train.benchmark, prog)
    config.remover()
    comando = train.comando(prog)  
    print comando
    ini = time.time()
    subprocess.call(comando)        
    fim = time.time()            
    print "    Plano " + train.opt.vetor() + ": " + str(fim - ini)
  
def processarArgumentos():
    parser = argparse.ArgumentParser(description = 'Script para o Treinamento', formatter_class=RawTextHelpFormatter)
    parser.add_argument("-b", default=0, type=int, help=benchmarks.help())
    parser.add_argument("-o", default=-1, type=int, help="Identificador do Plano de Otimizacao")
    parser.add_argument("-p", required=True, help="Nome do programa")    
    return parser.parse_args()
    
if __name__ == '__main__':
    args = processarArgumentos()
    print "*** Test ***"
    config.criarDiretorios()
    bench = benchmarks.create(args.b)
    opt = trainingOptimizations.create(args.o)
    train = Treinamento(bench, opt)
    executarComando(train, args.p)