#Text Based Uno Game
import random

discard = []
playerTurn = 0
playDirn = 1 # or -1 if they hit the ol' reverse
nameDict = {}
GameOver = False

'''
Uno Deck: 
Each of the 4 colors has one '0', and two of the numbers '1 through 9'
They also each have two "Skips", two "Draw Two's", and two "Reverses"
The deck also includes four Wild Cards and four "+4s"
'''

def makeDeck():
    global deck

    deck = []
    colors = ["Red", "Yellow", "Green", "Blue"]
    values = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, "Skip", "Draw Two", "Reverse"]
    wild = ["Wild Card", "Draw Four"]
    #Will create one card for every each possible value for each possible color and add to the list
    for color in colors:
        for value in values:
            cardVal = color+" "+str(value)
            deck.append(cardVal)

    for color in colors:
        for i in range(1, 12):
            cardVal = color+" "+str(values[i])   # adds the second card for each color excluding 0
            deck.append(cardVal)
    
    for i in range(4):
        deck.append(wild[0])  # slaps on 4 of each wild card to the end
        deck.append(wild[1])

    random.shuffle(deck)   #shuffles deck (No shit sherlock) Idk why this comment is even here
    return deck

'''
Draw Card function: 
Paramater of how many cards to draw,
Outputs top card(s) of deck, then updates deck
'''

def drawCards(numCards):
    global UnoDeck   # references the uno deck global variable
    drawnCards = []
    for i in range(numCards):
        drawnCards.append(UnoDeck.pop(i))   # adds top few cards to variable and gets rid of them from the deck
    
    return drawnCards  # returns list

'''
Print Hand function:
takes in the player number and their hand 
prints out their cards
'''

def printHand(playerNum, playerHand):
    cardNum = 1    # assigning numbers to each of the player's card for when they want to play
    
    print("")
    print(nameDict[playerNum]+"'s Cards")  # uses the name dictionary to get the current player's name
    print("- - - - - - - - - -")

    for card in playerHand:    # lists out the players cards (takes up a bit of space but it's fine)
        print(str(cardNum)+") "+card)
        cardNum += 1
    print("")

'''
Can Play function:
takes top discard card (string) and player's hand (list)
returns a boolean based on if they can play or not
'''
def canPlay(topDiscard, playerHand):
    discardCard = topDiscard.split()  # splits the string of the top discard card into color / number list
    if "Wild Card" in playerHand or "Draw Four" in playerHand:
        #print("Yippee!")
        return True    # if player has a wild card, automatically return true

    for card in playerHand:
        PHlist = card.split()      # for all of the player's cards, split into color / number
        if PHlist[0] in discardCard:   # and if either of their properties are in the discard list, return true
            return True
        elif PHlist[1] in discardCard:
            return True
    
    return False


#def specialCards(pickedCard):
    #global playDirn
    
        # give next player +2


UnoDeck = makeDeck()
hands = [] # all players' hands
numPlayers = int(input("How many people are playing? ")) 

while numPlayers < 2 or numPlayers > 4:
    numPlayers = int(input("Please enter a number from 2 to 4: ")) # basic verification for now, will change later

for i in range(numPlayers):
    hands.append(drawCards(7))   # adds list of cards (each person's hand) to hands list
    nameDict[i] = input("What is player "+str(i + 1)+"'s name? ")  # adds players to dictionary to store their names

discard.append(UnoDeck.pop(0))

# Note: There's a chance the discard pile starts with a wild card in which case the game breaks
# Will fix later lol

while GameOver == False:
    
    print("\nDiscard Pile: "+discard[-1])   # gets the last card added to the discard pile!
    printHand(playerTurn, hands[playerTurn])

    if canPlay(discard[-1], hands[playerTurn]):  # if player can play, ask them what card to put
        cardChosen = int(input("What card would you like to play? "))
        while not canPlay(discard[-1], [hands[playerTurn][cardChosen - 1]]):  # while they offer a card that can't be played,
            print("You can't play that card")       # repeat question
            cardChosen = int(input("What card would you like to play? "))
        currCard = hands[playerTurn].pop(cardChosen - 1)  # card that was just played (popped from player hand)
        discard.append(currCard)   # adds card to top of discard pile
    
    else:
        print("You have no cards to play. Draw 1")
        hands[playerTurn].extend(drawCards(1))  # Adds +1 card to hand (merges the two lists)


    print(currCard)
    if "Reverse" in currCard:
        playDirn *= -1

    elif currCard == "Wild Card":
        print("1) Red\n2) Yellow\n3) Green\n4) Blue")
        newColor = int(input("What color do you choose? "))

    elif currCard == "Draw Four":
        print("1) Red\n2) Yellow\n3) Green\n4) Blue")
        newColor = int(input("What color do you choose"))
        # give next player +4

    elif "Draw Two" in currCard:
        print("smth")


    playerTurn += playDirn
    if playerTurn == numPlayers:
        playerTurn = 0
    elif playerTurn < 0:
        playerTurn = numPlayers - 1

    #GameOver = True   # so that it won't infinite loop