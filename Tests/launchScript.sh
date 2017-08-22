cp ../src/*.java .
time python2.7 assign1TestScript.py -v ./ baltree *.in
time python2.7 assign1TestScript.py -v ./ hash *.in
time python2.7 assign1TestScript.py -v ./ linkedlist *.in
time python2.7 assign1TestScript.py -v ./ sortedlinkedlist *.in
time python2.7 assign1TestScript.py -v ./ bst *.in
rm *.java *.class
mkdir -p output
mv *.out output
