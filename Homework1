import http.client
import json
import time
import timeit
import sys
import collections
from pygexf.gexf import *


#
# implement your data retrieval code here
#


# complete auto grader functions for Q1.1.b,d
def min_parts():
    """
    Returns an integer value
    """
    return 1100

def lego_sets():
    """
    return a list of lego sets.
    this may be a list of any type of values
    but each value should represent one set

    e.g.,
    biggest_lego_sets = lego_sets()
    print(len(biggest_lego_sets))
    > 280
    e.g., len(my_sets)
    """
    return data1['results']

def gexf_graph():
    """
    return the completed Gexf graph object
    """
    # you must replace these lines and supply your own graph
    return graph

# complete auto-grader functions for Q1.2.d

def avg_node_degree():
    """
    hardcode and return the average node degree
    (run the function called “Average Degree”) within Gephi
    """
    # you must replace this value with the avg node degree
    return 5.425

def graph_diameter():
    """
    hardcode and return the diameter of the graph
    (run the function called “Network Diameter”) within Gephi
    """
    # you must replace this value with the graph diameter
    return 8

def avg_path_length():
    """
    hardcode and return the average path length
    (run the function called “Avg. Path Length”) within Gephi
    :return:
    """
    # you must replace this value with the avg path length
    return 4.564245211443109



if __name__ == "__main__":
    start = time.clock()
    
    api_token = sys.argv[1]
    conn = http.client.HTTPConnection("www.rebrickable.com")
    min_parts_val=min_parts()
    curl="https://rebrickable.com/api/v3/lego/sets/?key="+str(api_token)+"&page=1&page_size=500&ordering=-num_parts%2Cid&min_parts="+str(min_parts_val)
    conn.request("GET",curl)
    response=conn.getresponse()
    data1=json.load(response)
 
    sets=[]
    
    for i in range(0,len(data1['results'])):
        set_num=data1['results'][i]['set_num']
        set_url="https://rebrickable.com/api/v3/lego/sets/"+str(set_num)+"/parts/?key="+str(api_token)+"&page_size=1000"
        conn1 = http.client.HTTPConnection("www.rebrickable.com")
        conn1.request("GET",set_url)
        response=conn1.getresponse()
        data=json.load(response)
        parts=[]
        for i in range(0,len(data['results'])):
            dict={}
            dict['part_color']=data['results'][i]['color']['rgb']
            dict['part_quantity']=data['results'][i]['quantity']
            dict['part_name']=data['results'][i]['part']['name']
            dict['part_number']=data['results'][i]['part']['part_num']
            dict['part_id']=str(dict['part_number'])+'_'+str(dict['part_color'])
            parts.append(dict)

        if len(parts)>=20:
            new_parts = sorted(parts,key = lambda e:e.__getitem__('part_quantity'))[-21:-1]
        else:
            new_parts=sorted(parts,key = lambda e:e.__getitem__('part_quantity'))
        sets.append(new_parts)

    my_gexf = Gexf("Danrong Zhang", "bricks_graph")
    graph=my_gexf.addGraph("undirected", "static", "bricks_graph")
    
    atr1=graph.addNodeAttribute('set',type='boolean',defaultValue='false')
    atr2=graph.addNodeAttribute('part',type='boolean',defaultValue='false')

    for i in range(0,len(data1['results'])):
        tmp1= graph.addNode(data1['results'][i]['set_num'],data1['results'][i]['name'],r="0",g="0",b="0")
        tmp1.addAttribute(atr1,'true')
        for j in range(0,len(sets[i])):
            color_16=sets[i][j]['part_color']
            color_r=str(int(color_16[0:2],16))
            color_g=str(int(color_16[2:4],16))
            color_b=str(int(color_16[4:6],16))
            tmp= graph.addNode(sets[i][j]['part_id'],sets[i][j]['part_name'],r=color_r,g=color_g,b=color_b)
            tmp.addAttribute(atr2,'true')
            graph.addEdge(data1['results'][i]['set_num']+"_"+sets[i][j]['part_id'],data1['results'][i]['set_num'],sets[i][j]['part_id'],weight=sets[i][j]['part_quantity'])
            
    output_file=open("bricks_graph.gexf","wb+")
    my_gexf.write(output_file)
    elapsed = (time.clock() - start)
    print(elapsed)
    
