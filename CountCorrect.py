import sys
def count(path):
    fileIn = open(path.replace('\n',''))
    numCorrect = 0
    correct = 0
    for line in fileIn:
        if line[0] == 'u':
            correct = correct + 1
        else:
            num1 = line[:2]
            if (num1 == "1.") and (line[3] == str(correct)) and (line[4] == ' '):
                numCorrect = numCorrect + 1
    fileIn.close()
    return numCorrect
    
# Character NGrams|N:2    : featureNum = 0
# Character NGrams|N:3    : featureNum = 1
# Word NGrams|N:2         : featureNum = 2
# Word NGrams|N:3         : featureNum = 3
# POS NGrams|N:2          : featureNum = 4
# POS NGrams|N:2          : featureNum = 5
# MW Function Words       : featureNum = 6
# Sentence Length         : featureNum = 7
# Word Lengths            : featureNum = 8
# Words                   : featureNum = 9
def main():
    catNum = sys.argv[1]
    authNum = sys.argv[2]
    trainNum = sys.argv[3]
    testNum = sys.argv[4]
    featureNum = 0
    for line in sys.stdin:
        print catNum+authNum+trainNum+testNum+str(featureNum)+":"+str(count(line))
        if featureNum == 9:
            featureNum = 0
        else:
            featureNum = featureNum + 1
main()
