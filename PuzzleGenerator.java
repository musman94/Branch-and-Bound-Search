import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.HashSet;

public class PuzzleGenerator {

    class Node implements Comparable<Node> {
        public int cost;
        public int level;
        public Node parent;
        public int[] puzzle;
        public int zero_index;

        public Node( int[] puzzle, int zeroIndex, int newIndex, int newLevel, Node parent) {
            this.parent = parent;
            this.puzzle = new int[puzzle.length];
            for( int i = 0; i < puzzle.length; i++)
                this.puzzle[i] = puzzle[i];

            int temp = this.puzzle[zeroIndex];
            this.puzzle[zeroIndex] = this.puzzle[newIndex];
            this.puzzle[newIndex] = temp;

            this.cost = Integer.MAX_VALUE;
            this.level = newLevel;
            this.zero_index = newIndex;
        }

        @Override
        public int compareTo(Node o) {
            return 0;
        }

        public int getCost() {
            return cost;
        }
    }
    // bottom, left, top, right
    int[] cases = {3, -1, -3, 1};

    public static void main(String[] args) {
        int[] initialState = {6, 1, 8, 2, 5, 3, 7, 0, 4};
        //int[] initialState = {1, 2, 3, 8, 4, 5, 7, 6, 0};
        int[] goalState = {1, 2, 3, 8, 0, 4, 7, 6, 5};

        PuzzleGenerator generator = new PuzzleGenerator();
        //int[] initialState = generator.generateRandomInitialPuzzle();
        int zeroIndex = generator.findIndexOfZero(initialState);

        if (generator.checkIfSolvable(initialState)) {
            System.out.println("The puzzle is solvable");
            long startTime = System.nanoTime() * 1000000000;
            generator.solve(initialState, goalState, zeroIndex);
            long endTime = System.nanoTime() * 1000000000;
            System.out.println("Total time for BBS solve method: " + (endTime-startTime));
        } else
            System.out.println("The puzzle is not solvable");
    }

    public int findIndexOfZero(int[] arr) {
        int index = 0;
        for (int i = 0; i < 9; i++) {
            if (arr[i] == 0) {
                index = i;
            }
        }
        return index;
    }

    public boolean checkIfSolvable(int arr[]) {
        int inversions = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j] && arr[j] != 0) {
                    inversions++;
                }
            }
        }
        return (inversions % 2 != 0);
    }

    public void solve(int[] initialState, int[] goalState, int zeroIndex) {
        HashSet<Integer> hash_Set = new HashSet<Integer>();
        PriorityQueue<Node> queue = new PriorityQueue<Node>( 1000, (a, b) -> (a.cost + a.level) - (b.cost + b.level));
        Node root = new Node(initialState, zeroIndex, zeroIndex, 0, null);
        root.cost = calculateCost(initialState, goalState);
        System.out.println("Root cost: " + root.cost);
        queue.add(root);
        //hash_Set.add(root);
        System.out.println("Initial State: ");
        displayPuzzle(initialState);
        int queueSize = 0;

        while (!queue.isEmpty()) {

            if( queue.size() > queueSize)
                queueSize = queue.size();

            Node min = queue.poll();

            if (min.cost == 0) {
                System.out.println("Complete path: ");
                displayPath(min);
                System.out.println("Number of steps: " + min.level);
                System.out.println("Maximum queue size: " + queueSize);
                return;
            }

            for (int i = 0; i < 4; i++) {
                if (safeToSwap(min.zero_index, cases[i])) 
                {

                    if(!(hash_Set.contains(min.zero_index + cases[i])))
                    {
                    Node child = new Node(min.puzzle, min.zero_index, min.zero_index + cases[i], min.level + 1, min);
                    child.cost = calculateCost(child.puzzle, goalState);
                    queue.add(child);
                    }
                }
            }
            hash_Set.add(min.zero_index);
        }
    }

    public int calculateCost(int[] initialState, int[] goalState) {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            if (initialState[i] != 0 && initialState[i] != goalState[i]) {
                count++;
            }
        }
        return count;
    }

    public boolean checkIfExists(int[] puzzle, ArrayList<int[]> list) {
        boolean exists = true;
        for (int i = 0; i < list.size(); i++) {
            exists = true;
            for( int j = 0; j < list.get(i).length; j++) {
                if( list.get(i)[j] != puzzle[j])
                    exists = false;
            }
        }
        return exists;
    }

    public boolean safeToSwap(int zeroIndex, int caseI) {
        boolean isSafe = false;
        //( int[] puzzle, int zeroIndex, int newIndex, int newLevel, Node parent)
        if (caseI == -1) {
            if (((zeroIndex + caseI) != 5 && (zeroIndex + caseI) != 2) && (zeroIndex + caseI) >= 0) {
                //Node temp = new Node(node.puzzle, zeroIndex, zeroIndex + caseI, 0, null);
                //if(!visited)
                //if(hash_Set.contains())
                    isSafe = true;
            }
        } else if (caseI == 1) {
            if ((zeroIndex + caseI) % 3 != 0 && (zeroIndex + caseI) >= 0) {
               // Node temp = new Node(node.puzzle, zeroIndex, zeroIndex + caseI, 0, null);
               //if(!visited)
                    isSafe = true;
            }
        } else if (caseI == -3) {
            if ((zeroIndex + caseI) < 9 && (zeroIndex + caseI) >= 0) {
                //Node temp = new Node(node.puzzle, zeroIndex, zeroIndex + caseI, 0, null);
               //if(!visited)
                    isSafe = true;
            }
        } else if (caseI == 3) {
            if ((zeroIndex + caseI) < 9 && (zeroIndex + caseI) >= 0) {
                //Node temp = new Node(node.puzzle, zeroIndex, zeroIndex + caseI, 0, null);
                //if(!visited)
                    isSafe = true;
            }
        }
        return isSafe;
    }

    public void displayPuzzle(int[] puzzle) {
        for (int i = 1; i <= 9; i++) {

            System.out.print(puzzle[i - 1] + " ");
            if (i % 3 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    public void displayPath(Node root) {
        if (root == null) {
            return;
        }
        displayPath(root.parent);
        displayPuzzle(root.puzzle);
        System.out.println();
    }
    public int min(ArrayList<Integer> list) {
        int min = list.get(0);
        for( int i = 0; i < list.size(); i++) {
            if (list.get(i) < min)
                min = list.get(i);
        }
        return min;
    }
    public void removeEverything(ArrayList<Integer> list) {
        while( !list.isEmpty())
            list.remove(0);
    }

    public int[] generateRandomInitialPuzzle() {
        // creating a random initial state Puzzle
        int[] newArr = new int[9];

        for (int i = 0; i < 9; i++) {
            newArr[i] = i;
        }

        Random random = new Random();
        int[] initialState = new int[9];
        for (int i = 0; i < 9; i++) {
            int randomIndex = random.nextInt(newArr.length);
            int randomNumber = newArr[randomIndex];
            newArr = remove(newArr, randomIndex);
            initialState[i] = randomNumber;
        }
        return initialState;
    }
    public int[] remove(int[] arr, int index) {
        int[] newArr = new int[arr.length - 1];
        for (int i = 0; i < index; i++) {
            newArr[i] = arr[i];
        }
        for (int i = index + 1; i < arr.length; i++) {
            newArr[i - 1] = arr[i];
        }
        return newArr;
    }
}
