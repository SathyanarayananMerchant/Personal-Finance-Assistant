# import asyncio

# event_loop=asyncio.new_event_loop()
# event_loop.run_forever()

import random


def fib(n):
    if n==0:
        return 0
    if n==1:
        return 1
    
    return fib(n-1)+fib(n-2)

result=fib(5)
print(result)
    