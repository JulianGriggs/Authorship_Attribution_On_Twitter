import sys
def main():
    path = sys.argv[1]

    for line in sys.stdin:
        if line[0] == 'u':
            print ',' + path.replace('\n', '') + '/' + line.replace('\n', '') + ',' + line.replace('\n', '')
        else:
            id = line[:2]
            if (id.isdigit()):
                print str(id) + ',' + path.replace('\n', '') + '/' + line.replace('\n', '') + ',' + line.replace('\n', '')
            else:
                id = line[0]
                print str(id) + ',' + path.replace('\n', '') + '/' + line.replace('\n', '') + ',' + line.replace('\n', '')
    
main()
