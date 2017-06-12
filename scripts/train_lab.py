# -*- coding: utf-8 -*-
"""
Created on Fri Mar 17 14:51:11 2017

@author: ale
"""

from numpy import binary_repr
import math

def boolToStr (s):
    return "true" if s == "1" else "false"
    
class TreinamentoLab(object):
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
        comando.append("-X:irc:O0")
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
    
class TreinamentoLabOX(TreinamentoLab):
    
    def tamanho (self):
        return 2;
        
    def plano(self):
        return "OX"
        
    def otimizacoes (self, comando, vetor):
        comando.append("-X:opt:field_analysis=" + boolToStr(vetor[0]));

class TreinamentoLabO0(TreinamentoLab):
       
    def tamanho(self):
        return 16;
        
    def plano(self):
        return "O0"
        
    def otimizacoes(self, comando, vetor):
        comando.append("-X:opt:field_analysis=" + boolToStr(vetor[0]));
        comando.append("-X:opt:local_constant_prop=" + boolToStr(vetor[1]));
        comando.append("-X:opt:local_copy_prop=" + boolToStr(vetor[2]));
        comando.append("-X:opt:local_cse=" + boolToStr(vetor[3]));	
