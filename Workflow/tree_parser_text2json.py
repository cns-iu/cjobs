import csv
from collections import defaultdict
import json
import re
import sys
import codecs
import sys
import io
#from cStringIO import StringIO

def ctree():
    """ One of the python gems. Making possible to have dynamic tree structure.

    """
    return defaultdict(ctree)
def build_leaf(name, leaf):
    """ Recursive function to build desired custom tree structure

    """
    res = {"name": name}

    # add children node if the leaf actually has any children
    if len(leaf.keys()) > 0:
        res["children"] = [build_leaf(k, v) for k, v in leaf.items()]

    return res

def main():
    """ The main thread composed from two parts.

    First it's parsing the tab separated text file and builds a tree hierarchy from it.
    Second it's recursively iterating over the tree and building custom
    json-like structure (via dict).

    And the last part is just printing the result.

    """
    tree = ctree()
    filename = sys.argv[1]
    out = sys.argv[2]
 #   filename = 'radial_tree_skills_MOCK_DATA.txt'
    with io.open(filename, "r",encoding="utf-8") as csvfile:
        reader = csv.reader(csvfile, delimiter='\t')
        for rid, row in enumerate(reader):
            leaf = tree[row[0]]
 #           print(leaf)
            for cid in range(1, (len(row)-1)):
                leaf = leaf[row[cid]]

    # building a custom tree structure
    res = []
    for name, leaf in tree.items():
        res.append(build_leaf(name, leaf))
    with io.open(out, 'w',encoding='utf-8') as outfile:
        json.dump(res, outfile)

main()
