def customer_info(*args,**kargs):
    print(args)
    print(kargs)
categories=['health','entertainment','other']
info={'name':'rohan','age':20}
customer_info(*categories,**info)