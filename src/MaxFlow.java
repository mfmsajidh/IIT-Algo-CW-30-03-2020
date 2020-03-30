import java.lang.*;
import java.util.LinkedList;

public class MaxFlow {

    static final int VERTICES = 6; // Number of vertices in graph

    public static void main(String[] args) {

        // Creates a graph (Adjacency matrix)
        int[][] graph = new int[][]{
                {0, 10, 8, 0, 0, 0},
                {0, 0, 5, 5, 0, 0},
                {0, 4, 0, 0, 10, 0},
                {0, 0, 7, 0, 6, 3},
                {0, 0, 0, 10, 0, 14},
                {0, 0, 0, 0, 0, 0}
        };

        int sourceNode = 0;
        int sinkNode = VERTICES-1;

        MaxFlow maxFlow = new MaxFlow();
        Stopwatch stopwatch = new Stopwatch();

        System.out.println("The maximum possible flow is " + maxFlow.fordFulkerson(graph, sourceNode, sinkNode));
        System.out.println("Time taken to complete in seconds: " + stopwatch.elapsedTime());

    }

    /**
     * Returns tne maximum flow from s to t in the given graph
     * @param graph initial graph with capacities
     * @param s source node
     * @param t sink node
     * @return maximum flow according to Ford-Fulkerson
     */
    int fordFulkerson(int[][] graph, int s, int t)
    {
        int row, column;

        // Create a residual graph and fill the residual graph
        // with given capacities in the original graph as
        // residual capacities in residual graph

        // Residual graph where rGraph[i][j] indicates
        // residual capacity of edge from i to j (if there
        // is an edge. If rGraph[i][j] is 0, then there is
        // not)
        int[][] rGraph = new int[VERTICES][VERTICES];

        for (row = 0; row < VERTICES; row++)
            for (column = 0; column < VERTICES; column++)
                rGraph[row][column] = graph[row][column];

        // This array is filled by BFS and to store path
        int[] parent = new int[VERTICES];

        int max_flow = 0; // There is no flow initially

        // Augment the flow while there is path from source to sink
        while (breadthFirstSearch(rGraph, s, t, parent)) {
            // Find minimum residual capacity of the edhes
            // along the path filled by BFS. Or we can say
            // find the maximum flow through the path found.
            int path_flow = Integer.MAX_VALUE;
            for (column=t; column!=s; column=parent[column]) {
                row = parent[column];
                path_flow = Math.min(path_flow, rGraph[row][column]);
            }

            // update residual capacities of the edges and reverse edges along the path
            for (column=t; column != s; column=parent[column]) {
                row = parent[column];
                rGraph[row][column] -= path_flow;
                rGraph[column][row] += path_flow;
            }

            // Add path flow to overall flow
            max_flow += path_flow;
        }

        // Return the overall flow
        return max_flow;
    }

    /**
     *  Returns true if there is a path from source 's' to sink
     *  't' in residual graph. Also fills parent[] to store the
     *  path
     * @param rGraph residualGraph
     * @param s source node
     * @param t sink node
     * @param parentNode parent node
     * @return true if sink is reached
     */
    boolean breadthFirstSearch(int[][] rGraph, int s, int t, int[] parentNode) {
        // Create a visited array and mark all vertices as not visited
        boolean[] visited = new boolean[VERTICES];
        for (int i = 0; i< VERTICES; ++i)
            visited[i]=false;

        // Create a queue, enqueue source vertex and mark source vertex as visited
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(s);
        visited[s] = true;
        parentNode[s]=-1;

        // Standard Breadth First Search Loop
        while (queue.size()!=0) {
            int u = queue.poll();

            for (int v = 0; v< VERTICES; v++) {
                if (!visited[v] && rGraph[u][v] > 0) {
                    queue.add(v);
                    parentNode[v] = u;
                    visited[v] = true;
                }
            }
        }

        // If we reached sink in BFS starting from source, then return true, else false
        return (visited[t]);
    }
}
