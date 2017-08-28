#!/bin/bash

cp ../src/*.java .

javac Multiset.java LinkedListMultiset.java SortedLinkedListMultiset.java BstMultiset.java HashMultiset.java BalTreeMultiset.java TestTimer.java

for file in ./*.in; do
    echo "$file" | awk -F'.' '{print $3}'
    echo -n "BALTREE          : "
    java TestTimer baltree "$file"
    
    echo -n "HASH             : "
    java TestTimer hash "$file"

    echo -n "LINKEDLIST       : "
    java TestTimer linkedlist "$file"

    echo -n "SORTEDLINKEDLIST : "
    java TestTimer sortedlinkedlist "$file"

    echo -n "BST              : "
    java TestTimer bst "$file"
    echo "_____________________"
done
    
rm *.java *.class
