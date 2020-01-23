from util import entropy, information_gain, partition_classes
import numpy as np 
import ast

class DecisionTree(object):

    def __init__(self):
        # Initializing the tree as an empty dictionary or list, as preferred
        #self.tree = []
        self.tree = {}
        #pass

    def learn(self, X, y):
        # TODO: Train the decision tree (self.tree) using the the sample X and labels y
        # You will have to make use of the functions in utils.py to train the tree
        
        # One possible way of implementing the tree:
        #    Each node in self.tree could be in the form of a dictionary:
        #       https://docs.python.org/2/library/stdtypes.html#mapping-types-dict
        #    For example, a non-leaf node with two children can have a 'left' key and  a 
        #    'right' key. You can add more keys which might help in classification
        #    (eg. split attribute and split value)
        #pass

        max_info_gain, max_attribute, max_value = -1, 0, 0

        X_left = []
        X_right = []
        y_left = []
        y_right = []
        
        self.tree_depth = 0
        self.id = None

        if self.tree_depth > 10 or entropy(y) <= 0:
            self.id = y[0]
            return            
                        
        for i in range(0, len(X[0])):
            values = [X[j][i] for j in range(0, len(X))]
            # choose the split value according to its average in order to reduce the running time
            if isinstance(values[0], str):
                split_avg = values[0]
            else:                   
                split_avg = sum(values) / len(values)
                
            xLeft, xRight, yLeft, yRight = partition_classes(X, y, i, split_avg)
            current = []
            current.append(yLeft)
            current.append(yRight)
            temp = information_gain(y, current)
            if temp > max_info_gain:
                max_attribute = i
                max_value = split_avg
                max_info_gain = temp
                X_left = xLeft
                X_right = xRight
                y_left = yLeft
                y_right = yRight
        
        #build tree
        self.tree['max_attribute'], self.tree['max_value'] = max_attribute, max_value        
        self.tree['L'], self.tree['R'] = DecisionTree(), DecisionTree()
        
        #grow tree
        self.tree['L'].learn(X_left, y_left)  
        self.tree['R'].learn(X_right, y_right)
        self.tree['L'].tree_depth = self.tree_depth + 1
        self.tree['R'].tree_depth = self.tree_depth + 1




    def classify(self, record):
        # TODO: classify the record using self.tree and return the predicted label
        #pass
        curr_tree = self.tree
        if self.id != None:
            return self.id
        while self.id is None:
            value = curr_tree['max_value']
            attribute = curr_tree['max_attribute']

            if isinstance(record[attribute], str):
                if record[attribute] == value:
                    predict = curr_tree['L'].classify(record)
                else:
                    predict = curr_tree['R'].classify(record)
            else:
                if record[attribute] > value:
                    predict = curr_tree['R'].classify(record)
                else:
                    predict = curr_tree['L'].classify(record)
            return predict


