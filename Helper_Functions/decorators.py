def my_decorator(func):
    def wrapper(*args,**kwargs):
        print("Before calling the function")
        result=func(*args,**kwargs)
        print("After calling the functioni")
        return result
    
    return wrapper

@my_decorator
def say_hello(name):
    print(f"Hello, {name}!")


say_hello("Sathya")
