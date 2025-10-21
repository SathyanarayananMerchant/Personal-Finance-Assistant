# a=1000
# b=80000
# print(a/b)

# a=0.5
# b=0.25
# c=3.5

# print(round(a))
# print(round(b))
# print(round(c))
# print(a+b)
# print(round(a+b))

# if a+b == 0.75:
#     print(True)
# else:
#     print(False)

# print(round(203.1330945))

from decimal import Decimal

print(Decimal('0.01'))
n=89.90
print(int(n))
rounded='1e-'+ str(int(n))
print(Decimal(rounded))