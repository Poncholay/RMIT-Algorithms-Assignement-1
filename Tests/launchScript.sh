cp ../src/*.java .
echo "_____________________"
echo "BALTREE"
echo "_____________________"
time python2.7 assign1TestScript.py -v ./ baltree *.in

echo "_____________________"
echo "HASH"
echo "_____________________"
time python2.7 assign1TestScript.py -v ./ hash *.in

echo "_____________________"
echo "LINKEDLIST"
echo "_____________________"
time python2.7 assign1TestScript.py -v ./ linkedlist *.in

echo "_____________________"
echo "SORTEDLINKEDLIST"
echo "_____________________"
time python2.7 assign1TestScript.py -v ./ sortedlinkedlist *.in

echo "_____________________"
echo "BST"
echo "_____________________"
time python2.7 assign1TestScript.py -v ./ bst *.in

rm *.java *.class
mkdir -p output
mv *.out output
