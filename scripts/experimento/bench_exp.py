# -*- coding: utf-8 -*-
"""
Created on Fri Feb 24 12:45:53 2017

Classes que representam os benchmarks para os experimentos com machine learning

Para adicionar um novo benchmark ou programa e necessario estender a 
classe BenchExp.

@author: ale
"""

import config
import glob

'''
class BenchTreinamento():
    def __init__(self, benchExp, lab):
        self.lab = lab
        self.benchExp = benchExp
        
    def comandoRVM(self, comando, prog):
        comando.append(config.RVM)
        comando.append("-Xms1024M")
        comando.append("-Xbootclasspath/p:" + config.JAVASSIST 
            + ":" + config.AGENTE + self.benchExp.bootClassPath(prog))        

    def comandoAgente(self, comando, prog):
        comando.append("-javaagent:" + config.AGENTE + "=main=" + self.benchExp.main(prog) 
            + ";treinamento;caminho=" + config.DADOS + "/;skip=" + config.SKIP)
        
    def comando(self, prog):
        comando = []
        self.comandoRVM(comando, prog)        
        self.benchExp.classPath(comando, prog)
        self.comandoAgente(comando, prog)
        self.lab.comando(comando, self.lab.proximoVetor())
        self.benchExp.comandoPrograma(comando, prog)
        return comando
        
    def benchmarks(self):
        return self.benchExp.benchmarks()
'''    

class BenchExp():
        
    def comandoRVM(self, comando):
        raise NotImplementedError()

    def comandoAgente(self, comando):
        raise NotImplementedError()
        
    def benchmarks(self):
        raise NotImplementedError()
        
    def classPath(self, comando, prog):
        raise NotImplementedError()
        
    def comandoPrograma(self, comando, prog):
        comando.append(prog)
        
    def classPathFeatures(self, comando, prog):
        self.classPath(comando, prog)

    def classPathAgente(self, prog):
        return ""
        
    def bootClassPath(self, prog):
        return ""
        
    def main(self, prog):
        return prog + ".main"


class BenchExpML():
    def __init__(self, benchExp, lab):
        self.lab = lab
        self.benchExp = benchExp
        
    def comandoRVM(self, comando):
        raise NotImplementedError()        

    def comandoAgente(self, comando):
        raise NotImplementedError()
        
    def comando(self, prog, ml):
        comando = []
        self.comandoRVM(comando)        
        self.benchExp.classPath(comando, prog)
        self.comandoAgente(comando)
        self.lab.comandoExp(comando, ml)
        self.benchExp.comandoPrograma(comando, prog)
        return comando
        
    def benchmarks(self):
        return self.benchExp.benchmarks()

class BenchExpMLAgente(BenchExpML):
        
    def comandoRVM(self, comando):
        comando.append(config.RVM)
        comando.append("-Xbootclasspath/p:" + config.JAVASSIST 
            + ":" + config.AGENTE + self.bootClassPath())        

    def comandoAgente(self, comando):
        comando.append("-javaagent:" + config.AGENTE + "=ml:caminho=" + config.DADOS)
        
class BenchExpMLRVM(BenchExpML):
        
    def comandoRVM(self, comando):
        comando.append(config.RVM)
        comando.append("-Xbootclasspath/p:" + self.benchExp.bootClassPath())

    def comandoAgente(self, comando):
        pass
        

class BenchExpOne(BenchExp):
        
    def benchmarks(self):
        return ['Teste2']
        
    def classPath(self, comando, b):
        comando.append("-cp")
        comando.append("teste.jar")

        
class BenchExpJGFS1(BenchExp):
        
    def benchmarks(self):
        return ['JGFMathBench']
        
    def classPath(self, comando, b):
        comando.append("-cp")
        comando.append(config.JGF + ":" + config.JGFS1)
        
class BenchExpJGFS2(BenchExp):
        
    def benchmarks(self):
        return ['JGFHeapSortBenchSizeA']
        
    def classPath(self, comando, b):
        comando.append("-cp")
        comando.append(config.JGF + ":" + config.JGFS2)
        
        
class BenchExpDaCapo(BenchExp):
        
    def benchmarks(self):
        #problemas: batik, eclipse, h2?, jython, pmd?
        #return ['avrora', 'fop', 'luindex', 'lusearch']
        return ['avrora']
        
    def comandoPrograma(self, comando, prog):
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
        
    def classPathAgente(self, prog):
        return config.DIR_BASE + "/scratch/jar/*"
        
    def classPathFeatures(self, comando, prog):
        comando.append("-cp")        
        cp = config.jarsDaCapo(prog)
        comando.append(cp + ":" + config.DACAPO_JAR)
    
    def main(self, prog):
        return "Harness.main"

class BenchExpSPEC(BenchExp):
        
    def benchmarks(self):
        #problemas: serial, derby, crypto, sunflow, xml
        #return ['mpegaudio', 'compress', 'compiler', 'scimark']
        return ['mpegaudio']
        
    def comandoPrograma(self, comando, prog):
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
        comando.append("-it")
        comando.append(config.SPEC_IT)
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
        
    def classPathFeatures(self, comando, prog):
        comando.append("-cp")
        cp = config.SPEC_JAR
        #cp += self.montarCP()
        comando.append(cp)
        
    def montarCP(self):
        lista = ""
        for file in glob.glob(config.SPEC + "/lib/*.jar"):    
            lista += ":" + file
        return lista
        
    def bootClassPath(self, prog):
        return self.montarCP()
        
    def main(self, prog):
        return "spec.harness.Launch.main"

'''
def montarComando(self, bench, prog, ml):
        comando = []
        self.comandoRVM(comando)        
        self.classPath(comando, b)
        self.comandoAgente(comando)
        self.lab.comandoExp(comando, ml)
        self.comandoPrograma(comando, b)
        return comando
'''            
if __name__ == '__main__':
    pass
