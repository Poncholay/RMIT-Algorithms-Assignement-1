/**
 * SortedLinkedList implementation of a multiset. Extends src.main.java.LinkedListMultiset which Implements src.main.java.Multiset abstract class.
 *
 * @author poncholay
 */
public class SortedLinkedListMultiset<T> extends LinkedListMultiset<T> {

	/**
	 * Initializes the SortedLinkedList
	 */
	public SortedLinkedListMultiset() {
		this.mList = new SortedDoubleLinkedList<>();
	}

	/**
	 * Sorted double linked list class
	 */
	public class SortedDoubleLinkedList<U extends Comparable<U>> extends LinkedListMultiset.DoubleLinkedList<U> implements Iterable<U> {

		public SortedDoubleLinkedList() {
			super();
		}

		/**
		 * Add a new value to the list.
		 *
		 * @param newValue Value to add to list.
		 */
		@Override
		public void add(U newValue) {
			Node<U> newNode = new Node<>(newValue);

			try {
				if (mHead != null && compare(newValue, mHead.getValue()) < 0) {
					linkBeforeNode(newNode, null);
					return;
				}
				if (mTail != null && compare(newValue, mTail.getValue()) > 0) {
					linkAfterNode(newNode, null);
					return;
				}

				Node<U> currNode = mHead;
				for (int i = 0; i < mLength && currNode != null; ++i) {
					if (compare(newValue, currNode.getValue()) < 0) {
						linkBeforeNode(newNode, currNode);
						return;
					}
					currNode = currNode.getNext();
				}

			} catch (Exception ignored) {}

			linkAfterNode(newNode, null);
		}

		/**
		 * Add a new value to the list.
		 *
		 * @param index    Ignored.
		 * @param newValue Value to add to list.
		 */
		@Override
		public void add(int index, U newValue) {
			add(newValue);
		}

		/**
		 * Delete given value from list (delete first instance found).
		 *
		 * @param item Value to remove.
		 * @return True if deletion was successful, otherwise false.
		 */
		@Override
		public boolean remove(U item) {
			try {
				if ((mHead != null && compare(item, mHead.getValue()) < 0) || (mTail != null && compare(item, mTail.getValue()) > 0)) {
					return false;
				}
			} catch (Exception ignored) {}

			return super.remove(item);
		}

		/**
		 * Returns index of item.
		 *
		 * @param item Item for which index will be returned.
		 * @return Index of item or -1 if item is not in the list.
		 */
		@Override
		public int indexOf(U item) {
			try {
				if ((mHead != null && compare(item, mHead.getValue()) < 0) || (mTail != null && compare(item, mTail.getValue()) > 0)) {
					return -1;
				}
			} catch (Exception ignored) {}

			return super.indexOf(item);
		}

		/**
		 * Compares two items.
		 *
		 * @param val1 First item to compare.
		 * @param val2 Second item to compare.
		 * @return 0 if items are equal, a positive or negative interger describing the difference between items.
		 */
		private int compare(U val1, U val2) {
			if (val1 == null) {
				return val2 == null ? 0 : -1;
			}
			return val1.compareTo(val2);
		}
	}
}