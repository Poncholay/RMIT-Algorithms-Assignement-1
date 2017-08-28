cp ../src/*.java .

for file in ./*search*.in; do
    echo "_____________________"
    echo "BALTREE"
    echo "_____________________"
    python2.7 assign1TestScript.py -v ./ baltree "$file"

    echo "_____________________"
    echo "HASH"
    echo "_____________________"
    python2.7 assign1TestScript.py -v ./ hash "$file"

    echo "_____________________"
    echo "LINKEDLIST"
    echo "_____________________"
    python2.7 assign1TestScript.py -v ./ linkedlist "$file"

    echo "_____________________"
    echo "SORTEDLINKEDLIST"
    echo "_____________________"
    python2.7 assign1TestScript.py -v ./ sortedlinkedlist "$file"

    echo "_____________________"
    echo "BST"
    echo "_____________________"
    python2.7 assign1TestScript.py -v ./ bst "$file"
done

rm *.java *.class
mkdir -p output
mv *.out output
