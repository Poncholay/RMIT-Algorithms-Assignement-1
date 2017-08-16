cp ../src/*.java .
python2.7 assign1TestScript.py -v ./ baltree *.in
python2.7 assign1TestScript.py -v ./ hash *.in
python2.7 assign1TestScript.py -v ./ linkedlist *.in
python2.7 assign1TestScript.py -v ./ sortedlinkedlist *.in
python2.7 assign1TestScript.py -v ./ bst *.in
rm *.java *.class
mkdir -p output
mv *.out output
