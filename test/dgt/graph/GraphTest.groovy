package dgt.graph

class GraphTest extends GroovyTestCase {
	void test_read_graph() {
		def expected_graph = '''[1:node '1' 
\tedges=[edge (node = 2, weight = 0), edge (node = 5, weight = 0)]
, 2:node '2' 
\tedges=[edge (node = 3, weight = 0), edge (node = 1, weight = 0), edge (node = 5, weight = 0), edge (node = 4, weight = 0)]
, 3:node '3' 
\tedges=[edge (node = 2, weight = 0), edge (node = 4, weight = 0)]
, 4:node '4' 
\tedges=[edge (node = 3, weight = 0), edge (node = 2, weight = 0), edge (node = 5, weight = 0)]
, 5:node '5' 
\tedges=[edge (node = 2, weight = 0), edge (node = 1, weight = 0), edge (node = 4, weight = 0)]
]'''
		def graph = Graph.load_from_file('resources/graph.txt')
		print graph
		assertEquals(expected_graph, graph.toString())
	}

	void test_remove() {
		def graph = Graph.load_from_file('resources/graph.txt')
		graph.remove('1'..'5')
		assertTrue graph.nodes.isEmpty()
	}

	void test_find_path() {
		def graph = Graph.load_from_file('resources/maze.txt')
		def path = graph.find_path('[1,3]', '[2,10]')

		def path_summary = []
		path.each {node ->
			path_summary << node.data
		}

		assertEquals(['[1,3]', '[1,4]', '[1,5]', '[1,6]', '[1,7]', '[1,8]', '[1,9]', '[2,9]', '[2,10]'], path_summary)
	}
}
