package dgt.game

import dgt.graph.Graph
import dgt.graph.Edge

//        You are player 1
//        ***********
//        *..1...__.*
//        ....***...F
//        *2........*
//        ***********
class Maze {
	private int player_no
	private int opponent_no
	private Position my_position
	private Position opponent_position
	private Position exit_position
	private Graph graph

    Maze(String maze) {
		def lines = maze.tokenize("\n")
		def header = lines[0]
		player_no = header.find(~/\d/)

		lines = lines - lines[0]
		def graph_input = []

		lines.eachWithIndex { item, i ->
			item.chars.eachWithIndex { c, j ->
				if (c == '*') {
					return
				} else if (c.isDigit()) {
					if ((c as int) == player_no) {
						my_position = new Position(x: i, y: j)
						update_graph_input(graph_input, lines, i, j)
					} else {
						opponent_no = c as int
						opponent_position = new Position(x: i, y: j)
					}
				} else if (c == 'F') {
					exit_position = new Position(x: i, y: j)
					update_graph_input(graph_input, lines, i, j)

				} else if (c == '.' || c == '_') {
					update_graph_input(graph_input, lines, i, j)
				}
			}
		}

		graph_input.each {
			println it
		}

		graph = Graph.load_from_list graph_input
    }

	def update_graph_input(graph_input, lines, i, j) {
		def node = "[$i,$j]"
		def edges = find_edges(lines, i, j)
		edges.each {
			graph_input.add("$node; $it.node; $it.weight")
		}
	}

	/*
					North
		West	current_char	Est
					South
	 */
	// TODO finds 2 as valid edge
	def find_edges(lines, i, j) {
		def edges = []
		use(WeightCategory) {
			def should_check_west = j > 0
			if (should_check_west) {
				def west = lines[i].charAt(j - 1)
				if (west != '*') edges.add(new Edge("[$i,${j - 1}]", west.weight))
			}

			def should_check_est = j < lines[i].chars.size() - 1
			if (should_check_est) {
				def est = lines[i].charAt(j + 1)
				if (est != '*') edges.add(new Edge("[$i,${j + 1}]", est.weight))
			}

			def should_check_north = i > 0
			if (should_check_north) {
				def north = lines[i - 1].charAt(j)
				if (north != '*') edges.add(new Edge("[${i - 1},$j]", north.weight))
			}

			def should_check_south = i < lines.size() - 1
			if (should_check_south) {
				def south = lines[i + 1].charAt(j)
				if (south != '*') edges.add(new Edge("[${i + 1},$j]", south.weight))
			}
		}

		return edges
	}

    def next_move() {
       ''
    }

	@Override
	String toString() {
		return "Maze:\n\tplayer - $player_no\n\tposition - $my_position\n\texit - $exit_position\n\t$graph"
	}
}

class WeightCategory {
	static int getWeight(Character self) {
		switch (self) {
			case '.': return 10
			case '_': return 0
			default: return 10
		}
	}
}

class Position {
	def x = 0
	def y = 0

	@Override
	String toString() {
		return "$x, $y"
	}
}

