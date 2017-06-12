# -*- coding: utf-8 -*-
"""
Created on Wed Feb  8 14:15:54 2017

Modulo contendo os dados de configuracao para a execucao do ambiente.

@author: ale
"""

import os, subprocess, glob, logging

#===== Configuracoes dos diretorios =====#
# Diretorio basa da execucao do ambiente
DIR_BASE    = "/home/ale/doutorado"
# Diretorio para gravar os arquivos gerados
DADOS       = DIR_BASE + "/dados"
# Diretorio para gravar os tempos de execucao
TEMPOS      = DIR_BASE + "/dados/tempos"
# Diretorio para ler/gravar os arquivos de treinamento
TREINOS     = DIR_BASE + "/treinos"
# Diretorio com os resultados dos experimento
RESULTS     = DIR_BASE + "/results"
# Caminho completo da RVM
RVM         = "/home/ale/rvm/jikesrvm/dist/production_x86_64-linux/rvm" 
# Caminho do jar do javassist
JAVASSIST   = "/home/ale/doutorado/agente/lib/javassist.jar"
# Caminho do jar do agente
AGENTE      = "/home/ale/doutorado/agente/ml4jit.jar"
# Caminho do arquivo skip
SKIP        = "/home/ale/doutorado/agente/skip.txt"
#==========#


#===== Configuracoes de Treinamento (Agent) =====#
# Quantidade máxima de registros de tempo a serem coletados
SAMPLE_SIZE         = 100
# Habilita o modo de amostragem
SAMPLING            = "false"
# Tempo em modo com sampling
WITH_SAMPLING       = 100
# Tempo em modo sem sampling
WITHOUT_SAMPLING    = 100
#==========#

#===== Configuracoes de Treinamento (Script) =====#
# Permutações para um plano de otimização. -1 significa todas as permutações
PERMUTATIONS        = 99
#==========#

#===== Configuracoes Tempos de Treinamento =====#
# Considerar uma diferenca maior a esse numero
TEMPOS_DIF   = 0.01
# Utiliza a mediana dos tempos dos metodos, por padrao utiliza a media
TEMPOS_MEDIANA = True
# Utiliza o tempo de compilacao
TEMPOS_CT    = False
# Pelo menos quantos dados necessarios para considerar o metodo
TEMPOS_SIZE = 0
#==========#

#===== Configuracoes dos experimentos =====#
# Tempo em ms de execucao entre os experimentos
EXP_SLEEP   = 30
# Quantas vezes cada programa ira rodar
EXP_SIZE    = 20
#==========#

#===== Configuracoes ML =====#
# Quantidade de otimizacoes
ML_LEN   = 10
ML_PLAN  = "O0"
# Classificador
ML_CLF   = 1
# Lista contando as colunas que devem ser excluidas do treinamento/predição
ML_EXCLUDE = []
#==========#


#===== Configuracoes JGF =====#
JGF         = DIR_BASE + "/benchmarks/JGF/v2.0"
JGFS1       = JGF + "/section1"
JGFS2       = JGF + "/section2"
JGFS3       = JGF + "/section3"
#==========#

#===== Configuracoes JGF =====#
JGF_T       = DIR_BASE + "/benchmarks/threadv1.0"
#==========#


