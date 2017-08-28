#!/bin/bash

cp ../src/*.java .

javac Multiset.java LinkedListMultiset.java SortedLinkedListMultiset.java BstMultiset.java HashMultiset.java BalTreeMultiset.java TestTimer.java

echo -n "BALTREE          : "
for file in ./*.in; do
    echo "$file" | awk -F'.' '{print $3}'
    java TestTimer baltree "$file"    
done

echo -n "HASH             : "
for file in ./*.in; do
    echo "$file" | awk -F'.' '{print $3}'
    java TestTimer hash "$file"    
done

echo -n "LINKEDLIST       : "
for file in ./*.in; do
    echo "$file" | awk -F'.' '{print $3}'
    java TestTimer linkedlist "$file"    
done

echo -n "SORTEDLINKEDLIST : "
for file in ./*.in; do
    echo "$file" | awk -F'.' '{print $3}'
    java TestTimer sortedlinkedlist "$file"    
done

echo -n "BST          : "
for file in ./*.in; do
    echo "$file" | awk -F'.' '{print $3}'
    java TestTimer bst "$file"    
done
    
rm *.java *.class
