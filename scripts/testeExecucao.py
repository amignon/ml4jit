# -*- coding: utf-8 -*-
"""
Created on Tue Mar  7 12:41:32 2017

Utilizado para a realizacao de testes de execucao de um novo benchmark

@author: ale
"""

import time
import subprocess
import socket
import sys

import config
import servidor
import features
from experimento import bench_exp
from experimento import lab_exp

def executarComando(bench):
    for b in bench.benchmarks():
        print ("Extraindo Caracteristicas")
        features.executar(bench, b)
        print "Executando: " + b 
        config.remover()
        comando = bench.comando(b, True)
        print comando
        ini = time.time()
        subprocess.call(comando)        
        fim = time.time()
        dif = fim - ini
        print "\t Tempo: " + str (dif)     

def inicializarServidor():
    servidor.iniciar()   

def finalizarServidor():   
   HOST = '127.0.0.1'     # Endereco IP do Servidor
   PORT = 10002            # Porta que o Servidor esta
   tcp = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
   dest = (HOST, PORT)
   tcp.connect(dest)
   tcp.send("EXIT");
   tcp.close()
    
if __name__ == '__main__':
    print ("Teste de Execucao")  
    
   
    try:
        bench =  bench_exp.BenchExpOne(lab_exp.ExperimentoLab01())
        #bench =  BenchExpJGFS1(ExperimentoLab01())
        #bench = bench_exp.BenchExpSPEC(lab_exp.ExperimentoLab01()) 

    
        inicializarServidor()
        config.criarDiretorios()

        executarComando(bench)
    except:
        raise
    finally:        
        pass
        finalizarServidor()           


