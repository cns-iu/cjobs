# !/bin/bash
HOMEPATH=`pwd`
JAVAPATH=/usr/local/java/bin/
echo "cjobs is under " $HOMEPATH
echo "java path is " $JAVAPATH

echo "javac stanford ner"
$JAVAPATH/javac -d bin -classpath `pwd`/lib/bsh-1.3.0.jar:`pwd`/lib/jcommander-1.27.jar:`pwd`/lib/postgresql-42.1.4.jar:`pwd`/lib/stanford-ner.jar javasrc/TestData.java javasrc/ResultGeneration.java
cd bin
touch manifest.txt
echo "Manifest-version: 1.0" > manifest.txt
echo "Created-By: 1.0 (Shutian Ma)" >> manifest.txt
echo "Main-Class: stanfordtest.TestData" >> manifest.txt
echo "Class-Path: $HOMEPATH/lib/bsh-1.3.0.jar $HOMEPATH/lib/jcommander-1.27.jar $HOMEPATH/lib/postgresql-42.1.4.jar $HOMEPATH/lib/stanford-ner.jar" >> manifest.txt
$JAVAPATH/jar -cvfm TestData.jar manifest.txt stanfordtest/*.class
echo "Move TestData.jar to home dir"
mv TestData.jar $HOMEPATH
cd -P
cd cjobs
echo "Run TestData.jar"
time $JAVAPATH/java -jar TestData.jar stanford-ner/ner-model-skill.ser.gz Output/skill_ner.txt Output/skillmain_ner.txt 


