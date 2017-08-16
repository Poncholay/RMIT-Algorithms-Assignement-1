#
# Script to perform automated testing for assignment 1 of AA, 2017.
#
# The provided Python script will be the same one used to test your implementation.  
# We will be testing your code on the core teaching servers (titan, jupiter etc), so please try your code there.  
# The script first compiles your Java code, runs one of the five implementations then runs a series of test.  
# Each test consists of sequence of operations to execute, whose results will be saved to file, then compared against 
# the expected output.  If output from the tested implementation is the same as expected (script is tolerant for
# some formatting differences, but please try to stick to space separated output), then we pass that test.
# Otherwise, difference will be printed via 'diff' (if in verbose mode, see below).
#
# Usage, assuming you are in the directory where the test script " assign1TestScript.py" is located.
#
# > python assign1TestScript.py [-v] <codeDirectory> <name of implementation to test> <list of input files to test on>
#
#options:
#
#    -v : verbose mode
#
#Input:
#
#   code directory : directory where the Java files reside.  E.g., if directory specified is Assign1-s1234,
#        then Assign1-s1234/MultisetTester.java should exist.  This is also where the script
#        expects your program to be compiled and created in, e.g., Assign2-s1234/MultisetTester.class.
#    name of implementation to test: This is the name of the implemention to test.  The names
#        should be the same as specified in the script or in MultisetTest.java
#    input files: these are the input files, where each file is a list of commands to execute.  
#        IMPORTANT, the expected output file for the print operation must be in the same directory
#        as the input files, and the should have the same basename - e.g., if we have input operation
#        file of "test1.in", then we should have expected file "test1.exp".  Similarly, the
#        expected output file for the search operation must also be in the same directory and have
#        the same basename - e.g., using the same example, if input file is "test1.in", then the
#        expected file name for search results is "test1.search.exp"
#
#
# As an example, I can run the code like this when testing code directory "Assign1-s1234",
# all my input and expected files are located in a directory called "tests" 
# and named test1.in, test2.in and testing for hash table implementation:
#
#> python assign1TestScript.py -v   Assign1-s1234    hash     tests/test1.in    tests/test2.in
#
#
#
# Jeffrey Chan & Yongli Ren, 2017
#

import string
import csv
import sets
import getopt
import os
import os.path
import re
import sys
import subprocess as sp


def main():

    # process command line arguments
    try:
        # option list
        sOptions = "v"
        # get options
        optList, remainArgs = getopt.gnu_getopt(sys.argv[1:], sOptions)
    except getopt.GetoptError, err:
        print >> sys.stderr, str(err)
        usage(sys.argv[0])

    bVerbose = False

    for opt, arg in optList:
        if opt == "-v":
            bVerbose = True
        else:
            usage(sys.argv[0])


    if len(remainArgs) < 3:
        usage(sys.argv[0])


    # code directory
    sCodeDir = remainArgs[0]
    # which implementation to test (see MultiTester.java for the implementation strings)
    sImpl = remainArgs[1]
    # set of input files that contains the operation commands
    lsInFile = remainArgs[2:]
    
    
    # check implementatoin
    setValidImpl = set(["linkedlist", "sortedlinkedlist", "bst", "hash", "baltree"])
    if sImpl not in setValidImpl:
        print >> sys.stderr, sImpl + " is not a valid implementation name."
        sys.exit(1)


    # compile the skeleton java files
    sCompileCommand = "javac MultisetTester.java Multiset.java LinkedListMultiset.java\
        SortedLinkedListMultiset.java BstMultiset.java HashMultiset.java BalTreeMultiset.java"
    sExec = "MultisetTester"

    # whether executable was compiled and constructed
    bCompiled = False

    sOrigPath = os.getcwd()
    os.chdir(sCodeDir)

    # compile
    proc = sp.Popen([sCompileCommand], shell=True)
    proc.communicate()

    # check if executable was constructed
    if not os.path.isfile(sExec + ".class"):
        print >> sys.stderr, sExec + ".java didn't compile successfully."
    else:
        bCompiled = True


    # variable to store the number of tests passed
    passedNum = 0
    vTestPassed = [False for x in range(len(lsInFile))]
    print ""

    if bCompiled:
        # loop through each input test file
        for (j, sInLoopFile) in enumerate(lsInFile):
            sInFile = os.path.join(sOrigPath, sInLoopFile);
            sTestName = os.path.splitext(os.path.basename(sInFile))[0]
            #sOutputFile = os.path.join(sCodeDir, sTestName + "-" + sImpl + ".out")
            sOutputFile = os.path.join(sTestName + "-" + sImpl + ".out")
            sSearchOutputFile = os.path.join(sTestName + "-" + sImpl + ".search.out")
            sExpectedFile = os.path.splitext(sInFile)[0] + ".exp"
            sSearchExpectedFile = os.path.splitext(sInFile)[0] + ".search.exp"
            
            # check if expected files exist
            if not os.path.isfile(sExpectedFile):
                print >> sys.stderr, sExpectedFile + " is missing."
                continue
            
            if not os.path.isfile(sSearchExpectedFile):
                print>> sys.stderr, sSearchExpectedFile + " is missing."
                continue

            with open(sOutputFile, "w") as fOut:
                #sCommand = os.path.join(sCodeDir, sExec + " " + sImpl)
                sCommand = os.path.join("java " + sExec + " " + sImpl + " " + sSearchOutputFile)
                # following command used by my dummy code to test possible output (don't replace above)
