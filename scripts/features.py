# -*- coding: utf-8 -*-
"""
Created on Mon Feb 20 11:22:13 2017

@author: ale
"""


import os
import time
import subprocess
from numpy import binary_repr
import numpy as np

from pandas import DataFrame, read_csv
import pandas as pd
import tempos
import config
import benchmarks

log = config.getLog()
    
def montarComando(bench, prog):
    comando = []
    comando.append(config.RVM)
    comando.append("-Xms1024M")
    comando.append("-Xbootclasspath/p:" + config.JAVASSIST + ":" + config.AGENTE)
    comando.append("-X:aos:enable_recompilation=false")
    comando.append("-X:aos:initial_compiler=opt")
    comando.append("-X:irc:O2")
    bench.classPathFeatures(comando, prog)
    comando.append("ml4jit.FeaturesExtractor")
    return comando
    
def extrair(bench, prog):
    log.info("Extracting features: " + prog)
    config.remover()    
    c = montarComando(bench, prog)
    ini = time.time()
    subprocess.call(c)        
    fim = time.time()
    log.debug("Time: " + str (fim - ini))
    
    
def adicionarColunas(df):
    for i in xrange(config.ML_LEN):
        nome = "O" + str(i+1)
        df[nome] = None
        
def adicionarLinhas1(df, tempos):
    excluir = []    
    tam = len(df)    
    for e in xrange(tam):
        excluir.append(e)
    
    for k in tempos.iterkeys():        

        for i in xrange(tam):
            if (str(k) == str(df.loc[i]["ID"])):
                excluir.remove(i)
                alterarLinha(df, i, tempos[k][0])
                planos = tempos[k][1:]
                for p in planos:
                    n = copiarLinha(df, i)
                    alterarLinha(df, n, p)
    df.drop(df.index[excluir], inplace=True)
    
def adicionarLinhas(df, tempos):
    excluir = []    
    tam = len(df)
    log.debug("TAM: " + str(tam))
    for e in xrange(tam):
        excluir.append(e)
    log.debug("Criou a lista de exclusao...")
    log.debug("Processando...")
    cont = 0;
    for i in xrange(tam):
        cont += 1
        if cont % 100 == 0:
            log.debug(str(cont))
        for k in tempos.iterkeys():                
            if (str(k) == str(df.loc[i]["ID"])):
                excluir.remove(i)
                alterarLinha(df, i, tempos[k][0])
                planos = tempos[k][1:]
                for p in planos:
                    n = copiarLinha(df, i)
                    alterarLinha(df, n, p)
    df.drop(df.index[excluir], inplace=True)

def copiarLinha(df, i):
    n = len(df)
    df.loc[n] = df.loc[i]
    return n
    
def alterarLinha(df, i, plano):
    x = 1
    for s in plano:
        st = "O" + str(x)
        x = x + 1
        df.set_value(i, st, s)
    for j in xrange(config.ML_LEN - (x-1)):
        st = "O" + str(j + x)
        df.set_value(i, st, "0")
        
def processar(tempos, prog, level):
    print "Processando Caracteristicas..."
    df = pd.read_csv("features.csv")
    #tam = len(binary_repr(lab.tamanho()-1))
    adicionarColunas(df)
    adicionarLinhas(df, tempos)
    df.to_csv(config.TREINOS + "/treino_" + level + "_" + prog + ".csv", index=False)
    
    
if __name__ == '__main__':
    print ("Extraindo caracteristicas...")
    #executar(BenchExpOne(None))
    extrair(benchmarks.create(1), 'fop')