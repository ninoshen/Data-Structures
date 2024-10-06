# search.py
# ---------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util
from game import Directions
from typing import List

class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()




def tinyMazeSearch(problem: SearchProblem) -> List[Directions]:
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]

def depthFirstSearch(problem: SearchProblem) -> List[Directions]:
    """
    Search the deepest nodes in the search tree first.

    Your search algorithm needs to return a list of actions that reaches the
    goal. Make sure to implement a graph search algorithm.

    To get started, you might want to try some of these simple commands to
    understand the search problem that is being passed in:

    print("Start:", problem.getStartState())
    print("Is the start a goal?", problem.isGoalState(problem.getStartState()))
    print("Start's successors:", problem.getSuccessors(problem.getStartState()))
    """
    stored = util.Stack()
    start = problem.getStartState()
    stored.push((start, [], 0))  # (state, path, cost)
    visited = []

    while not stored.isEmpty():
        current, path, costs = stored.pop()
        if problem.isGoalState(current):
            return path
        if current not in visited:
            visited.append(current)
            for succ, action, cost in problem.getSuccessors(current):
                if succ not in visited:
                    stored.push((succ, path + [action], costs + cost))

    return []

    # util.raiseNotDefined()

def breadthFirstSearch(problem: SearchProblem) -> List[Directions]:
    """Search the shallowest nodes in the search tree first."""
    stored = util.Queue()
    listed_states = []
    stored.push((problem.getStartState(), [], 0))
    
    while not stored.isEmpty():
        current_state, moves, total_cost = stored.pop()
        listed_states.append(current_state)
        
        if problem.isGoalState(current_state):
            return moves
        
        for location, move, cost in problem.getSuccessors(current_state):
            if location not in listed_states:
                temp_list = (location, moves + [move], total_cost + cost)
                stored.push(temp_list)
                listed_states.append(location)

    return []

    # util.raiseNotDefined()

def uniformCostSearch(problem: SearchProblem) -> List[Directions]:
    """Search the node of least total cost first."""
    stored = util.PriorityQueue()
    listed_states = []
    stored.push((problem.getStartState(), [], 0), 0)

    while not stored.isEmpty():
        current_state, moves, total_cost = stored.pop()

        if current_state in listed_states:
            continue

        listed_states.append(current_state)

        if problem.isGoalState(current_state):
            return moves

        for location, move, cost in problem.getSuccessors(current_state):
            if location not in listed_states:
                new_cost = total_cost + cost
                temp_list = (location, moves + [move], new_cost)
                stored.push(temp_list, new_cost)

    return []

    #util.raiseNotDefined()

def nullHeuristic(state, problem=None) -> float:
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0

def aStarSearch(problem: SearchProblem, heuristic=nullHeuristic) -> List[Directions]:
    """Search the node that has the lowest combined cost and heuristic first."""

    stored = util.PriorityQueue()
    start = problem.getStartState()
    stored.push((start, [], 0), heuristic(start, problem))
    visited = {}
    visited[start] = 0

    while not stored.isEmpty():
        current, path, stepcost = stored.pop()
        if problem.isGoalState(current):
            return path
        for state, actions, cost in problem.getSuccessors(current):
            total_cost = stepcost + cost
            if state not in visited or total_cost < visited[state]:
                visited[state] = total_cost
                fn = total_cost + heuristic(state, problem)
                stored.push((state, path + [actions], total_cost), fn)
    return []

    # util.raiseNotDefined()

# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
