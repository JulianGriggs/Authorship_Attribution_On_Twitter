import sys
def main():
    categories = ["Business", "Entertainment", "Music", "Sports", "Television"];
    index = sys.argv[1]
    print categories[int(index)]
    
main()