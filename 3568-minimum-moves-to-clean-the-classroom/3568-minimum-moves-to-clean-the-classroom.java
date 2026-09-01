import java.util.*;

class Solution {
    public int minMoves(String[] classroom, int energy) {
        int m = classroom.length;
        int n = classroom[0].length();
        
        List<int[]> litterList = new ArrayList<>();
        int startR = 0, startC = 0;
        
        // Locate starting position 'S' and litter positions 'L'
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                char ch = classroom[r].charAt(c);
                if (ch == 'S') {
                    startR = r;
                    startC = c;
                } else if (ch == 'L') {
                    litterList.add(new int[]{r, c});
                }
            }
        }
        
        int numLitter = litterList.size();
        int fullMask = (1 << numLitter) - 1;
        
        // Map grid positions of litter to their respective bit indexes
        int[][] litterIdx = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(litterIdx[i], -1);
        }
        for (int i = 0; i < numLitter; i++) {
            litterIdx[litterList.get(i)[0]][litterList.get(i)[1]] = i;
        }
        
        // bestEnergy[r][c][mask] stores the max remaining energy seen for state (r, c, mask)
        int[][][] bestEnergy = new int[m][n][1 << numLitter];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Arrays.fill(bestEnergy[i][j], -1);
            }
        }
        
        // Handle initial mask if student starts directly on a litter cell
        int initialMask = 0;
        if (litterIdx[startR][startC] != -1) {
            initialMask |= (1 << litterIdx[startR][startC]);
        }
        
        if (initialMask == fullMask) {
            return 0;
        }
        
        // Queue elements: {r, c, mask, curEnergy, steps}
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startR, startC, initialMask, energy, 0});
        bestEnergy[startR][startC][initialMask] = energy;
        
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int r = curr[0];
            int c = curr[1];
            int mask = curr[2];
            int curE = curr[3];
            int steps = curr[4];
            
            if (mask == fullMask) {
                return steps;
            }
            
            // Cannot move further if energy is depleted (and not standing on 'R')
            if (curE == 0) {
                continue;
            }
            
            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];
                
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && classroom[nr].charAt(nc) != 'X') {
                    int nextE = curE - 1;
                    char nextCell = classroom[nr].charAt(nc);
                    
                    // Restore energy to max capacity if landing on 'R'
                    if (nextCell == 'R') {
                        nextE = energy;
                    }
                    
                    int nextMask = mask;
                    if (nextCell == 'L') {
                        nextMask |= (1 << litterIdx[nr][nc]);
                    }
                    
                    if (nextMask == fullMask) {
                        return steps + 1;
                    }
                    
                    // Prune state if we've reached (nr, nc, nextMask) with equal or higher energy
                    if (nextE > bestEnergy[nr][nc][nextMask]) {
                        bestEnergy[nr][nc][nextMask] = nextE;
                        queue.add(new int[]{nr, nc, nextMask, nextE, steps + 1});
                    }
                }
            }
        }
        
        return -1;
    }
}