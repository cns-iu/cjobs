# Data Scientist and Data Engineer Jobs 2010-2016 Graph
*timeseries-job-courses-pub2010-2016.Rmd* - Read csv file as input containing the distribution of 
jobs, courses, publications per month/year and print a time graph as output.


# Radial Tree

Usage from a terminal:
`Rscript radial-tree.R input-file output-image`

Sample: `Rscript radial-tree.R https://cdn.rawgit.com/christophergandrud/networkD3/master/JSONdata/flare.json tree.jpeg`

Radial tree is used to view all skills co-occurences in a hierarchical structure. The original file has the following 4-column format: 
**skills-family** TAB **skills-cluster** TAB **skills** TAB **count** - text format

Parser (*tree_parser_test2json.py*) is build in python to convert a text file into a json format for radial-tree.

Usage: `python tree_parser_test2json.py input-file output-file`

Add a desired root node to the tree.

# Uzzi Similarity score
Constructing Zscore (Uzzi similarity score) network from co-occurrence network and Isomap embedding
Input 2 files:
File 1 Format nodes.txt (id and skill label)

1	"Business Systems Analysis"

2	"Writing"

3	"Statistics"

4	"Simulation"

File 2 Format edges.txt (source id target id weight)

1	2	 1

1	3	 1

1	4	 1

1	5	 2

1	6	 1

Output: depopulization graph and scatter plot

Code: *uzzi-similarity-py3.py* (for python 3.x)

Run with 4 arguments: `python uzzi-similarity-py3.py nodes.txt edges.txt output-depopularization.png output-scatterplot.png`

# Run GMAP

File `GMAP-Lingfei` - author Lingfei Wu https://www.knowledgelab.org/people/detail/lingfei_wu/

Input 2 files:

File 1  nodes.txt (id label)

1 Excel

2 Administration

File 2 edges.txt (source-id target-id weight)

1	2	 1

1	3	 1

Open jupyter notebook. Make changes to files input names. Adjust the number of cluster (currently `Ncluster=3`). If labels are needed, display the top # of label. For example, find the top 3 %:  `np.percentile(list(K.values()),97),0.03*3000`. Copy the diplayed numer, for example, `1714`, to the *nodeClusterLabel*: `if k>1744:`. If all labels are needed, change it to `0`.

# Conversion Script for GMAP

Input 2 files:

File 1  Skill nodes.txt

1 Excel

2 Administration

File 2 SkillFamilyCluster.txt

Category:Business;  TAB Excel TAB Administration
Category:Travel; TAB Comunication skills

Code: *labelToNodeId.py* for python 3.x

Run with 3 arguments: `python labelToNodeId.py nodes.txt SkillFamilyCluster.txt output-text-file.txt`

Output file format:

Category:Business; 203 103 2

