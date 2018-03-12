import numpy
import scipy
import matplotlib
#import matplotlib.cm as cm
import statsmodels.api as sm
from os import listdir
from scipy.stats.stats import pearsonr
import json
import random
import itertools
from copy import deepcopy
import time
import scipy as sp
from scipy.sparse import csr_matrix
import matplotlib.cm as cm
from numpy.random import choice
import itertools
from sklearn import manifold
import operator
import itertools
from datetime import datetime as dt
import matplotlib.mlab as mlab
from scipy.stats import norm
import re
from scipy import stats
from scipy.spatial import Voronoi
from scipy import sparse
from sklearn.metrics.pairwise import cosine_similarity
import sys
from collections import defaultdict, Counter
import networkx as nx
import numpy as np
import pylab as plt
import math
import re
from sklearn.manifold import TSNE
from sklearn.manifold import MDS
from sklearn.cluster import KMeans

# Create an empy dictionary
S={}
RS={} # reverse dictionary
# Read lines, split by tab, create a list and pre-process labels to lower cases
filenameskills = sys.argv[1]
filenameedges = sys.argv[2]
depopularizeplot = sys.argv[3]
scatterplot = sys.argv[4]
with open(filenameskills,'r') as f:
    for line in f:
        line=line.strip().split('\t')
        s=line[1].strip().split('"')[1]
        S[int(line[0])]=s.lower()
RS=dict((v,k) for k,v in list(S.items()))
len(S),len(RS)

# Uzzi random shuffling
#see this article for more details: http://science.sciencemag.org/content/342/6157/468

#unfold co-occurance 
From=[]
To=[]
with open(filenameedges,'r') as f:
    for line in f:
        line=''.join([c if ord(c) < 128 else '?' for c in line])
        i,j,w=list(map(int,line.strip().split('\t')))
        #G.add_edge(i,j,weight=w)
        a=min(i,j)
        b=max(i,j)
        x,y=list(zip(*[[a,b]]*w))
        From+=x
        To+=y

#len(From),len(To) #(147821, 147821)

D=defaultdict(lambda:0)
for x,y in zip(From,To):
    D[(x,y)]+=1
D=dict(D)

DD=defaultdict(lambda:[])
Nshuffle=100# number of shuffling times
for i in range(Nshuffle):
 #   flushPrint(str(i))
    D1=defaultdict(lambda:0)
    To1=np.random.permutation(To)
    for x,y in zip(From,To1):
        D1[(x,y)]+=1
    for x,y in D1:
        DD[(x,y)].append(D1[(x,y)])
DD=dict(DD)


# In[10]:

# construct original network
G=nx.Graph()
for x,y in D:
    G.add_edge(S[x].lower(),S[y].lower(),weight=D[(x,y)])


# In[11]:

#investigate the effect of shuffling: de-popularize to distill similarity 

d=[]
for x,y in D:
    if (x,y) in DD:
        m=np.mean(DD[(x,y)]) 
        sd=np.std(DD[(x,y)])
        if sd==0:
            sd=1
        w=D[(x,y)]
        z1=w-m
        z=(w-m+0.0)/sd
        d.append([G.degree()[S[x]],G.degree()[S[y]], m, sd, w, z1, z ])
ki,kj,m,sd,w,z1,z=np.array(d).T

#print("debug0\n")
#print(len(d))
# In[12]:

fig = plt.figure(figsize=(10, 6),facecolor='white')
cmap = cm.get_cmap('Accent',5)
ax = fig.add_subplot(231)
plt.scatter(ki*kj,w,facecolor=cmap(0),edgecolor='none',s=3,alpha=0.5)
plt.xscale('log')
plt.yscale('log')
plt.plot([1,10000],[1,10000],linestyle='--',color='gray',label='y=x')
plt.legend(loc=2,frameon=False)
plt.xlabel(r'$K_i*K_j$',size=18)
plt.ylabel(r'$W_{ij}$',size=18)
plt.ylim(1,10**5)

ax = fig.add_subplot(232)
plt.scatter(ki*kj,m,facecolor=cmap(1),edgecolor='none',s=3,alpha=0.5)
plt.xscale('log')
plt.yscale('log')
plt.plot([1,10000],[1,10000],linestyle='--',color='gray',label='y=x')
plt.legend(loc=2,frameon=False)
plt.xlabel(r'$K_i*K_j$',size=18)
plt.ylabel(r'$E(W_{ij})$',size=18)
plt.ylim(1,10**5)

ax = fig.add_subplot(233)
plt.scatter(ki*kj,sd,facecolor=cmap(2),edgecolor='none',s=3,alpha=0.5)
plt.xscale('log')
plt.yscale('log')
plt.plot([1,10000],[1,10000],linestyle='--',color='gray',label='y=x')
plt.legend(loc=2,frameon=False)
plt.xlabel(r'$K_i*K_j$',size=18)
plt.ylabel(r'$SD(W_{ij})$',size=18)
plt.ylim(0.1,10**5)

