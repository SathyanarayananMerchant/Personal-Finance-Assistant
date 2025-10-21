class query:
    def __init__(self) -> None:
        self.type=None
        self.columns=[]
        self.table=None
        self.where=[]
        self.params=[]

class conditions:
    def __init__(self,column,operator,value) -> None:
        self.column=column
        self.operator=operator
        self.value=value



