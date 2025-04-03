#Text Based Uno Game
import random

discard = []
playerTurn = 0
playDirn = 1 # or -1 if they hit the ol' reverse
nameDict = {}
GameOver = False
colors = ["Red", "Yellow", "Green", "Blue"]

'''
Uno Deck: 
Each of the 4 colors has one '0', and two of the numbers '1 through 9'
They also each have two "Skips", two "Draw Two's", and two "Reverses"
The deck also includes four Wild Cards and four "+4s"
'''

def makeDeck():
    global deck
    global colors

    deck = []
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
    global currColor
    discardCard = topDiscard.split()  # splits the string of the top discard card into color / number list
    if "Wild Card" in playerHand or "Draw Four" in playerHand:
        #print("Yippee!")
        return True    # if player has a wild card, automatically return true

    for card in playerHand:
        PHlist = card.split()      # for all of the player's cards, split into color / number
        if PHlist[0] in discardCard or PHlist[0] == currColor:   # and if either of their properties are in the discard list, return true
            return True   # also returns true if at least one card's color matches the chosen wild color
        elif PHlist[1] in discardCard:
            return True
    
    return False

'''
Special Cards function:
Applies all special effects for a given card
Function mainly exists to make the game loop cleaner
'''
def specialCards(currCard):
    global playerTurn
    global currColor
    global playDirn
    global hands
    global numPlayers

    if currCard == "Wild Card":
        print("1) Red\n2) Yellow\n3) Green\n4) Blue")   # if played card is a wild card, ask for color input
        
        newColor = int(input("What color do you choose? "))
        while newColor < 1 or newColor > 4:
            newColor = int(input("Invalid option. What color do you choose? "))  # authentication
        currColor = colors[newColor - 1]
        print("Chosen Color: "+currColor)

    elif currCard == "Draw Four":
        print("1) Red\n2) Yellow\n3) Green\n4) Blue")  # same as wild card 
            
        newColor = int(input("What color do you choose? "))
        while newColor < 1 or newColor > 4:
            newColor = int(input("Invalid option. What color do you choose? "))  # I'll prob make this a method
        currColor = colors[newColor - 1]
        print("Chosen Color: "+currColor)
        
        playerDraw = playerTurn + playDirn
        if playerDraw >= numPlayers:
            playerDraw = 0   # copy and pasted from the Draw 2 one -> will make it a method
        elif playerDraw < 0:
            playerDraw = numPlayers - 1
        #print(nameDict[playerDraw])
        hands[playerDraw].extend(drawCards(4))
        
    if "Reverse" in currCard:  # if the current card has a reverse, switch play direction
        playDirn *= -1
        
    elif "Skip" in currCard:
        playerTurn += playDirn  # goes one extra in the current direction (skips next player)
        if playerTurn >= numPlayers:   # if last player's turn done, loop back 
            playerTurn = 0
        elif playerTurn < 0:   # same thing but in opposite direction
            playerTurn = numPlayers - 1

    elif "Draw Two" in currCard:
        playerDraw = playerTurn + playDirn
        if playerDraw >= numPlayers:   # chooses person to give cards according to play direction
            playerDraw = 0
        elif playerDraw < 0:
            playerDraw = numPlayers - 1
        hands[playerDraw].extend(drawCards(2))  # gives next player two cards


UnoDeck = makeDeck()
hands = [] # all players' hands
numPlayers = int(input("How many people are playing? ")) 
while numPlayers < 2 or numPlayers > 4:
    numPlayers = int(input("Please enter a number from 2 to 4: ")) # basic verification for now, will change later

for i in range(numPlayers):
    hands.append(drawCards(7))   # adds list of cards (each person's hand) to hands list
    nameDict[i] = input("What is player "+str(i + 1)+"'s name? ")  # adds players to dictionary to store their names

currCard = UnoDeck.pop(0)
discard.append(currCard)
currColor = discard[0].split()[0]  # gets the discard pile's color
#print(currColor)

specialCards(currCard)

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

        #check if player has won yet
        if len(hands[playerTurn]) == 0:
            GameOver = True
		
		#special cards
        specialCards(currCard)

    else:
        input("You have no cards to play. Draw 1")
        hands[playerTurn].extend(drawCards(1))  # Adds +1 card to hand (merges the two lists)

    playerTurn += playDirn     # increments play cycle
    if playerTurn >= numPlayers:   # if last player's turn done, loop back 
        playerTurn = 0
    elif playerTurn < 0:   # same thing but in opposite direction
        playerTurn = numPlayers - 1


print("\nGame Over")
print(nameDict[playerTurn]+" is the winner!")

#Problem List:
# - If there's a special card at the beginning it doesn't work