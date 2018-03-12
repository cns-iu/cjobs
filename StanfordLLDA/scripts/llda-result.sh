# !/bin/bash
HOMEPATH=/people/mashut/
JAVAPATH=/usr/local/java/bin/
echo "cjobs is under " $HOMEPATH
echo "java path is " $JAVAPATH

echo "javac stanford llda"
$JAVAPATH/javac -d bin javasrc/lldaresults.java
cd bin
touch manifest.txt
echo "Manifest-version: 1.0" > manifest.txt
echo "Created-By: 1.0 (Shutian Ma)" >> manifest.txt
echo "Main-Class: cjobs.lldaresults" >> manifest.txt
echo "Class-Path: lib/postgresql-42.1.4.jar" >> manifest.txt
$JAVAPATH/jar -cvfm lldaresults.jar manifest.txt cjobs/*.class
echo "Move lldaresults.jar to home dir"
mv lldaresults.jar $HOMEPATH/cjobs
cd -P
cd cjobs
echo "Run lldaresults.jar"
echo "Run...."
time $JAVAPATH/java -jar lldaresults.jar '3' Output/skill_llda3.txt Output/skillmain_llda3.txt
time $JAVAPATH/java -jar lldaresults.jar '5' Output/skill_llda5.txt Output/skillmain_llda5.txt

