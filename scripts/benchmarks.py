# -*- coding: utf-8 -*-
"""
Created on Wed Apr  5 12:40:29 2017

Classes que representam os benchmarks para os experimentos com machine learning

Para adicionar um novo benchmark ou programa e necessario estender a 
classe Benchmark.

@author: ale
"""

import glob
import config


class Benchmark():        
        
    def programs(self):
        raise NotImplementedError()
        
    def classPath(self, comando, prog):
        raise NotImplementedError()
        
    def programCommand(self, comando, prog):
        comando.append(prog)
        
    def classPathFeatures(self, comando, prog):
        self.classPath(comando, prog)

    def classPathAgent(self, prog):
        return ""
        
    def bootClassPath(self, prog):
        return ""        

class Teste(Benchmark):
        
    def programs(self):
        return ['Teste2']
        
    def classPath(self, comando, b):
        comando.append("-cp")
        comando.append("teste.jar")
        
        
class DaCapo(Benchmark):
        
    def programs(self):
        #problemas: batik, eclipse, jython, tomcat, tradebeans, tradesoap, xalan?
        lista = []
        lista.append('avrora')
        lista.append('fop')
        lista.append('luindex')
        lista.append('lusearch')
        lista.append('h2')
        lista.append('pmd')
        lista.append('sunflow')
        return lista
        
    def programCommand(self, comando, prog):
        comando.append("-jar")
        comando.append(config.DACAPO_JAR)
        comando.append(prog)
        comando.append('-s')        
        comando.append('small')
        
    def classPath(self, comando, prog):
        pass        
        #comando.append("-cp")
        #comando.append(config.DACAPO_JAR)
        #cp = config.jarsDaCapo(prog)
        #comando.append(cp + ":" + config.DACAPO_JAR)
        
    def classPathAgent(self, prog):
        return config.DIR_BASE + "/scratch/jar/*"
        
    def classPathFeatures(self, comando, prog):
        comando.append("-cp")        
        cp = config.jarsDaCapo(prog)
        comando.append(cp + ":" + config.DACAPO_JAR)


class SpecJVM2008(Benchmark):
        
    def programs(self):
        #problemas: serial, derby, crypto, sunflow, xml
        lista = []
        lista.append('mpegaudio')
        lista.append('compress')
        lista.append('compiler')
        lista.append('scimark')
        return lista
        
    def programCommand(self, comando, prog):
        comando.append("-jar")
        comando.append(config.SPEC_JAR)        
        comando.append("-Dspecjvm.home.dir=" + config.SPEC)        
        comando.append("-chf")
        comando.append("false")
        comando.append("-ctf")
        comando.append("false")
        comando.append("-crf")
        comando.append("false")
        comando.append("-ikv")
        comando.append("-wt")
        comando.append(config.SPEC_WT)
        #comando.append("-it")
        #comando.append(config.SPEC_IT)
        comando.append("-i")
        comando.append(config.SPEC_I)
        comando.append("-bt")
        comando.append(config.SPEC_BT)
        comando.append("-ops")
        comando.append(config.SPEC_OPS)
        comando.append(prog)

    def classPath(self, comando, prog):
        comando.append("-cp")
        comando.append(config.SPEC_JAR)
        
    '''  
    def classPathFeatures(self, comando, prog):
        
        comando.append("-cp")
        cp = config.SPEC_JAR
        #cp += self.montarCP()
        comando.append(cp)
    '''
    
    def montarCP(self):
        lista = ""
        for file in glob.glob(config.SPEC + "/lib/*.jar"):    
            lista += ":" + file
        return lista
        
    def bootClassPath(self, prog):
        return self.montarCP()

        
class JGF_S2(Benchmark):
        
    def programs(self):
        # problemas:
        lista = []
	lista.append('JGFSparseMatmultBenchSizeB')        
	lista.append('JGFCryptBenchSizeB')
        lista.append('JGFFFTBenchSizeB')
        lista.append('JGFHeapSortBenchSizeB')
        lista.append('JGFLUFactBenchSizeB')
        lista.append('JGFSeriesBenchSizeB')
        lista.append('JGFSORBenchSizeB')
        
        return lista
        
    def classPath(self, comando, b):
        comando.append("-cp")
        comando.append(config.JGF + ":" + config.JGFS2)      
        
class JGF_S3(Benchmark):
        
    def programs(self):
        # problemas: JGFSearchBenchSizeB
        lista = []
        lista.append('JGFEulerBenchSizeB')
        lista.append('JGFMolDynBenchSizeB')
        lista.append('JGFMonteCarloBenchSizeB')
        lista.append('JGFRayTracerBenchSizeB')
        return lista
        
    def classPath(self, comando, b):
        comando.append("-cp")
        comando.append(config.JGF + ":" + config.JGFS3)  
        
class JGF_T(Benchmark):
        
    def programs(self):
        # problemas: JGFSearchBenchSizeA
        lista = []
        lista.append('JGFLUFactBenchSizeA')
        return lista
        
    def classPath(self, comando, b):
        comando.append("-cp")
        comando.append(config.JGF_T + ":" + config.JGF_T + "/section2") 
        
    def programCommand(self, comando, prog):
        comando.append(prog)
        comando.append('3')
        
def create(op):
    if   op == 1:
        return DaCapo()
    elif op == 2:
        return SpecJVM2008()
    elif op == 3:
        return JGF_S2()
    elif op == 4:
        return JGF_S3()
    elif op == 5:
        return JGF_T()
    else:
        return Teste()
        
def help():
    s = "Identificador do benchmark"
    s += ("\n\t 1 - DaCapo")
    s += ("\n\t 2 - SpecJVM2008")
    s += ("\n\t 3 - JGF S2")
    s += ("\n\t 4 - JGF S3")
    return s
        
if __name__ == '__main__':
    pass
