package dgt.graph

class Graph {
	def nodes = [:]

	static Graph load_from_file(file) {
		if (file instanceof String) {
			def input_file = new File(file)
			return load(input_file)
		} else if (file instanceof File) {
			return load(file)
		}
	}

	private static Graph load(File file) {
		def graph = new Graph()
		file.eachLine { line ->
			load_line(graph, line)
		}
		graph
	}

	static Graph load_from_list(List list) {
		def graph = new Graph()
		list.each { line ->
			load_line(graph, line)
		}
		graph
	}

	private static void load_line(graph, line) {
		def tokens = line.replaceAll(' ', '').split(';')
		def key = tokens[0]

		if (graph.get(key) != null) {
			def node = graph.get(key)
			def edge = new Edge(tokens[1], tokens[2])
			node.edges.add(edge)
		} else {
			def node = new Node(key)
			def edge = new Edge(tokens[1], tokens[2])
			node.edges.add(edge)
			graph.add(tokens[0], node)
		}
	}

	void add(key, Node node) {
		nodes[key] = node
	}

	Node get(key) {
		nodes[key]
	}

	void remove(keys) {
		keys.each { key ->
			Node node = get(key)
			if (node == null) {
				println "could not find node for key ${key}"
			}
			node.edges.each {
				get(it.node).edges.remove(new Edge(key))
			}
			println "removing node ${key}"
			nodes.remove(key)
		}
	}

	@Override
	String toString() {
		nodes.toString()
 	}

	// a_star algorithm
	def find_path(from, to) {
		def open_list = new TreeSet<Node>(Node.ordered_by_total_cost)
		def closed_list = []

		def from_node = from instanceof Node ?: nodes[from]
		def to_node = to instanceof Node ?: nodes[to]

		open_list << from_node

		while (open_list ? true : false) {
			def node = open_list.first()
			open_list.remove node
			closed_list << node

			if (node.data == to_node.data) {
				// we're done, we've found the path
				def path = []
				def i = node
				while (i.parent != null) {
					path << i
					i = i.parent
				}
				path << i
				return path.reverse()
			}

			node.edges.each { edge ->
				def linked_node = nodes[edge.node]
				if (closed_list.contains(linked_node)) {
					return
				} else {
					if (open_list.contains(linked_node)) {
						if (linked_node.cost > node.cost + edge.weight) {
							linked_node.parent = node
							linked_node.cost = node.cost + edge.weight
						}
					} else {
						linked_node.parent = node
						linked_node.cost = node.cost + edge.weight
						linked_node.heuristic_cost = manhattan_estimate(linked_node, to_node)		// should multiply by weight
						open_list << linked_node
					}
				}
			}
		}

		return []
	}

	// TODO - refactor, as it's not readable
	def manhattan_estimate(from, to) {
		// from [1,8] -> to [2,10]
		def from_coords = (from.data - '[' - ']').split(',')
		def to_coords = (to.data - '[' - ']').split(',')

		int x = (from_coords[0] as int) - (to_coords[0] as int)
		int y = (from_coords[1] as int) - (to_coords[1] as int)

		return x.abs() + y.abs()
	}
}


// TODO Node -> GridNode
class Node {
	def data
	def parent
	Set edges = []

	def cost
	def heuristic_cost

	final static ordered_by_total_cost = [
			compare: {node_1, node_2 ->
				int result = (node_1.cost + node_1.heuristic_cost) <=> (node_2.cost + node_2.heuristic_cost)
				if (result == 0) {
					result = node_1.data <=> node_2.data
				}
				result
			}
	] as Comparator

	Node(data) {
		this.data = data
	}

	@Override
	String toString() {
		"node '${data.toString()}' \n\tedges=${edges}\n"
	}
}

class Edge {
	def node
	def weight

	Edge(node, weight = 0) {
		this.node = node
		this.weight = weight
	}

	@Override
	String toString() {
		"edge (node = ${node}, weight = ${weight})"
	}

	boolean equals(o) {
		if (this.is(o)) return true
		if (getClass() != o.class) return false
		if (node != o.node) return false
		return true
	}

	int hashCode() {
		return (node != null ? node.hashCode() : 0)
	}
}