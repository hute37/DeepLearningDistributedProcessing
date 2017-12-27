def createGenerator():
    mylist = [1,2,3]
    for i in mylist:
        yield i * i

mygenerator = createGenerator()
print(mygenerator)

for i in mygenerator:
    print(i)