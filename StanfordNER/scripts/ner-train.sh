# !/bin/bash
HOMEPATH=/people/mashut/
JAVAPATH=/usr/local/java/bin/
echo "cjobs is under " $HOMEPATH
echo "java path is " $JAVAPATH

echo "javac stanford ner"
$JAVAPATH/javac -d bin javasrc/BuildTrainTextSql.java
cd bin
touch manifest.txt
echo "Manifest-version: 1.0" > manifest.txt
echo "Created-By: 1.0 (Shutian Ma)" >> manifest.txt
echo "Main-Class: stanfordner.BuildTrainTextSql" >> manifest.txt
echo "Class-Path: lib/postgresql-42.1.4.jar" >> manifest.txt
$JAVAPATH/jar -cvfm BuildTrainTextSql.jar manifest.txt stanfordner/*.class
echo "Move BuildTrainTextSql.jar to home dir"
mv BuildTrainTextSql.jar $HOMEPATH/cjobs
cd -P
cd cjobs
echo "Run BuildTrainTextSql.jar"
echo "Building text to train...."
# output path             document length             records number
time $JAVAPATH/java -jar BuildTrainTextSql.jar stanford-ner/train.tsv
echo "Train with the text...."
cd stanford-ner
time $JAVAPATH/java -cp stanford-ner.jar edu.stanford.nlp.ie.crf.CRFClassifier -prop labelskill.prop

