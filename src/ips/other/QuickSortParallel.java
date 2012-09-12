package ips.other;

import ips.algorithm.knn.DistanceResult;
import ips.data.entities.wlan.WLANFingerprint;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 
 * An implementation of the quicksort algorithm that uses the forkjoin
 * framework.
 * 
 * @author Wouter Van Rossem
 * 
 */
public class QuickSortParallel {

	static final ForkJoinPool fjPool = new ForkJoinPool();

	class QuickSortTask extends
			RecursiveTask<List<Entry<WLANFingerprint, DistanceResult>>> {

		/**
		 * Generated UID
		 */
		private static final long serialVersionUID = -3621244686090972354L;

		int SEQUENTIAL_CUTOFF = 10;

		Entry<WLANFingerprint, DistanceResult>[] entries;

		int low, high;

		public QuickSortTask(Entry<WLANFingerprint, DistanceResult>[] entries,
				int low, int high) {

			this.entries = entries;
			this.low = low;
			this.high = high;

		}

		@SuppressWarnings("unused")
		@Override
		protected List<Entry<WLANFingerprint, DistanceResult>> compute() {

			if (high - low < SEQUENTIAL_CUTOFF) {

				int pivot = entries[random(low, high)].getValue().nrOfMatchedBssids; // random
																						// pivot
				int i = low - 1;
				int j = high;

				while (true) {
					do {
						i++;
					} while (entries[i].getValue().nrOfMatchedBssids < pivot);
					do {
						j--;
					} while (entries[j].getValue().nrOfMatchedBssids > pivot);
					if (i < j) {
						swap(i, j);
					} else {
						break;
					}
				}

			} else {

				int mid = (int) Math.floor((high + low) / 2);

				QuickSortTask t1 = new QuickSortTask(entries, low, mid);
				QuickSortTask t2 = new QuickSortTask(entries, mid + 1, high);

				t1.fork();

				List<Entry<WLANFingerprint, DistanceResult>> res = t2.compute();
				List<Entry<WLANFingerprint, DistanceResult>> res2 = t1.join();

			}

			return null;
		}

		/**
		 * http://stackoverflow.com/questions/363681/java-generating-random-
		 * number-in-a-range
		 * 
		 * @param min
		 * @param max
		 * @return
		 */
		private int random(int min, int max) {

			return min + (int) (Math.random() * ((max - min) + 1));

		}

		private void swap(int i, int j) {

			Entry<WLANFingerprint, DistanceResult> temp = entries[i];
			entries[i] = entries[j];
			entries[j] = temp;

		}

		public void quicksort(Entry<WLANFingerprint, DistanceResult>[] a,
				int lo, int hi) {
			if (lo < hi - 1) {
				int pv = a[random(lo, hi)].getValue().nrOfMatchedBssids; // random
																			// pivot
				int i = lo - 1;
				int j = hi;
				while (true) {
					do {
						i++;
					} while (a[i].getValue().nrOfMatchedBssids < pv);
					do {
						j--;
					} while (a[j].getValue().nrOfMatchedBssids > pv);
					if (i < j) {
						swap(i, j);
					} else {
						break;
					}
				}
				quicksort(a, lo, j + 1);
				quicksort(a, j + 1, hi);
			}
		}
	}

}
