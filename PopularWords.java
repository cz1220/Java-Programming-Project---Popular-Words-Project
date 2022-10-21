import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Map.Entry;

class Word {
    public String word;
    public int freq;
}

public class PopularWords {
    private HashMap<String, Integer> table;
    private Word[] words;
    private String filename;
    private String keywords;
    private int k;

    
    public static void main(String[] args) {
        PopularWords popularWords = new PopularWords();
        popularWords.readParameters();
        popularWords.loadWords();
        popularWords.sort();
        popularWords.print();
    }
    
    
    /**
     * Constructor
     */
    public PopularWords() {
        table = new HashMap<>();
        k = -1;
    }

    /**
     * Read parameters from file.
     */
    public void readParameters() {
        try {
            Scanner scanner = new Scanner(new File("parameter.txt"));
            filename = scanner.nextLine().trim();
            keywords = scanner.nextLine().trim();
            if (scanner.hasNextLine()) {
                k = scanner.nextInt();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load words.
     */
    public void loadWords() {
        try {
            // Parse words.
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = line.replace("--", " ").trim();
                StringTokenizer token = new StringTokenizer(line, " \"',!?.()[]*/;#$%0123456789");
                while (token.hasMoreTokens()) {
                    String word = token.nextToken().toLowerCase();
                    if (word.equals("-")) {
                        continue;
                    }
                    // Put words to hashtable.
                    if (table.containsKey(word)) {
                        table.put(word, table.get(word) + 1);
                    } else {
                        table.put(word, 1);
                    }
                }
            }
            scanner.close();

            // Prepare for merge sort.
            words = new Word[table.size()];
            int index = 0;
            for (Entry<String, Integer> entry : table.entrySet()) {
                String key = entry.getKey();
                int freq = entry.getValue();
                Word word = new Word();
                word.word = key;
                word.freq = freq;
                words[index++] = word;
            }
            // Set value k to size of table if not set.
            if (k == -1) {
                k = table.size();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use merge sort to sort words.
     */
    public void sort() {
        MergeSort.mergeSort(words, new Comparator<Word>() {
            @Override
            public int compare(Word word1, Word word2) {
                if (keywords.equalsIgnoreCase("name")) { // Sort by name.
                    return word1.word.compareTo(word2.word);
                } else if (keywords.equalsIgnoreCase("frequency")) { // Sort by frequency.
                    if (word1.freq > word2.freq) {
                        return -1;
                    } else if (word1.freq < word2.freq) {
                        return 1;
                    } else {
                        return word1.word.compareTo(word2.word);
                    }
                } else if (keywords.equalsIgnoreCase("scarcity")) { // Sort by scarcity.
                    if (word1.freq < word2.freq) {
                        return -1;
                    } else if (word1.freq > word2.freq) {
                        return 1;
                    } else {
                        return word1.word.compareTo(word2.word);
                    }
                }
                return 0;
            }
        });
    }

    /**
     * Print the result.
     */
    public void print() {
        for (int i = 0; i < k; i++) {
        	System.out.print(words[i].word + " "); 
        	System.out.print(words[i].freq); 
        	System.out.println(); 
            
        }
    }

}