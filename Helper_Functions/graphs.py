class Graph:
    class undirected_graph:
        def __init__(self):
            self.adj_list={}

        def add_node(self, node):
            if node not in self.adj_list:
                self.adj_list[node]=[]
                return True
            return False
    
        def add_edge(self, node1, node2):
            if node1 in self.adj_list and node2 in self.adj_list:
                self.adj_list[node1].append(node2)
                self.adj_list[node2].append(node1)
                return True
            return False
    
        def display(self):
            for node, neighbours in self.adj_list.items():
                print(f"{node}->{neighbours}")

    class Weighted_directed_Graph:
        def __init__(self) -> None:
            self.adj_list={}

        def add_node(self, node, weight=0):
            if node not in self.adj_list:
                self.adj_list[node]=[]
                return True
            return False
        
        def add_edge(self, node1, node2, weight=0):
            if node1 and node2 in self.adj_list:
                self.adj_list[node1].append((node2, weight))
                return True
            return False
        
        def find_path(self, start, end, path=[]):
            path=path+[start]
            if start==end:
                return path
            if start not in self.adj_list:
                return None
            for neighbour, weight in self.adj_list[start]:
                if neighbour not in path:
                    newpath= self.find_path(neighbour, end, path)
                    if newpath:return newpath

            return None
            


        def display(self):
            for node, neighbours in self.adj_list.items():
                connections= ", ".join([f"{neighbour}[{weight}]" for neighbour, weight in neighbours ])
                print(f"{node}->{connections}")
        


# mygraph=Graph.undirected_graph()

# mygraph.add_node('A')
# mygraph.add_node('B')
# mygraph.add_edge('A','B')

# mygraph.add_node('A')
# mygraph.add_node('C')
# mygraph.add_edge('A','C')

# mygraph.add_node('D')
# mygraph.add_node('B')
# mygraph.add_edge('B','D')

# mygraph.display()

newgraph=Graph.Weighted_directed_Graph()

newgraph.add_node('A')
newgraph.add_node('B')
newgraph.add_edge('A','B',23)
newgraph.add_edge('B','A',12)

newgraph.add_node('C')
newgraph.add_node('D')
newgraph.add_edge('C','D',23)
newgraph.add_edge('D','C',12)

newgraph.add_node('E')
newgraph.add_node('F')
newgraph.add_edge('E','F',23)
newgraph.add_edge('F','E',12)

newgraph.add_node('E')
newgraph.add_node('C')
newgraph.add_edge('E','C',23)
newgraph.add_edge('C','E',12)

newgraph.add_node('A')
newgraph.add_node('D')
newgraph.add_edge('A','D',23)
newgraph.add_edge('D','A',12)

newgraph.display()


path=newgraph.find_path('A','C')
print(path)