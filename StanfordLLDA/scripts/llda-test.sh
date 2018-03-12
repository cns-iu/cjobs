# !/bin/bash
HOMEPATH=/people/mashut/
JAVAPATH=/usr/local/java/bin/
echo "cjobs is under " $HOMEPATH
echo "java path is " $JAVAPATH

echo "javac stanford llda"
$JAVAPATH/javac -d bin javasrc/BuildTestCsvSql.java javasrc/RemovStopw.java javasrc/RemovPunc.java
cd bin
touch manifest.txt
echo "Manifest-version: 1.0" > manifest.txt
echo "Created-By: 1.0 (Shutian Ma)" >> manifest.txt
echo "Main-Class: cjobs.BuildTestCsvSql" >> manifest.txt
echo "Class-Path: lib/postgresql-42.1.4.jar" >> manifest.txt
$JAVAPATH/jar -cvfm BuildTestCsvSql.jar manifest.txt cjobs/*.class
echo "Move BuildTrainCsvSql.jar to home dir"
mv BuildTestCsvSql.jar $HOMEPATH/cjobs
cd -P
cd cjobs
echo "Run BuildTestCsvSql.jar"
echo "Building text to train...."
time $JAVAPATH/java -jar BuildTestCsvSql.jar
echo "Test with the text...."
cd llda
time /usr/local/jdk1.6/bin/java -jar tmt-0.4.0.jar lda-infer.scala

