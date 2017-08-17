import java.io.PrintStream;
import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.Objects;

/**
 * LinkedList implementation of a multiset. Implements src.main.java.Multiset abstract class.
 *
 * @author poncholay
 */
public class LinkedListMultiset<T> extends Multiset<T> {

	/** Internal linkedlist used to implement multiset semantics. */
	protected DoubleLinkedList<MapTuple<T>> mList;

	/**
	 * Initializes the LinkedList
	 */
	public LinkedListMultiset() {
		this.mList = new DoubleLinkedList<>();
	}

	public void add(T item) {
		int index = mList.indexOf(new LinkedListMultiset.MapTuple<>(item));
		if (index != -1) {
			mList.get(index).inc();
		} else {
			mList.add(0, new MapTuple<>(item, 1));
		}
	}

	public int search(T item) {
		int index = mList.indexOf(new LinkedListMultiset.MapTuple<>(item));
		if (index != -1) {
			return mList.get(index).getIdx();
		}
		return 0;
	}

	public void removeOne(T item) {
		int index = mList.indexOf(new LinkedListMultiset.MapTuple<>(item));
		if (index != -1) {
			MapTuple<T> node = mList.get(index);
			if (node.getIdx() > 1) {
				node.dec();
			} else {
				mList.remove(index);
			}
		}
	}

	public void removeAll(T item) {
		mList.remove(new MapTuple<>(item));
	}

	public void print(PrintStream out) {
		for (MapTuple<T> tmp : mList) {
			out.println(tmp.getValue() + printDelim + tmp.getIdx());
		}
	}


	/**
	 * Double linked list class
	 */
	public static class DoubleLinkedList<U> implements Iterable<U> {

		protected Node<U> mHead;
		protected Node<U> mTail;
		protected int mLength;


		public DoubleLinkedList() {
			mHead = null;
			mTail = null;
			mLength = 0;
		}

		/**
		 * Add a new value to the start of the list.
		 *
		 * @param newValue Value to add to list.
		 */
		public void add(U newValue) {
			linkBeforeNode(new Node<>(newValue), mHead);
		}

		/**
		 * Add value (and corresponding node) at position 'index'.  Indices start at 0.
		 *
		 * @param index Position in list to add new value to.
		 * @param newValue Value to add to list.
		 *
		 * @throws IndexOutOfBoundsException In index are out of bounds.
		 */
		public void add(int index, U newValue) throws IndexOutOfBoundsException {
			if (index > mLength || index < 0) {
				throw new IndexOutOfBoundsException("Supplied index " + index + " is invalid.");
			}

			if (index == 0) {
				linkBeforeNode(new Node<>(newValue), mHead);
				return;
			}
			if (index == mLength) {
				linkAfterNode(new Node<>(newValue), mTail);
				return;
			}

			linkBeforeNode(new Node<>(newValue), getNodeAtIndex(index));
		}

		/**
		 * Returns the value stored in node at position 'index' of list.
		 *
		 * @param index Position in list to get new value for.
		 * @return Value of element at specified position in list.
		 *
		 * @throws IndexOutOfBoundsException In index are out of bounds.
		 */
		public U get(int index) throws IndexOutOfBoundsException {
			if (index >= mLength || index < 0) {
				throw new IndexOutOfBoundsException("Supplied index is invalid.");
			}

			return getNodeAtIndex(index).getValue();
		}

		/**
		 * Delete given value from list (delete first instance found).
		 *
		 * @param value Value to remove.
		 * @return True if deletion was successful, otherwise false.
		 */
		public boolean remove(U value) {
			Node<U> currNode = mHead;

			for (int i = 0; i < mLength; ++i) {
				if (Objects.equals(currNode.getValue(), value)) {
					unlinkNode(currNode);
					return true;
				}
				currNode = currNode.getNext();
			}

			return false;
		}

		/**
		 * Delete value (and corresponding node) at position 'index'.  Indices start at 0.
		 *
		 * @param index Position in list to get new value for.
		 * @return Value of node that was deleted.
		 */
		public U remove(int index) throws IndexOutOfBoundsException {
			if (index >= mLength || index < 0) {
				throw new IndexOutOfBoundsException("Supplied index is invalid.");
			}

			Node<U> node = getNodeAtIndex(index);
			U value = node.getValue();
			unlinkNode(node);

			return value;
		}

		/**
		 * Returns index of item.
		 *
		 * @param item Item for which index will be returned.
		 * @return Index of item or -1 if item is not in the list.
		 */
		public int indexOf(U item) {
			int i = 0;
			for (U tmp : this) {
				if (Objects.equals(tmp, item)) {
					return i;
				}
				++i;
			}

			return -1;
		}

		/**
		 * Returns the value stored in node at position 'index' of list.
		 *
		 * @param value Value to search for.
		 * @return True if value is in list, otherwise false.
		 */
		public boolean contains(U value) {
			return indexOf(value) != -1;
		}

