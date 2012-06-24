#!/usr/bin/env groovy

def lines = [][]
def playerNumber = 0
def firstLine = true

for (line in args[0].split('n')) {
    line = line - '\\'
    if (firstLine) {
        playerNumber = line.getAt(line.length() - 1)
        firstLine = false
    } else {
        //println('add line - ' + line.getChars())
        lines.add(line.getChars())
    }
}

//println lines

def player_x = 0
def player_y = 0

def target_x = 0
def target_y = 0

for (i in 0..<lines.size()) {
    //if (lines[i].contains(playerNumber)) {
        for (j in 0..<lines[i].size()) {
            def ch = lines[i][j]
            //println ch
            if ( ch == playerNumber) {
                //println ('yes ' + i + ' '+ j)

                player_x = i
                player_y = j
            } else  if (ch == 'F') {
                target_x = i
                target_y = j
            }
        }
    //}

}

//println 'player - ' + player_x + ' ' + player_y
//println 'target - ' + target_x + ' ' + target_y



if (player_y < target_y) {
    def cell =  lines[player_x][player_y + 1]
    if (cell == '.' || cell == 'F' ) {
        println 'E'
        return
    } else if (player_x < target_x) {
        cell = lines[player_x + 1][player_y]
        if (cell == '.' || cell == 'F') {
            println 'S'
            return
        }
    } else if (cell == '.' || cell == 'F') {
        println 'N'
        return
    }

} else if (cell == '.' || cell == 'F') {
    println 'E'
    return
}

println 'E'



//#println args[0] 