ax = fig.add_subplot(234)
plt.scatter(ki*kj,z1,facecolor=cmap(3),edgecolor='none',s=3,alpha=0.5)
plt.xscale('log')
#plt.yscale('log')
#plt.plot([1,10000],[1,10000],linestyle='--',color='gray',label='y=x')
#plt.legend(loc=2,frameon=False)
plt.xlabel(r'$K_i*K_j$',size=18)
plt.ylabel(r'$W_{ij} - E(W_{ij})$',size=18)
plt.ylim(-50,200)

ax = fig.add_subplot(235)
plt.scatter(ki*kj,z,facecolor=cmap(4),edgecolor='none',s=3,alpha=0.5)
plt.xscale('log')
#plt.yscale('log')
#plt.plot([1,10000],[1,10000],linestyle='--',color='gray',label='y=x')
#plt.legend(loc=2,frameon=False)
plt.xlabel(r'$K_i*K_j$',size=18)
plt.ylabel(r'$Zscore = \frac{W_{ij} - E(W_{ij})}{SD(W_{ij})}$',size=18)
plt.ylim(-50,200)

ax = fig.add_subplot(236)
mz=np.mean(z)
plt.plot([mz,mz],[0,16000],'r-',linewidth=2,linestyle='--',label=str(np.round(mz,2)))
plt.hist(z,50,color='RoyalBlue',alpha=0.7)
plt.xlim(-10,20)
plt.legend(loc=1,frameon=False)
plt.ylabel(r'$Frequency$',size=18)
plt.xlabel(r'$Zscore $',size=18)

plt.savefig(depopularizeplot)
#plt.savefig('Depopularize.pdf')


# Zscore (Uzzi similarity score network)

Z=defaultdict(lambda:defaultdict(lambda:0))
for x,y in D:
    if (x,y) in DD:
        m=np.mean(DD[(x,y)]) 
        sd=np.std(DD[(x,y)])
        w=D[(x,y)]
        if sd==0:
            sd=1
        z=(w-m+0.0)/sd
        Z[x][y]=z
        Z[y][x]=z
Z=dict(Z)

#select the top two friends in Zscore network

GZ=nx.Graph()
for i in Z:
    topZscore=[a for a,b in sorted(list(Z[i].items()),key=lambda x:-x[1])][:2]
    for j in topZscore:
        GZ.add_edge(S[i],S[j])

#check connection : we want to make sure the entire network is connected, or esle more friens should be selected in 
# the last step
nx.number_connected_components(GZ)


# Isomap embedding based on the network distance


def ISOMAP(graph,method):
    dists = []
    p=nx.shortest_path_length(graph)
    #p=nx.all_pairs_dijkstra_path_length(G3)
    n=0
    nodes=list(graph.nodes().keys())
    for i in p:
        n+=1
        if n%100==0:
            flushPrint(str(n/100))#88
        vs=[i[1][j] for j in nodes]
        dists.append(vs)
    adist = np.array(dists)
    amax = np.amax(adist)
    adist = adist/float(amax)
    if method=='MDS':
        coords= MDS(n_components=2, dissimilarity="precomputed", random_state=6).fit_transform(adist)    
    if method=='TSNE':
        coords = TSNE(n_components=2,metric="precomputed").fit_transform(adist)
    dic=dict((i,j) for i,j in zip(nodes,coords))  
    return dic


dic=ISOMAP(GZ,'TSNE')


# Kmeans to cluster nodes in low-dimension space

X=np.array(list(dic.values()))
kmeans = KMeans(n_clusters=3, random_state=0).fit(X)
group=dict(list(zip(list(dic.keys()),kmeans.labels_)))


# save the coordinates out
'''
with open('skillPosBG.txt', "wb") as f:
    for i in dic:
        k=G.degree()[i]
        x,y=dic[i]
        g=group[i]
        f.write(i+'\t'+ str(x)+ '\t'+ str(y)+ '\t'+ str(k)+'\t'+ str(g) + '\n')
'''

#plot the demonstation 

fig = plt.figure(figsize = (15, 15))
cmap = cm.get_cmap('rainbow',3)
x,y=np.array(list(dic.values())).T
sizes=[G.degree()[i] for i in list(GZ.nodes().keys())]
gs=[group[i] for i in dic]
plt.scatter(x, y, s = sizes, c=gs,alpha = 0.4)
for i in dic:
    k=G.degree()[i]
    if k>200:
        #if k>300:
        #    k=300
        x,y=dic[i]
        g=group[i]
        plt.text(x,y,i,fontsize=k/30.0+3,color=cmap(g))
plt.savefig(scatterplot)