		/**
		 * Links a node before another node.
		 * If node is null, the new node becomes the head of the list.
		 *
		 * @param newNode The node that will be linked.
		 * @param node The node before which the new node will be linked.
		 */
		protected void linkBeforeNode(Node<U> newNode, Node<U> node) {
			mLength++;
			if (mHead == null) {
				mHead = newNode;
				mTail = newNode;
				return;
			}

			if (node == null) {
				node = mHead;
			}
			if (node == mHead) {
				mHead = newNode;
			}

			newNode.setNext(node);
			if (node.getPrev() != null) {
				node.getPrev().setNext(newNode);
			}
			newNode.setPrev(node.getPrev());
			node.setPrev(newNode);
		}

		/**
		 * Links a node after another node.
		 * If node is null, the new node becomes the tail of the list.
		 *
		 * @param newNode The node that will be linked.
		 * @param node The node after which the new node will be linked.
		 */
		protected void linkAfterNode(Node<U> newNode, Node<U> node) {
			mLength++;
			if (mTail == null) {
				mHead = newNode;
				mTail = newNode;
				return;
			}

			if (node == null) {
				node = mTail;
			}
			if (node == mTail) {
				mTail = newNode;
			}

			newNode.setNext(node.getNext());
			if (node.getNext() != null) {
				node.getNext().setPrev(newNode);
			}
			newNode.setPrev(node);
			node.setNext(newNode);
		}

		/**
		 * Unlinks a node.
		 *
		 * @param node The node to be unlinked.
		 */
		protected void unlinkNode(Node<U> node) {
			mLength -= 1;
			if (node.getPrev() != null) {
				if (node.getNext() != null) {
					node.getNext().setPrev(node.getPrev());
				} else {
					mTail = node.getPrev();
				}
				node.getPrev().setNext(node.getNext());
			} else {
				mHead = node.getNext();
				if (mHead == null) {
					mTail = null;
				} else {
					mHead.setPrev(null);
				}
			}
		}

		/**
		 * Returns the node with the corresponding index.
		 *
		 * @param index The index of the node.
		 * @return Node with corresponding index or the head/tail of the list if the index is too small/big.
		 */
		protected Node<U> getNodeAtIndex(int index) {
			Node<U> currNode;

			if (index < Math.ceil(mLength / 2)) {
				currNode = mHead;
				for (int i = 0; i < index; ++i) {
					currNode = currNode.getNext();
				}
			} else {
				currNode = mTail;
				for (int i = mLength - 1; i > index; --i) {
					currNode = currNode.getPrev();
				}
			}

			return currNode;
		}

		/**
		 * Iterator type
		 */
		public Iterator<U> iterator() {
			return new Iterator<U>() {

				Node<U> current = mHead;

				@Override
				public boolean hasNext() {
					return current != null;
				}

				@Override
				public U next() {
					if (hasNext()) {
						U ret = current.getValue();
						current = current.getNext();
						return ret;
					}
					return null;
				}

				@Override
				public void remove() {
					Node<U> tmp = current;
					current = current.getNext();
					LinkedListMultiset.DoubleLinkedList.this.unlinkNode(tmp);
				}
			};
		}

		/**
		 * Node type
		 */
		protected class Node<V> {
			private V mValue;
			private Node<V> mNext;
			private Node<V> mPrev;

			public Node(V value) {
				mValue = value;
				mNext = null;
				mPrev = null;
			}

			public void setValue(V value) {
				mValue = value;
			}
			public V getValue() {
				return mValue;
			}

			public void setNext(Node<V> next) {
				mNext = next;
			}
			public Node<V> getNext() {
				return mNext;
			}

			public void setPrev(Node<V> prev) {
				mPrev = prev;
			}
			public Node<V> getPrev() {
				return mPrev;
			}
		}
	}


	/**
	 * Map Tuple class
	 */
	public static class MapTuple<W> implements Comparable<MapTuple<W>> {
		private W value;
		private int idx;

		public MapTuple(W value) {
			this(value, 0);
		}

		public MapTuple(W value, int idx) {
			this.value = value;
			this.idx = idx;
		}

		public void inc() {
			this.idx++;
		}
		public void dec() {
			this.idx--;
		}

		public W getValue() {
			return value;
		}
		public void setValue(W value) {
			this.value = value;
		}

		public int getIdx() {
			return idx;
		}
		public void setIdx(int idx) {
			this.idx = idx;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof LinkedListMultiset.MapTuple)) return false;
			MapTuple node = (MapTuple) o;
			return Objects.equals(value, node.value);
		}

		@Override
		public int hashCode() {
			return Objects.hash(value);
		}

		@Override
		public int compareTo(MapTuple<W> node) {
			try {
				@SuppressWarnings("unchecked")
				Comparable<W> a = (Comparable) value;
				return a.compareTo(node.getValue());
			} catch (Exception e) {
				throw new InvalidParameterException();
			}
		}
	}
}