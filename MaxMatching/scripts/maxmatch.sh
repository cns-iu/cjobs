# !/bin/bash
HOMEPATH=/people/mashut/
JAVAPATH=/usr/local/java/bin/
echo "cjobs is under " $HOMEPATH
echo "java path is " $JAVAPATH
echo "compile......"
$JAVAPATH/javac -d bin javasrc/RemovPunc.java javasrc/RemovStopw.java javasrc/MaxMatching.java javasrc/SkillExtraction.java
cd bin
touch manifest.txt
echo "Manifest-version: 1.0" > manifest.txt
echo "Created-By: 1.0 (Shutian Ma)" >> manifest.txt
echo "Main-Class: cjobs.SkillExtraction" >> manifest.txt
echo "Class-Path: lib/postgresql-42.1.4.jar" >> manifest.txt
$JAVAPATH/jar -cvfm SkillExtraction.jar manifest.txt cjobs/*.class
echo "Move SkillExtraction.jar to home dir"
mv SkillExtraction.jar $HOMEPATH/cjobs
cd cjobs
echo "Run SkillExtraction.jar"
time $JAVAPATH/java -jar SkillExtraction.jar Output/skill_max.txt Output/skillmain_max.txt
