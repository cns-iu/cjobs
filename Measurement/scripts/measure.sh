HOMEPATH=/people/mashut/
JAVAPATH=/usr/local/java/bin/

echo "cjobs is under " $HOMEPATH
echo "java path is " $JAVAPATH
echo "javac measurement"
$JAVAPATH/javac -d bin javasrc/Measurement.java javasrc/PRmeasure.java
cd bin
touch manifest.txt
echo "Manifest-version: 1.0" > manifest.txt
echo "Created-By: 1.0 (Shutian Ma)" >> manifest.txt
echo "Main-Class: F1Measure.Measurement" >> manifest.txt
echo "Class-Path: lib/postgresql-42.1.4.jar" >> manifest.txt
$JAVAPATH/jar -cvfm Measure.jar manifest.txt F1Measure/*.class
echo "Move Measure.jar to home dir"
mv Measure.jar $HOMEPATH/cjobs
cd -P
cd cjobs
echo "Run Measure.jar"
nohup time $JAVAPATH/java -jar Measure.jar Output/validation.txt 'exact'
nohup time $JAVAPATH/java -jar Measure.jar Output/validation.txt 'max'
nohup time $JAVAPATH/java -jar Measure.jar Output/validation.txt 'ngram'
nohup time $JAVAPATH/java -jar Measure.jar Output/validation.txt 'ner'
nohup time $JAVAPATH/java -jar Measure.jar Output/validation.txt 'llda3'
nohup time $JAVAPATH/java -jar Measure.jar Output/validation.txt 'llda5'

