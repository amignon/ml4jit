# -*- coding: utf-8 -*-
"""
Created on Wed Apr  5 12:58:13 2017

@author: ale
"""

import math, random
from numpy import binary_repr
import config

log = config.getLog()

def boolToStr (s):
    return "true" if s == "1" else "false"

class TrainingOptimization(object):
    def __init__(self):
        self.n = -1
        self.permutations = self.createPermutations()
        log.debug('Permutations: ' + str(self.permutations))
        
    def otimizacoes(self, comando, vetor):
        raise NotImplementedError() 
        
    def plano(self):
        raise NotImplementedError() 
        
    def tamanho (self):
        raise NotImplementedError()    

    def length(self):
        return len(self.permutations)
    
    def ref(self):
        return self.tamanho() - 1
    
    def createPermutations(self):
        p = config.PERMUTATIONS;
        t = self.tamanho() - 1
        if p < 0 or p >= self.tamanho():
            p = t;
        list = random.sample(xrange(t), p)
        list.append(t)
        list.sort()
        return list
        
    def comando(self, comando, vetor):
        comando.append("-X:aos:enable_recompilation=false")
        comando.append("-X:aos:initial_compiler=opt")        
        comando.append("-X:opt:mode=training")   
        # desabilita as otimizacoes de inline e reorder code
        comando.append("-X:opt:reorder_code=false")
        comando.append("-X:opt:reorder_code_ph=false")
        comando.append("-X:opt:inline=false")
        comando.append("-X:opt:inline_guarded=false")
        comando.append("-X:opt:inline_guarded_interfaces=false")
        comando.append("-X:opt:inline_preex=false")
        comando.append("-X:opt:h2l_inline_new=false")
        comando.append("-X:opt:h2l_inline_write_barrier=false")
        comando.append("-X:opt:h2l_inline_primitive_write_barrier=false")
        comando.append("-X:opt:osr_guarded_inlining=false")
        comando.append("-X:opt:osr_inline_policy=false")
        self.otimizacoes(comando, vetor)        
        
    def vetor(self):
        return binary_repr(self.permutations[self.n], self.qtdeOpt())

    def proximoVetor(self):     
        self.n = self.n + 1
        return self.vetor()
   
    def qtdeOpt(self):
        return int(math.log(self.tamanho(), 2))
        
    
class TrainingOptimizationTeste(TrainingOptimization):
    
    def tamanho (self):
        return 2;
        
    def plano(self):
        return "teste"
        
    def otimizacoes (self, comando, vetor):
        comando.append("-X:opt:field_analysis=" + boolToStr(vetor[0]));

class TrainingOptimizationO0(TrainingOptimization):
       
    def tamanho(self):
        return 64;
        
    def plano(self):
        return "O0"
       
    def otimizacoes(self, comando, vetor):
        comando.append("-X:irc:O0")
        comando.append("-X:opt:field_analysis=" + boolToStr(vetor[0]));
        comando.append("-X:opt:local_constant_prop=" + boolToStr(vetor[1]));
        comando.append("-X:opt:local_copy_prop=" + boolToStr(vetor[2]));
        comando.append("-X:opt:local_cse=" + boolToStr(vetor[3]));	
        comando.append("-X:opt:regalloc_coalesce_moves=" + boolToStr(vetor[4]));	        
        comando.append("-X:opt:regalloc_coalesce_spills=" + boolToStr(vetor[5]));        
        
class TrainingOptimizationO1(TrainingOptimization):
       
    def tamanho(self):
        return 512;
        
    def plano(self):
        return "O1"
        
    def otimizacoes(self, comando, vetor):
        comando.append("-X:irc:O1")
        comando.append("-X:opt:field_analysis=" + boolToStr(vetor[0]));
        comando.append("-X:opt:local_constant_prop=" + boolToStr(vetor[1]));
        comando.append("-X:opt:local_copy_prop=" + boolToStr(vetor[2]));
        comando.append("-X:opt:local_cse=" + boolToStr(vetor[3]));	
        comando.append("-X:opt:regalloc_coalesce_moves=" + boolToStr(vetor[4]));	        
        comando.append("-X:opt:regalloc_coalesce_spills=" + boolToStr(vetor[5]));
        comando.append("-X:opt:control_static_splitting=" + boolToStr(vetor[6]));
        comando.append("-X:opt:escape_scalar_replace_aggregates=" + boolToStr(vetor[7]));
        comando.append("-X:opt:escape_monitor_removal=" + boolToStr(vetor[8]));        
        
def create(op):
    if op == 0:
        return TrainingOptimizationO0()
    if op == 1:
        return TrainingOptimizationO1()
    else:
        return TrainingOptimizationTeste()
        
def help():
    s = "Identificador do Plano de Otimizacao"
    s += ("\n\t 0 - O0")
    s += ("\n\t 1 - O1")
    return s
        
if __name__ == '__main__':
    t = create(0);
    print t.length()
    print t.ref()
    print t.qtdeOpt()    
    print t.proximoVetor()
    print t.vetor()
    print t.proximoVetor()
    print t.vetor()
    print t.proximoVetor()
    print t.vetor()
    
    
    
    
class TrainingOptimization2(object):
    def __init__(self):
        self.n = -1
       
    def tamanho (self):
        raise NotImplementedError() 
    
    def otimizacoes(self, comando, vetor):
        raise NotImplementedError() 
        
    def plano(self):
        raise NotImplementedError() 
        
    def vetor(self):
        return binary_repr(self.n, self.qtdeOpt())
        
    def comando(self, comando, vetor):
        comando.append("-X:aos:enable_recompilation=false")
        comando.append("-X:aos:initial_compiler=opt")        
        comando.append("-X:opt:mode=training")   
        # desabilita as otimizacoes de inline e reorder code
        comando.append("-X:opt:reorder_code=false")
        comando.append("-X:opt:reorder_code_ph=false")
        comando.append("-X:opt:inline=false")
        comando.append("-X:opt:inline_guarded=false")
        comando.append("-X:opt:inline_guarded_interfaces=false")
        comando.append("-X:opt:inline_preex=false")
        comando.append("-X:opt:h2l_inline_new=false")
        comando.append("-X:opt:h2l_inline_write_barrier=false")
        comando.append("-X:opt:h2l_inline_primitive_write_barrier=false")
        comando.append("-X:opt:osr_guarded_inlining=false")
        comando.append("-X:opt:osr_inline_policy=false")
        self.otimizacoes(comando, vetor)        
        
    def proximoVetor(self):        
        if (self.n == -1 or self.n == (self.tamanho() - 1)):
            self.n = 0
        else:
            self.n = self.n + 1
        return binary_repr(self.n, self.qtdeOpt())
   
    def qtdeOpt(self):
        return int(math.log(self.tamanho(), 2))