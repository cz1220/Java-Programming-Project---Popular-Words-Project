import java.util.Comparator;
import java.util.LinkedList;

public class MergeSort {

    // -------- support for top-down merge-sort of arrays --------
    /** Merge contents of arrays S1 and S2 into properly sized array S. */
    public static void merge(Word[] S1, Word[] S2, Word[] S, Comparator<Word> comp) {
        int i = 0, j = 0;
        while (i + j < S.length) {
            if (j == S2.length || (i < S1.length && comp.compare(S1[i], S2[j]) < 0))
                S[i + j] = S1[i++]; // copy ith element of S1 and increment i
            else
                S[i + j] = S2[j++]; // copy jth element of S2 and increment j
        }
    }

    /** Merge-sort contents of array S. */
    public static void mergeSort(Word[] S, Comparator<Word> comp) {
        int n = S.length;
        if (n < 2)
            return; // array is trivially sorted
        // divide
        int mid = n / 2;
        Word[] S1 = new Word[mid];
        System.arraycopy(S, 0, S1, 0, mid); // copy of first half
        Word[] S2 = new Word[n - mid];
        System.arraycopy(S, mid, S2, 0, n - mid); // copy of second half
        // conquer (with recursion)
        mergeSort(S1, comp); // sort copy of first half
        mergeSort(S2, comp); // sort copy of second half
        // merge results
        merge(S1, S2, S, comp); // merge sorted halves back into original
    }

    // -------- support for top-down merge-sort of queues --------
    /** Merge contents of sorted queues S1 and S2 into empty queue S. */
    public static void merge(LinkedList<Word> S1, LinkedList<Word> S2, LinkedList<Word> S, Comparator<Word> comp) {
        while (!S1.isEmpty() && !S2.isEmpty()) {
            if (comp.compare(S1.getFirst(), S2.getFirst()) < 0)
                S.add(S1.pollFirst()); // take next element from S1
            else
                S.add(S2.pollFirst()); // take next element from S2
        }
        while (!S1.isEmpty())
            S.add(S1.pollFirst()); // move any elements that remain in S1
        while (!S2.isEmpty())
            S.add(S2.pollFirst()); // move any elements that remain in S2
    }

    /** Merge-sort contents of queue. */
    public static void mergeSort(LinkedList<Word> S, Comparator<Word> comp) {
        int n = S.size();
        if (n < 2)
            return; // queue is trivially sorted
        // divide
        LinkedList<Word> S1 = new LinkedList<>(); // (or any queue implementation)
        LinkedList<Word> S2 = new LinkedList<>();
        while (S1.size() < n / 2)
            S1.add(S.pollFirst()); // move the first n/2 elements to S1
        while (!S.isEmpty())
            S2.add(S.pollFirst()); // move remaining elements to S2
        // conquer (with recursion)
        mergeSort(S1, comp); // sort first half
        mergeSort(S2, comp); // sort second half
        // merge results
        merge(S1, S2, S, comp); // merge sorted halves back into original
    }

    // -------- support for bottom-up merge-sort of arrays --------
    /** Merges in[start..start+inc-1] and in[start+inc..start+2*inc-1] into out. */
    public static void merge(Word[] in, Word[] out, Comparator<Word> comp, int start, int inc) {
        int end1 = Math.min(start + inc, in.length); // boundary for run 1
        int end2 = Math.min(start + 2 * inc, in.length); // boundary for run 2
        int x = start; // index into run 1
        int y = start + inc; // index into run 2
        int z = start; // index into output
        while (x < end1 && y < end2)
            if (comp.compare(in[x], in[y]) < 0)
                out[z++] = in[x++]; // take next from run 1
            else
                out[z++] = in[y++]; // take next from run 2
        if (x < end1)
            System.arraycopy(in, x, out, z, end1 - x); // copy rest of run 1
        else if (y < end2)
            System.arraycopy(in, y, out, z, end2 - y); // copy rest of run 2
    }

    @SuppressWarnings({ "unchecked" })
    /** Merge-sort contents of data array. */
    public static void mergeSortBottomUp(Word[] orig, Comparator<Word> comp) {
        int n = orig.length;
        Word[] src = orig; // alias for the original
        Word[] dest = (Word[]) new Object[n]; // make a new temporary array
        Word[] temp; // reference used only for swapping
        for (int i = 1; i < n; i *= 2) { // each iteration sorts all runs of length i
            for (int j = 0; j < n; j += 2 * i) // each pass merges two runs of length i
                merge(src, dest, comp, j, i);
            temp = src;
            src = dest;
            dest = temp; // reverse roles of the arrays
        }
        if (orig != src)
            System.arraycopy(src, 0, orig, 0, n); // additional copy to get result to original
    }
}
