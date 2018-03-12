import re
import sys

# Create an empy dictionary
S={}
RS={} # reverse dictionary
skillslist = sys.argv[1]#'voronoi_cluster_DSDE2010.txt'
nodesid =  sys.argv[2]#'voronoi_nodes_DSDE2010.txt'
out = sys.argv[3]#'out.txt'
with open(nodesid, 'r', encoding='latin1') as f:
    for line in f:
        line=''.join([c if ord(c) < 128 else '?' for c in line])
        line=line.strip().split('\t')
        s = line[1].strip()
        s = s.replace("\"","")
        S[int(line[0])]=s.lower()
        RS=dict((v,k) for k,v in list(S.items()))

        
# load gmap skills

wikiformat = []
with open(skillslist,'r',encoding='latin1') as f:
 #   print(skills)
    
    for line in f:
        skillline = []
 #       print('debug3')
        line=''.join([c if ord(c) < 128 else '?' for c in line])
        line=line.strip().split('\t')
        cat = line[1].strip()
        family = line[0]
        family = family.replace(" ","_")
        family = family.replace(",","")
        skills = line[1:]
        for s in skills:
 #           print(S[1])
            line = s.strip()
            line = line.replace("\"","")
            line = line.lower()
 #           print(line)
            skey = RS[line]
 #           print(skey)
            skillline+=[skey]
 #           print(skillline)
        skillnodes = ' '.join(map(str, skillline))
 #      print(skillnodes)
        combinefamilynode =''.join([family,skillnodes])
 #       print(combinefamilynode)
        wikiformat+=[combinefamilynode]
        
with open(out, "w") as f:
    k=0
    for i in wikiformat:
        f.write(wikiformat[k] + '\n')
        k+=1
            

 #print ', '.join(mylist)
