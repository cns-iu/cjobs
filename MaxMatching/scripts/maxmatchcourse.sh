# !/bin/bash
HOMEPATH=/people/mashut/
JAVAPATH=/usr/local/java/bin/
echo "cjobs is under " $HOMEPATH
echo "java path is " $JAVAPATH
echo "compile......"
$JAVAPATH/javac -d bin javasrc/RemovPunc.java javasrc/RemovStopw.java javasrc/MaxMatching.java javasrc/SkillExtractionCourse.java
cd bin
touch manifest.txt
echo "Manifest-version: 1.0" > manifest.txt
echo "Created-By: 1.0 (Shutian Ma)" >> manifest.txt
echo "Main-Class: cjobs.SkillExtractionCourse" >> manifest.txt
echo "Class-Path: lib/postgresql-42.1.4.jar" >> manifest.txt
$JAVAPATH/jar -cvfm SkillExtractionCourse.jar manifest.txt cjobs/*.class
echo "Move SkillExtractionCourse.jar to home dir"
mv SkillExtractionCourse.jar $HOMEPATH/cjobs
cd cjobs
echo "Run SkillExtractionCourse.jar"
time $JAVAPATH/java -jar SkillExtractionCourse.jar Output/skillcourse_max.txt Output/skillmaincourse_max.txt
