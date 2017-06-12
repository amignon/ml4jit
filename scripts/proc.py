# -*- coding: utf-8 -*-
"""
Created on Mon Apr 10 23:21:32 2017

@author: ale
"""
import config, tempos, features, time, trainingOptimizations

config.TEMPOS = "/home/ale/tmp/tempos"
config.TREINOS = "/home/ale/tmp/treinos"

if __name__ == '__main__':
    ini = time.time()    
    t = tempos.processar(64, 'fop')
    fim = time.time()
    print "Tempos: " + str(fim - ini)    
    ini = time.time()
    features.processar(t, 'fop', trainingOptimizations.TrainingOptimizationO0())    
    fim = time.time()
    print "Features: " + str(fim - ini)