# multiAgents.py
# --------------
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


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent
from pacman import GameState

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState: GameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState: GameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]
        # print(newGhostStates)

        score = 0
        distances = [manhattanDistance(newPos, food) for food in newFood.asList()]
        if distances:
            closest = min(distances)
            score += 1 / closest
        for ghost_state, scared_time in zip(newGhostStates, newScaredTimes):
            ghostPos = ghost_state.getPosition()
            ghostDistance = manhattanDistance(newPos, ghostPos)
            if ghostDistance > 0: 
                # when ghosts are scared, reward smaller distance more
                if scared_time > 0: 
                    score += 10 / ghostDistance
                else:
                    score -= 2 / ghostDistance   
            else:
                score -= 500  

        return successorGameState.getScore() + score

def scoreEvaluationFunction(currentGameState: GameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        return self.findValue(gameState, 0, 0)[1]

    # find minimax action for current gamestate: 
    def findValue(self, gameState, index, depth):
        if depth == self.depth or len(gameState.getLegalActions(index)) == 0:
            return self.evaluationFunction(gameState), None
        if index == 0:
            return self.maxValue(gameState, index, depth)
        else:
            return self.minValue(gameState, index, depth)

    # for pacman: 
    def maxValue(self, gameState, index, depth):
        maxScore = float('-inf')
        nextAction = None
        legalActions = gameState.getLegalActions(index)
        for action in legalActions:
            successorState = gameState.generateSuccessor(index, action)
            score = self.findValue(successorState, index + 1, depth)[0]
            if score > maxScore:
                maxScore, nextAction = score, action
        return maxScore, nextAction

    # for ghost: 
    def minValue(self, gameState, index, depth):
        minScore = float('inf')
        nextAction = None
        legalActions = gameState.getLegalActions(index)
        for action in legalActions:
            successorState = gameState.generateSuccessor(index, action)
            if index == gameState.getNumAgents() - 1:
                # if last ghost's turn, increase depth
                score = self.findValue(successorState, 0, depth + 1)[0] 
            else:
                # if not, increase index to move to next ghost
                score = self.findValue(successorState, index + 1, depth)[0] 
            if score < minScore:
                minScore, nextAction = score, action
        return minScore, nextAction

class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"
        util.raiseNotDefined()

    

class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        alpha = float('-inf')
        beta = float('inf')
        nextAction = None
        bestScore = float('-inf')

        for action in gameState.getLegalActions(0):
            successorState = gameState.generateSuccessor(0, action)
            score = self.minValue(successorState, 1, 0, alpha, beta)[0]
            if score > bestScore:
                bestScore, nextAction = score, action
            alpha = max(alpha, bestScore)

        return nextAction
    
    def maxValue(self, gameState, index, depth, alpha, beta):
        if depth == self.depth or gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState), None

        v = float('-inf')
        legalActions = gameState.getLegalActions(index)
        nextAction = None

        for action in legalActions:
            successorState = gameState.generateSuccessor(index, action)
            score = self.minValue(successorState, 1, depth, alpha, beta)[0]
            if score > v:
                v, nextAction = score, action
            if v > beta:
                return v, nextAction
            alpha = max(alpha, v)

        return v, nextAction
    
    def minValue(self, gameState, index, depth, alpha, beta):
        if depth == self.depth or gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState), None

        v = float('inf')
        legalActions = gameState.getLegalActions(index)
        nextAction = None

        for action in legalActions:
            successorState = gameState.generateSuccessor(index, action)
            if index == gameState.getNumAgents() - 1:
                # If this is the last ghost, move to Pacman and increase depth
                score = self.maxValue(successorState, 0, depth + 1, alpha, beta)[0]
            else:
                # Otherwise, move to the next ghost
                score = self.minValue(successorState, index + 1, depth, alpha, beta)[0]

            if score < v:
                v, nextAction = score, action
            if v < alpha:
                return v, nextAction
            beta = min(beta, v)

        return v, nextAction

def betterEvaluationFunction(currentGameState: GameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION: <write something here so we know what you did>
    """
    "*** YOUR CODE HERE ***"
    util.raiseNotDefined()

# Abbreviation
better = betterEvaluationFunction
