import java.util.*;

class Solution {
    public List<List<Integer>> palindromePairs(String[] words) {
        List<List<Integer>> result = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();

       
        for (int i = 0; i < words.length; i++) {
            map.put(words[i], i);
        }

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            int n = word.length();

            for (int j = 0; j <= n; j++) {
                String prefix = word.substring(0, j);
                String suffix = word.substring(j);

          
                if (isPalindrome(prefix)) {
                    String reversedSuffix = new StringBuilder(suffix).reverse().toString();
                    if (map.containsKey(reversedSuffix) && map.get(reversedSuffix) != i) {
                        result.add(Arrays.asList(map.get(reversedSuffix), i));
                    }
                }

                
                if (j != n && isPalindrome(suffix)) {
                    String reversedPrefix = new StringBuilder(prefix).reverse().toString();
                    if (map.containsKey(reversedPrefix) && map.get(reversedPrefix) != i) {
                        result.add(Arrays.asList(i, map.get(reversedPrefix)));
                    }
                }
            }
        }

        return result;
    }

    private boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left++) != s.charAt(right--)) {
                return false;
            }
        }
        return true;
    }
}