#                 lCommand = os.path.join(sCodeDir, sExec + " " + sExpectedFile + ".test")
                if bVerbose:
                    print "Testing: " + sCommand
                with open(sInFile, "r") as fIn:
                    proc = sp.Popen(sCommand, shell=True, stdin=fIn, stdout=fOut, stderr=sp.PIPE)
                    #proc = sp.Popen(sCommand, shell=True, stdin=sp.PIPE, stdout=sp.PIPE, stderr=sp.PIPE)

                    #(sStdout, sStderr) = proc.communicate("a hello\np\nq")
                    (sStdout, sStderr) = proc.communicate()

                    if sStderr == None:
                        print >> sys.stderr, "Cannot execute " + sInFile
                        print >> sys.stderr, sStderr
                    else:
                        # compare expected with output
                        bPassed = evaluate(sExpectedFile, sOutputFile)
                        bSearchPassed = evaluateSearch(sSearchExpectedFile, sSearchOutputFile)
                        if bPassed and bSearchPassed:
                            passedNum += 1
                            vTestPassed[j] = True
                        else:
                            # print difference if failed
                            if bVerbose:
                                if not bPassed:
                                    print >> sys.stderr, "Difference between output and expected:"
                                    proc = sp.Popen("diff -y " + sOutputFile + " " + sExpectedFile, shell=True)
                                    proc.communicate()
                                    print >> sys.stderr, ""
                                if not bSearchPassed:
                                    print >> sys.stderr, "Difference between search output and expected:"
                                    proc = sp.Popen("diff -y " + sSearchOutputFile + " " + sSearchExpectedFile, shell=True)
                                    proc.communicate()
                                    print >> sys.stderr, ""

    # change back to original path
    os.chdir(sOrigPath)

    print "SUMMARY: " + sExec + " has passed " + str(passedNum) + " out of " + str(len(lsInFile)) + " tests."
    print "PASSED: " + ", ".join([str(x+1) for (x,y) in enumerate(vTestPassed) if y == True]) + "\n"




#######################################################################################################


def evaluate(sExpectedFile, sOutputFile):
    """
    Evaluate if the output is the same as expected input for the print operation.s
    """

    ltExpMatches = []
    ltActMatches = []
    sDelimiter = " | "

    with open(sExpectedFile, "r") as fExpected:
        for sLine in fExpected:
            # space delimiter
            sLine1 = sLine.strip()
            lFields = string.split(sLine1, sDelimiter)
            ltExpMatches.append((lFields[0], int(lFields[1])))


    with open(sOutputFile, "r") as fOut:
        for sLine in fOut:
            # space delimiter
            sLine1 = sLine.strip()

            # if line is empty, we continue (this also takes care of extra newline at end of file)
            if len(sLine1) == 0:
                continue
            # should be space-delimited, but in case submissions use other delimiters
            lFields = re.split("[\t ]*[,|\|]?[\t ]*", sLine1)
            if len(lFields) != 2:
                # less than 2 numbers on line, which is a valid matching if not empty line
                return False
            else:
                try:
                    ltActMatches.append((lFields[0], int(lFields[1])))
                except ValueError:
                    # one or both of the vertices are not integers
                    return False


    setExpMatches = sets.Set(ltExpMatches)
    setActMatches = sets.Set(ltActMatches)

    # if there are differences between the sets
    if len(setExpMatches.symmetric_difference(setActMatches)) > 0:
        return False


    # passed
    return True



def evaluateSearch(sSearchExpectedFile, sSearchOutputFile):
    """
    Evaluate if the output is the same as expected input for searching
    """

    with open(sSearchExpectedFile, "r") as fExpected:
        with open(sSearchOutputFile, "r") as fOut:
            sameParts = set(fExpected).intersection(fOut);

    # all lines should be the same
    # count number of lines in expected file
    lineNum = sum(1 for line in open(sSearchExpectedFile, "r"))

    # if there are differences between the sets
    if len(sameParts) != lineNum:
        return False


    # passed
    return True




def usage(sProg):
    print >> sys.stderr, sProg + "[-v] <code directory> <name of implementation to test> <list of test input files>"
    sys.exit(1)



if __name__ == "__main__":
    main()
