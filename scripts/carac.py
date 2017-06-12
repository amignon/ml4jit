# -*- coding: utf-8 -*-
"""
Created on Fri Feb  3 12:33:07 2017

@author: ale
"""

from pandas import DataFrame, read_csv
import pandas as pd
import tempos, config
from numpy import binary_repr

def adicionarColunas(df):
    for i in xrange(config.ML_LEN):
        nome = "O" + str(i+1)
        df[nome] = None
        
def adicionarLinhas(df, tempos):
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
        
def processar(tempos, prog, tam):
    print "Processando Caracteristicas..."
    df = pd.read_csv("caracteristicas.csv")
    tam = len(binary_repr(tam-1))
    adicionarColunas(df)
    adicionarLinhas(df, tempos)
    df.to_csv(config.TREINOS + "/treino_" + config.ML_PLAN + "_" + prog + ".csv", index=False)        
        
if __name__ == "__main__":
    print pd.__version__
    processar(tempos.processar(16), 'Teste2', 16)
    '''
    tempos = {'1731009485': ['0000', '0101', '1011'], '2121695142': ['0110', '0000', '0001', '0010', '0100', '1100', '1010', '1011', '1001', '1000']}

    df = pd.read_csv(r'../dados/caracteristicas.csv')

    adicionarColunas(df)
    adicionarLinhas(df, tempos)

    # TODO: Excluir as linhas que não estão na medicao de tempo
    print df
    df.to_csv('treino.csv', index=False)

    df2 = pd.read_csv('treino.csv')
    print df2

    '''