#===== Configuracoes DaCapo =====#
DACAPO      = DIR_BASE + "/benchmarks/Dacapo"
DACAPO_JAR  = DACAPO + "/dacapo-9.12-bach.jar"
DACAPO_JARS = {}
DACAPO_JARS["avrora"]       = ["avrora-cvs-20091224.jar"]
DACAPO_JARS["batik"]        = ["batik-all.jar", "xml-apis-ext.jar","xml-apis.jar", "crimson-1.1.3.jar", "xerces_2_5_0.jar", "xalan-2.6.0.jar"]
DACAPO_JARS["eclipse"]      = ["eclipse.jar"]
DACAPO_JARS["fop"]          = ["fop.jar", "avalon-framework-4.2.0.jar", "batik-all-1.7.jar", "commons-io-1.3.1.jar", "commons-logging-1.0.4.jar", "serializer-2.7.0.jar", "xmlgraphics-commons-1.3.1.jar"]
DACAPO_JARS["h2"]           = ["derbyTesting.jar","junit-3.8.1.jar","h2-1.2.121.jar","tpcc.jar"]
DACAPO_JARS["jython"]       = ["jython.jar","antlr-3.1.3.jar", "asm-3.1.jar", "asm-commons-3.1.jar", "constantine-0.4.jar", "jna-posix.jar", "jna.jar"]
DACAPO_JARS["luindex"]      = ["luindex.jar", "lucene-core-2.4.jar", "lucene-demos-2.4.jar"]
DACAPO_JARS["lusearch"]     = ["lusearch.jar", "lucene-core-2.4.jar", "lucene-demos-2.4.jar"]
DACAPO_JARS["pmd"]          = ["pmd-4.2.5.jar", "jaxen-1.1.1.jar", "asm-3.1.jar", "junit-3.8.1.jar", "xercesImpl.jar", "xml-apis.jar"]
DACAPO_JARS["sunflow"]      = ["sunflow-0.07.2.jar", "janino-2.5.12.jar"]
DACAPO_JARS["tomcat"]       = ["dacapo-tomcat.jar", "bootstrap.jar","tomcat-juli.jar","commons-daemon.jar","commons-httpclient.jar", "commons-logging.jar","commons-codec.jar"] 
DACAPO_JARS["tradebeans"]   = ["daytrader.jar"]
DACAPO_JARS["tradesoap"]    = ["daytrader.jar"]
DACAPO_JARS["xalan"]        = ["xalan-benchmark.jar", "xalan.jar", "xercesImpl.jar", "xml-apis.jar", "serializer.jar"]
#==========#

#===== Configuracoes SPECJVM2008 =====#
SPEC        = DIR_BASE + "/benchmarks/SPECjvm2008"
SPEC_JAR    = SPEC + "/SPECjvm2008.jar"
SPEC_WT     = "0"
SPEC_IT     = "10"
SPEC_I      = "1"
SPEC_BT     = "1"
SPEC_OPS    = "5"
#==========#

def _createLog():
    #logging.basicConfig(level=logging.DEBUG,
    #                format='%(asctime)s [%(module)s] %(levelname)-8s %(message)s',
    #                datefmt='%Y-%m-%d %H:%M:%S')
    logger = logging.getLogger('ml4jit')
    logger.setLevel(logging.DEBUG)
    # create console handler with a higher log level
    ch = logging.StreamHandler()
    # create formatter and add it to the handlers
    formatter = logging.Formatter('%(asctime)s [%(module)-15s] %(levelname)-8s %(message)s', 
                                  '%Y-%m-%d %H:%M:%S')
    ch.setFormatter(formatter)
    # add the handlers to the logger
    logger.addHandler(ch)
    return logger    
    
_log = _createLog()
def getLog():
    return _log

def jarsDaCapo(prog):
    lista = DACAPO_JARS[prog]
    it = iter(lista)
    cp = DACAPO + "/jar/" + next(it)
    for l in it:
        cp += ":" + DACAPO + "/jar/" + l
    return cp

def criarDiretorios():
    '''
    Criar os diretorios caso nao existam
    '''
    if not os.path.exists(DADOS):
        os.makedirs(DADOS)
    if not os.path.exists(TEMPOS):
        os.makedirs(TEMPOS)
    if not os.path.exists(TREINOS):
        os.makedirs(TREINOS)
        
        
def remover():    
    '''
    Remove os logs e os arquivos gerados na execucao
    '''
    removerArquivo(DADOS + "/*")
    removerArquivo(DIR_BASE + "/ml4jit.log")
    removerArquivo(DIR_BASE + "/rvm.log")
    removerArquivo(DIR_BASE + "/compilationTime.txt")
    
def removerTempos():
    removerArquivo(TEMPOS + "/*")
    
def removerArquivo(caminho):
    for file in glob.glob(caminho):
        if os.path.isfile(file):
            subprocess.call(["rm", file])

if __name__ == '__main__':
    pass

