package ips.other;

import ips.algorithm.knn.DistanceResult;
import ips.data.entities.wlan.WLANFingerprint;

import java.util.Map.Entry;

/**
 * 
 * Standard quicksort algorithm.
 * 
 * @author Wouter Van Rossem
 * 
 */
public class QuickSort {
	private Entry<WLANFingerprint, DistanceResult>[] entries;

	public Entry<WLANFingerprint, DistanceResult>[] sort(
			Entry<WLANFingerprint, DistanceResult>[] values) {

		if (values == null || values.length == 0) {
			return values;
		}

		this.entries = values;

		int length = values.length;
		quicksort(0, length - 1);

		return entries;
	}

	private void quicksort(int low, int high) {
		int i = low, j = high;

		Entry<WLANFingerprint, DistanceResult> pvt = entries[low + (high - low)
				/ 2];

		int pivot = 0;

		if (pvt == null) {
			System.out.println("Pivot is null");
			return;
		} else {
			pivot = pvt.getValue().nrOfMatchedBssids;
		}

		while (i <= j) {

			while (entries[i].getValue().nrOfMatchedBssids < pivot) {
				i++;
			}

			while (entries[j].getValue().nrOfMatchedBssids > pivot) {
				j--;
			}

			if (i <= j) {
				swap(i, j);
				i++;
				j--;
			}
		}

		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);
	}

	private void swap(int i, int j) {
		Entry<WLANFingerprint, DistanceResult> temp = entries[i];
		entries[i] = entries[j];
		entries[j] = temp;
	}
}
