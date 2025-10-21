def count_up_to(n):
    i=1
    while i<=n:
        yield i
        i+=1

g = count_up_to(5)
print(next(g))
print(next(g))



# list=[2,6,822,348]

# for i in list:
#     print(i)

    