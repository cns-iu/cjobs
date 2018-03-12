# !/bin/bash
HOMEPATH=/people/mashut
JAVAPATH=/usr/local/java/bin
echo "cjobs is under path " $HOMEPATH
echo "Java is under path " $JAVAPATH
echo "----------Exact Match------------"
echo "Compile now..."
$JAVAPATH/javac -d bin javasrc/ExactM.java
cd bin
touch manifest.txt
echo "Manifest-version: 1.0" > manifest.txt
echo "Created-By: 1.0 (Shutian Ma)" >> manifest.txt
echo "Main-Class: Matching.ExactM" >> manifest.txt
echo "Class-Path: lib/postgresql-42.1.4.jar" >> manifest.txt
$JAVAPATH/jar -cvfm ExactM.jar manifest.txt Matching/*.class
echo "Move ExactM.jar to home dir"
mv ExactM.jar $HOMEPATH/cjobs
cd -P
cd cjobs
echo "Run ExactM.jar"
time $JAVAPATH/java -jar ExactM.jar Output/job_none.txt 
