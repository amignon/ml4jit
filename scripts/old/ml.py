# -*- coding: utf-8 -*-
"""
Created on Mon Feb  6 12:59:10 2017

@author: ale
"""

from pandas import DataFrame, read_csv
from sklearn.linear_model import LinearRegression
import pandas as pd
import numpy as np
import glob
from sklearn import svm
from sklearn import tree
from sklearn import neighbors
from sklearn.ensemble import RandomForestClassifier

from sklearn.multioutput import MultiOutputRegressor
from sklearn.ensemble import GradientBoostingRegressor
from sklearn.ensemble import BaggingRegressor
import config

class ML():
    def __init__(self):
        train = processarArquivos()    
        self.columns = trainColumns(train.columns.tolist())
        clf = createClassifier()
        self.clf = clf.fit(train[self.columns], train[targetColumns()])
        
    def prediction(self, data):
        df = pd.DataFrame(columns=getColumns())
        df.loc[0] = data
        pred = self.clf.predict(df[self.columns])
        print pred[0]
        res = ''
        for p in pred[0]:     
            if p > 0.5:
                res += "1"
            else:
                res += "0"
            #res = res + str(int(p))
        print res
        return int(res, 2)
        

def createClassifier():
    
    #return tree.DecisionTreeClassifier()
    return neighbors.KNeighborsClassifier(n_neighbors = 1)
    #return neighbors.RadiusNeighborsClassifier(3)
    #return RandomForestClassifier(n_estimators=1)
    #return MultiOutputRegressor(GradientBoostingRegressor(random_state=0))
    #return MultiOutputRegressor(tree.DecisionTreeRegressor())
    
def getColumns():
    return ['ID','Nome','Bytecodes','Locals','Synch','Exceptions','Leaf','Final','Private','Static','Array_Load', 'Array_Store','Arithmetic','Compare','Branch', 'Switch','Put', 'Get','Invoke','New','ArrayLength','Athrow','Checkcast','Monitor','Multi_newarray', 'Conversion'];
    
def processarArquivos():
    lista = []
    for file in glob.glob(config.TREINOS + "/treino_" + config.ML_PLAN + "_*.csv"):   
        lista.append(pd.read_csv(file))
    return pd.concat(lista, ignore_index=True)
    
def trainColumns(columns):
    excluir = ['ID', 'Nome']
    for i in range(config.ML_LEN):
        excluir.append('O' + str(i+1))
    return [c for c in columns if c not in excluir]
    
def targetColumns():
    lista = []
    for i in range(config.ML_LEN):
        lista.append('O' + str(i+1))
    return lista
    
def processar(carac):
    dados = carac.split(",")

def DecisionTree(train, test, columns):
    clf = tree.DecisionTreeClassifier()
    clf = clf.fit(train[columns], train[target])
    predictions = clf.predict(test[columns])
    print predictions

if __name__ == "__main__":
    ml = ML()
    
    s = "-1591438954,heapsort.NumericSortTest.NumSift(II),92,5,0,0,1,0,1,0,0.06521739,0.02173913,0.054347824,0.0,0.06521739,0.0,0.0,0.08695652,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0".split(",")
    r =  ml.prediction(s)
    print r
    
    
    
    '''
    train = processarArquivos()
    s = "2121695142,teste.Main.f1(),9,0,0,0,0,0,0,1,0.0,0.0,0.0,0.0,0.0,0.0,0.11111111,0.11111111,0.0,0.0,0.0,0.0,0.0,0.0".split(",");
    #s = "2121696103,teste.Main.f2(),8,0,0,0,0,0,0,1,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.125,0.125,0.0,0.125,0.0,0.0,0.0".split(",");
    print s
    
    df = pd.DataFrame(columns=getColumns())
    df.loc[0] = s
    print df
    
    columns = train.columns.tolist()
    columns = [c for c in columns if c not in ['ID', 'Nome', 'O1', 'O2', 'O3', 'O4']]
    target = ['O1', 'O2', 'O3', 'O4']
    #target = np.ravel(target)
    
    
    #train = df.sample(frac=0.8, random_state=1)
    #test = df.loc[~df.index.isin(train.index)]
    DecisionTree(train, df, columns)
'''