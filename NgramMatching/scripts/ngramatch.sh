# !/bin/bash
HOMEPATH=/people/mashut/
JAVAPATH=/usr/local/java/bin/
echo "cjobs is under " $HOMEPATH
echo "java path is " $JAVAPATH

echo "Compile ngram matching...."
$JAVAPATH/javac -d bin javasrc/RemovPunc.java javasrc/RemovStopw.java javasrc/NgramGeneration.java javasrc/NgramFilter.java
$JAVAPATH/javac -d bin javasrc/NgramSkillExtraction.java javasrc/RemovPunc.java javasrc/RemovStopw.java
cd bin
touch manifest.txt
echo "Manifest-version: 1.0" > manifest.txt
echo "Created-By: 1.0 (Shutian Ma)" >> manifest.txt
echo "Main-Class: cjobs.NgramFilter" >> manifest.txt
echo "Class-Path: lib/postgresql-42.1.4.jar" >> manifest.txt
$JAVAPATH/jar -cvfm NgramFilter.jar manifest.txt cjobs/*.class
echo "Move NgramFilter.jar to home dir"
mv NgramFilter.jar $HOMEPATH/cjobs
cd -P
cd cjobs
echo "Run NgramFilter.jar"
time $JAVAPATH/java -jar NgramFilter.jar
cd bin
touch manifest.txt
echo "Manifest-version: 1.0" > manifest.txt
echo "Created-By: 1.0 (Shutian Ma)" >> manifest.txt
echo "Main-Class: cjobs.NgramSkillExtraction" >> manifest.txt
echo "Class-Path: lib/postgresql-42.1.4.jar" >> manifest.txt
$JAVAPATH/jar -cvfm NgramSkillExtraction.jar manifest.txt cjobs/*.class
echo "Move NgramSkillExtraction.jar to home dir"
mv NgramSkillExtraction.jar $HOMEPATH/cjobs
cd -P
cd cjobs
echo "Run NgramSkillExtraction....."
time $JAVAPATH/java -jar NgramSkillExtraction.jar Output/skill_ngram.txt Output/skillmain_ngram.txt
