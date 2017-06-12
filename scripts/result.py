# -*- coding: utf-8 -*-
"""
Created on Wed Apr 12 14:33:34 2017

@author: ale
"""

import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import config, glob
import scipy.stats.mstats as m

class Program:
    def __init__(self, nome, time, refTime, comp, compRef):
        self.nome = nome
        self.time = time
        self.refTime = refTime
        self.comp = comp
        self.compRef = compRef
    
    def imprimir(self):
        print '{0:12} Time: {1:10f} Ref: {2:10f} DIF: {3:10f}'.format(
            self.nome, self.medianTime(), self.medianTimeRef(), self.difTime())
            
    def medianTime(self):
        return np.median(self.time)
        
    def medianTimeRef(self):
        return np.median(self.refTime)
        
    def difTime(self):
        #return 1 + ((self.medianTime()-self.medianTimeRef())/self.medianTime())   
        return self.medianTime() / self.medianTimeRef()
        
    def medianComp(self):
        return np.median(self.comp)
        
    def medianCompRef(self):
        return np.median(self.compRef)

    def difComp(self):
        #return 1 + ((self.medianComp()-self.medianCompRef())/self.medianComp())   
        return self.medianComp() / self.medianCompRef()

class Bench:
    def __init__ (self, nome, tf, tnf):        
        self.nome = nome[1:len(nome)-1]
        self.tf = tf
        self.tnf = tnf
        
    def imprimir (self):
        print '{0:12} TF: {1:10f} TNF: {2:10f} DIF: {3:10f}'.format(
            self.nome, self.medianaTF(), self.medianaTNF(), self.dif())

    def medianaTF (self):
        return np.median(self.tf);
        #return np.average(self.tf);
    
    def medianaTNF (self):
        return np.median(self.tnf);
        #return np.average(self.tnf);
        
    def dif (self):        
        return ((self.medianaTF()-self.medianaTNF())/self.medianaTF())*100     

def grafico (benchs):
    N = len(benchs);
    ind = np.arange(N)  # the x locations for the groups
    width = 0.60
    fig, ax = plt.subplots();
    dif = []
    nomes = []
    for b in benchs:   
        nomes.append(b.nome)
        dif.append(b.difTime())
        
  
    ax.axhline(color='black')
    ax.bar(ind, dif, width, color='blue', align='center')    
    ax.set_ylabel('%')
    ax.set
    ax.set_title('Programs')
    ax.set_xticks(ind)
    ax.set_xticklabels(nomes, rotation=300)
    ax.set_xlim([-1,N])
    
    plt.show()
    
    
def graph(dados):
    '''
    raw_data = {'name': ['Teste 1', 'Teste 2'],
        'execution': [0.35, 0.60],
        'compilation': [0.20, 0.70]}

    dados = {}
    dados['name'] = ['Teste 1', 'Teste 2']
    dados['execution'] = [0.35, 0.60]
    dados['compilation'] = [0.20, 0.70]
    print dados
    '''
    df = pd.DataFrame(dados, columns = ['name', 'execution', 'compilation'])
    print df
    # Setting the positions and width for the bars
    pos = list(range(len(df['execution'])))
    width = 0.25

    # Plotting the bars
    fig, ax = plt.subplots(figsize=(10,5))

    # Create a bar with pre_score data,
    # in position pos,
    plt.bar(pos,
        #using df['pre_score'] data,
        df['execution'],
        # of width
        width,
        # with alpha 0.5
        alpha=0.7,
        # with color
        color='black',#'#EE3224',
        # with label the first value in first_name
        label=df['name'][0])

    # Create a bar with mid_score data,
    # in position pos + some width buffer,
    plt.bar([p + width for p in pos],
        #using df['mid_score'] data,
        df['compilation'],
        # of width
        width,
        # with alpha 0.5
        alpha=0.7,
        # with color
        color='gray',#F78F1E',
        # with label the second value in first_name
        label=df['name'][1])

    # Set the y axis label
    #ax.set_ylabel('Score')

    # Set the chart's title
    #ax.set_title('Test Subject Scores')

    # Set the position of the x ticks
    ax.set_xticks([p + 1.0 * width for p in pos])

    # Set the labels for the x ticks
    ax.set_xticklabels(df['name'], rotation=300, fontsize=8)
    
    
    

    # Setting the x-axis and y-axis limits
    plt.xlim(min(pos)-width, max(pos)+width*4)
    plt.ylim([0, max(np.maximum(df['execution'], df['compilation'])) + 0.1] )
    
    #plt.ylim([0, 1.4] )
