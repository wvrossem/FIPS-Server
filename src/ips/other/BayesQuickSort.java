package ips.other;

import ips.algorithm.bayes.data.BayesFingerprint;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author Wouter Van Rossem
 *
 */
public class BayesQuickSort {
	private Entry<BayesFingerprint, Integer>[] entries;

	@SuppressWarnings("unchecked")
	public Entry<BayesFingerprint, Integer>[] sort(
			Map<BayesFingerprint, Integer> probabilities) {

		int size = probabilities.size();
		Entry<BayesFingerprint, Integer>[] values = new Map.Entry[size];

		probabilities.entrySet().toArray(values);

		return sort(values);
	}

	public Entry<BayesFingerprint, Integer>[] sort(
			Entry<BayesFingerprint, Integer>[] values) {

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

		Entry<BayesFingerprint, Integer> pvt = entries[low + (high - low) / 2];

		int pivot = 0;

		if (pvt == null) {
			System.out.println("Pivot is null");
			return;
		} else {
			pivot = pvt.getValue();
		}

		while (i <= j) {

			while (entries[i].getValue() > pivot) {
				i++;
			}

			while (entries[j].getValue() < pivot) {
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
		Entry<BayesFingerprint, Integer> temp = entries[i];
		entries[i] = entries[j];
		entries[j] = temp;
	}

}
