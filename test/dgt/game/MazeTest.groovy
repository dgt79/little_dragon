package dgt.game
//        You are player 1
//        ***********
//        *..1...__.*
//        ....***...F
//        *2........*
//        ***********

class MazeTest extends GroovyTestCase {

    void test_next_move() {
		def maze = new Maze("You are player 1\n***********\n*..1...__.*\n....***...F\n*2........*\n***********")
		println maze

		def where_to = maze.next_move()

		assertEquals 'E', where_to
	}

}
