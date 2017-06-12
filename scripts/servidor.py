# -*- coding: utf-8 -*-
"""
Created on Fri Feb 10 13:00:50 2017

@author: ale
"""

import random
import socket
import sys
import time
import threading
from ml import ML

import logging as log
log.basicConfig(filename='servidor.log', format='%(levelname)s: %(message)s',level=log.DEBUG)

class socketThread (threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)
    def run(self):
        log.info("Inicializando servidor...")
        server()
        log.info("Finalizando servidor...")

def server():
    ml = ML()
    # Create a TCP/IP socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

    # Bind the socket to the port
    server_address = ('localhost', 10002)
    log.debug('Inicializando %s na porta %s', server_address[0], server_address[1])
    sock.bind(server_address)

    # Listen for incoming connections
    sock.listen(1)

    while True:
        # Wait for a connection
        log.debug('Esperando por uma conexao...')
        connection, client_address = sock.accept()
        try:
            
            log.debug('Conexao de %s', client_address)

            # Recebendo dados
            data = connection.recv(2048)
            data = data.strip("\n")
            log.debug('\tRecebido %s', data)
            if data == "EXIT":
                break;                    
                
            #print '\tRecebido %s' % data
            res = ml.prediction(data.split(","))

            # Enviando dados            
            connection.sendall(str(res) + "\n")     
            log.debug("\tEnviado: %s", res)
        finally:            
            connection.close()

    sock.close()
    
def iniciar():
    t1 = socketThread()
    t1.start()

            
    
if __name__ == '__main__':
    log.basicConfig(filename='servidor.log', format='%(levelname)s: %(message)s',level=log.DEBUG)
    iniciar()

