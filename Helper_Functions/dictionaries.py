d={"a":[1]}
d["b"]=[2]
d["a"].append(3)
d["a"].append("apple")
d["a"].append((12,10))

list=[]
list=list+['d']
print(list)

print(d.items())
test= 'a' in d
print(test)

# list=[1,2,3,"apple"]
# print(list)