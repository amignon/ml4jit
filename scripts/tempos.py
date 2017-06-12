# -*- coding: utf-8 -*-
"""
Created on Thu Feb  2 13:21:09 2017

@author: ale
"""

import glob
import config
import numpy as np
from numpy import binary_repr

log = config.getLog()

class TempoMetodo(object):
    def __init__(self, cod):
        self.cod = cod;
        self.dados = []
        self.compilacao = 0;
        
    def add(self, t):
        self.dados.append(t);
                
    def addTempoCompilacao(self, t):
        self.compilacao = t;
        
    def tempoTotal(self):
        total = 0
        if config.TEMPOS_MEDIANA:
            total += self.mediana()
        else:
            total += self.media()
        if config.TEMPOS_CT:
            total += self.compilacao        
        return total
        
    def media(self):
        return np.mean(self.dados)
        
    def mediana(self):
        return np.median(self.dados)
        
    def ignorar(self):
        return len(self.dados) < config.TEMPOS_SIZE      
        
    def imprimir (self):
        print "\t" + self.cod + " " + str(self.media()) + " " + str(self.compilacao) + " " + str(self.tempoTotal());

class Tempos(object):
    def __init__(self, nome):
        self.nome = nome;
        self.metodos = {};
    
    def add(self, cod, t):
        m = self.metodos.get(cod);
        if m == None:
            m = TempoMetodo(cod)
            self.metodos[cod] = m
        m.add(t)
        
    def addTempoCompilacao(self, cod, t):
        m = self.metodos.get(cod);
        if not m == None:
            m.addTempoCompilacao(t)       
        
    def imprimir (self):
        print self.nome
        print len(self.metodos)
        for v in self.metodos.itervalues():
            v.imprimir()
            
def adicionarVirgulas(s):
    return ','.join(str(x) for x in s)
    
def obterPadrao(file):
    return file.split("_")[3].split(".")[0]

def obterTempos(tempos, prog, level):    
    pattern = config.TEMPOS + "/tempos_" + level + "_" + prog + "_*.txt"
    for file in glob.glob(pattern):    
        f = open(file,'r')        
        t = Tempos(obterPadrao(file))
        for line in f:
            cod = line.split("|")[0]
            dif = long (line.split("|")[1])
            t.add(cod, dif)    
        tempos[t.nome] = t
        f.close()
        
def obterCompilacao(tempos, prog, level):
    pattern = config.TEMPOS + "/compilacao_" + level + "_" + prog + "_*.txt"
    for file in glob.glob(pattern):    
        f = open(file,'r')        
        t = tempos[obterPadrao(file)]
        for line in f:
            cod = line.split("|")[0]
            dif = long (line.split("|")[1])
            t.addTempoCompilacao(cod, dif)  
        f.close()
        
def obterMelhores(tempos, nref):
    final = {}
    s = binary_repr(nref)
    ref = tempos[s]
    for k in ref.metodos.iterkeys():
        lista = []
        for t in tempos.itervalues():            
            if t.metodos.has_key(k): 
                if not t.metodos[k].ignorar():
                    dif = 1 - (float(t.metodos[k].tempoTotal()) / ref.metodos[k].tempoTotal()); 
                    log.debug('%-15s %-10s %s', k, t.nome, str(dif))
                    if (dif > config.TEMPOS_DIF):                
                        lista.append(t.nome)
        if len(lista) > 0:
            final[k] = lista
    return final
    
def processar(tam, prog, level):
    print "Processando Tempos..."
    tempos={}
    obterTempos(tempos, prog, level)
    obterCompilacao(tempos, prog, level)
    return obterMelhores(tempos, tam)
    
if __name__ == "__main__":  
    d = processar(2)
    print d
    print len(d)

'''
final = {}
ref =  tempos['tempos_1111.txt']
for k in ref.metodos.iterkeys():
    print ref.nome + " " + k
    lista = []
    for t in tempos.itervalues():
        dif = 1 - float( t.metodos[k].media()) / ref.metodos[k].media();        
        if (dif > 0.01):
            print "\t" + t.nome + " " +str(t.metodos[k].media()) + " " + str(ref.metodos[k].media()) + "  " + str(dif) 
            lista.append(t.nome)
    final[k] = lista

print final



dict = {};
tempo = {};

caminho = ''
arquivo = caminho + 'tempos.txt'
file = open(arquivo, 'r')
i = 0;
for line in file:
    i = i + 1;
    if (i % 10000 == 0):
        print ('Processando... ', i);
    id = line.split("|")[0];
    dif = long (line.split("|")[3]);
    if dict.has_key(id):
        dict[id] = dict[id] + 1
        tempo[id] = tempo[id] + dif
    else:
        dict.update({id:1})
        tempo.update({id:dif})
        

for k in dict.iterkeys():
    print k, ' \t', dict[k]

 
for key, value in sorted(dict.iteritems(), key=lambda (k,v): (v,k)):
    print "%12s \t %7s \t %15s" % (key, value, tempo[key] / value) 
    
'''