# Adding the legend and showing the plot
    plt.legend(['Execution', 'Compilation'], loc='upper center',  bbox_to_anchor=(0.5, 1.13),
          ncol=2, fancybox=True, shadow=True, prop={'size':12})
    
    
    #plt.grid()
    plt.axhline(y=1.0, xmin=0, xmax=1, color='black')
    plt.show()  
    
def processFile(f, name):    
    line = next(f)
    time = map(long, line.strip('[]\n').split(','))
    line = next(f)
    refTime = map(long, line.strip('[]\n').split(','))
    line = next(f)
    comp = map(long, line.strip('[]\n').split(','))
    line = next(f)
    refComp = map(long, line.strip('[]\n').split(','))
    p = Program(name, np.array(time), np.array(refTime), np.array(comp), np.array(refComp))
    return p
    
    
def processPrograms(programs):    
    dados = {}
    dados['name'] = []
    dados['execution'] = []
    dados['compilation'] = []    
    for p in programs:        
        dados['name'].append(p.nome)
        dados['execution'].append(p.difTime())
        dados['compilation'].append(p.difComp())
    dados['name'].append('geo')
    dados['execution'].append(m.gmean(dados['execution']))
    dados['compilation'].append(m.gmean(dados['compilation']))    
    return dados        
    

def mapping(name):
    m = {}
    m ['JGFSparseMatmultBenchSizeB'] = 'Sparse' 
    m ['JGFCryptBenchSizeB'] = 'Crypt'
    m ['JGFLUFactBenchSizeB'] = 'LUFact'
    m ['JGFHeapSortBenchSizeB'] = 'HeapSort'
    m ['JGFFFTBenchSizeB'] = 'FFT'
    m ['JGFSORBenchSizeB'] = 'SOR'
    m ['JGFSeriesBenchSizeB'] = 'Series'
    m ['JGFEulerBenchSizeA'] = 'Euler'
    m ['JGFMonteCarloBenchSizeA'] = 'MC'
    m ['JGFMolDynBenchSizeA'] = 'MD'
    m ['JGFRayTracerBenchSizeA'] = 'Ray Tracer'
    ret = m.get(name);
    if ret == None:
        return name
    return ret;


if __name__ == '__main__':   
    #config.RESULTS = '/home/ale/doutorado/resultados/resultsdacapo'
    programs = []    
    for file in glob.glob(config.RESULTS + "/*.txt"):    
        name = file.split("_")[1].split(".")[0]
        name = mapping(name)
        f = open(file,'r')        
        programs.append(processFile(f, name))
        f.close()    
    dados = processPrograms(programs)
    graph(dados)
    
    '''   
    f = open(config.RESULTS + "/resultado.txt", "r") 
    programs.append(processFile(f))
    f.close()
    
    f = open(config.RESULTS + "/resultado.txt", "r") 
    programs.append(processFile(f))
    f.close()
    
    graph()

    dados = [np.array(ml), np.array(nml)]
    
    #plt.boxplot(dados)
    
    y = [np.mean(np.array(ml)), np.mean(np.array(nml))]
    N = len(y)
x = range(2)
width = 1/1.9
plt.bar(x, y, color="blue")
'''

