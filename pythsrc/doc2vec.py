# -*- coding: utf-8 -*-
import sys
reload(sys)
sys.setdefaultencoding('utf8')
import gensim, logging
import os

# logging information
logging.basicConfig(format='%(asctime)s : %(levelname)s : %(message)s', level=logging.INFO)
print 'here'

# get input file, text format
f = open('C:\Users\mashut\data\cjobs\origin\cjob-data for d2v.txt','r')
input = f.readlines()
count = len(input)
print count

# read file and separate words
alldocs=[] # for the sake of check, can be removed
count=0 # for the sake of check, can be removed
for line in input:
    line=line.strip('\n')
    alldocs.append(gensim.models.doc2vec.TaggedDocument(line,count)) # for the sake of check, can be removed
    count+=1 # for the sake of check, can be removed
'''
# PV-DM w/concatenation - window=5 (both sides) approximates paper's 10-word total window size
Doc2Vec(sentences,dm=1, dm_concat=1, size=100, window=2, hs=0, min_count=2, workers=cores)
# PV-DBOW  
Doc2Vec(sentences,dm=0, size=100, hs=0, min_count=2, workers=cores)
# PV-DM w/average
Doc2Vec(sentences,dm=1, dm_mean=1, size=100, window=2, hs=0, min_count=2, workers=cores)
'''
# train and save the model
sentences= gensim.models.doc2vec.TaggedLineDocument('output.seq')
model = gensim.models.Doc2Vec(sentences,size=100, window=3)
model.train(sentences)
model.save('C:\Users\mashut\data\cjobs\origin\all_model.txt')


# save vectors
out=open("C:\Users\mashut\data\cjobs\origin\all_vector.txt","wb")
for num in range(0,count):
    docvec =model.docvecs[num]
    out.write(docvec)
    #print num
    #print docvec
out.close()
'''
# test, calculate the similarity
# 注意 docid 是从0开始计数的
# 计算与训练集中第一篇文档最相似的文档
sims = model.docvecs.most_similar(0)
print sims
# get similarity between doc1 and doc2 in the training data
sims = model.docvecs.similarity(1,2)
print sims
'''







