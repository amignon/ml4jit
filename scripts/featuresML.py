# -*- coding: utf-8 -*-
"""
Created on Fri Mar 10 11:42:43 2017

@author: ale
"""
import time
import features, config, benchmarks
from ml import ML

log = config.getLog()

def execute(benchmark, prog):    
    log.info("Extracting features...")
    features.extrair(benchmark, prog)
    log.info("Processing...")
    ini = time.time()
    programs = _oneOut(benchmark, prog)
    _process(programs)
    fim = time.time()
    log.debug("\t Process Time: " + str (fim - ini))
    
def _process(programs):
    ml = ML(programs)
    f = open("features.csv", "r")
    out = open("featuresml.txt", "w")
    next(f)
    for linha in f:
        data = linha.strip("\n")        
        id = linha.split(",")[0]
        res = ml.prediction(data.split(","))
        out.write(str(id) + "," + str(res) + "\n")        
    out.close()
    f.close()
    
def _oneOut(benchmark, prog):
    return [p for p in benchmark.programs() if p not in prog]

if __name__ == '__main__':
    b = benchmarks.create(3)    
    execute(b, 'JGFSeriesBenchSizeB')