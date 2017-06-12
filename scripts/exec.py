# -*- coding: utf-8 -*-
"""
Created on Wed Apr  5 15:46:55 2017

@author: ale
"""


import time, sys, argparse
import subprocess

import config
   
    return parser.parse_args()
    
if __name__ == '__main__':
    args = processarArgumentos()
    print "*** Test ***"
    config.criarDiretorios()
    bench = benchmarks.create(args.b)
    opt = trainingOptimizations.create(args.o)
    train = Treinamento(bench, opt)
    executarComando(train, args.p)
