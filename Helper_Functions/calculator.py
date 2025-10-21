from decimal import Decimal, InvalidOperation, ROUND_HALF_UP
from math import pow

class calculator:
    def string_to_decimal(self, input:str) -> Decimal | None:
        try:
            return Decimal(input)
        except InvalidOperation:
            print(f"Error: {input} is not a number")
            return None
        
    def add(self, a:str,b:str) -> Decimal | None:
        num1=self.string_to_decimal(a)
        num2=self.string_to_decimal(b)
        sum=0
        if num1 and num2 is not None:
            sum=num1+num2
            return sum
        else:
            print("Error: one or both of the numbers are invalid")
            return None
        
    def subtract(self, a:str, b:str) -> Decimal | None:
        num1=self.string_to_decimal(a)
        num2=self.string_to_decimal(b)

        diff=0
        
        if num1 and num2 is not None:
            diff=num1-num2
            return diff
        else:
            print("Error: one or both of the numbers are invalid")
            return None
        
    def multiply(self, a:str, b:str) -> Decimal | None:
        num1=self.string_to_decimal(a)
        num2=self.string_to_decimal(b)
        product=0

        if num1 and num2 is not None:
            product=num1*num2
            return product
        else:
            print("Error: one or both of the numbers are invalid")
            return None
        
    def divide(self, a:str, b:str) -> Decimal | None:
        num1=self.string_to_decimal(a)
        num2=self.string_to_decimal(b)
        
        quo=0

        if num1 and num2 is not None:
            quo=num1/num2
            return quo
        else:
            print("Error: one or both of the numbers are invalid")
            return None
        
    def roundoff(self, a: str, b: str) -> Decimal | None:
        num = self.string_to_decimal(a)
        n_dig = self.string_to_decimal(b)

        if num is None:
            print("Error: invalid number input")
            return None

        if n_dig is not None:
            try:
                # create Decimal quantize pattern: b=2 -> Decimal('0.01')
                quant = Decimal("1").scaleb(-int(n_dig))
            except Exception as e:
                print("Error:", e)
                return None
        else:
            quant = Decimal("0.01")  # default 2 decimal places

        return num.quantize(quant, rounding=ROUND_HALF_UP)


a="19.8932475"
b="183.239847"

calc=calculator()
num=calc.add(a,b)
result=calc.roundoff(str(num), str(5))
print(result)



        










