##Function name: GetSimilarWiki.py
##Author: Shutian Ma
##Date Written: 1.2018
##Function Description: Return the top-n similar wiki term according to each job/course
##-----------------------------------------
##Static:
##d2vmodel which is the file path of trained model
##
##



from __future__ import division

from gensim import corpora, models, similarities
from scipy.sparse import csr_matrix, coo_matrix
#import gensim.models.keyedvectors as word2vec
import pickle
import numpy as np
import gensim.models as g
import sys,os
import logging
logging.basicConfig(format='%(asctime)s : %(levelname)s : %(message)s', level=logging.INFO)

#file path for trained model
d2vmodel= sys.argv[1]
#file path for input data
#each line is a piece of job: jobid title and description 
jobdata = sys.argv[2]
#file path for output file
#number of wiki term which are top 50 similar with that piece of job
output = sys.argv[3]


#loading the trained model
m = g.Doc2Vec.load(d2vmodel)

#read the job data into ilines
inferdoc = open(jobdata)
ilines = inferdoc.readlines()
inferdoc.close()

#write the result to ouput
result=open(output,'a')
for iline in ilines:
	line = iline.split('\t')
	#write the jobid first
	result.write(line[0]+'\t')
	#for each line, wiki index will be separated by tab
	infer_vector=m.infer_vector(iline.replace('\t',' '))
	#you can set top n here, now is 50
	similar_documents = m.docvecs.most_similar([infer_vector], topn = 50)
	for similar_document in similar_documents:
		result.write(str(similar_document[0])+' ')
	result.write('\r')
result.close() 
