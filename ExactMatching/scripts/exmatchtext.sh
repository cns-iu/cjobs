# !/bin/bash
HOMEPATH=/people/mashut
JAVAPATH=/usr/local/java/bin
echo "cjobs is under path " $HOMEPATH
echo "Java is under path " $JAVAPATH
echo "----------Exact Match------------"
echo "Compile now..."
$JAVAPATH/javac -d bin javasrc/ExactMtext.java
cd bin
touch manifest.txt
echo "Manifest-version: 1.0" > manifest.txt
echo "Created-By: 1.0 (Shutian Ma)" >> manifest.txt
echo "Main-Class: cjobs.ExactMtext" >> manifest.txt
echo "Class-Path: lib/postgresql-42.1.4.jar" >> manifest.txt
$JAVAPATH/jar -cvfm ExactMtext.jar manifest.txt cjobs/*.class
echo "Move ExactMtext.jar to home dir"
mv ExactMtext.jar $HOMEPATH/cjobs
cd -P
cd cjobs
echo "Run ExactMtext.jar"
time $JAVAPATH/java -jar ExactMtext.jar Output/skill_exact.txt Output/skillmain_exact.txt 
