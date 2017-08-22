import java.io.PrintStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BstMultiset<T> extends Multiset<T> {
	/** Internal BinarySearchTree used to implement multiset semantics. */
	private BinarySearchTree<LinkedListMultiset.MapTuple<T>> mTree;

	public BstMultiset() {
		mTree = new BinarySearchTree<>();
	}
	
	public void add(T item) {
		LinkedListMultiset.MapTuple<T> node = mTree.get(new LinkedListMultiset.MapTuple<>(item));
		if (node != null) {
			node.inc();
		} else {
			mTree.add(new LinkedListMultiset.MapTuple<>(item, 1));
		}
	}

	public int search(T item) {
		LinkedListMultiset.MapTuple<T> node = mTree.get(new LinkedListMultiset.MapTuple<>(item));
		if (node != null) {
			return node.getIdx();
		}
		return 0;
	}

	public void removeOne(T item) {
		LinkedListMultiset.MapTuple<T> node = mTree.get(new LinkedListMultiset.MapTuple<>(item));
		if (node != null) {
			if (node.getIdx() > 1) {
				node.dec();
			} else {
				mTree.remove(node);
			}
		}
	}

	public void removeAll(T item) {
		mTree.remove(new LinkedListMultiset.MapTuple<>(item));
	}

	public void print(PrintStream out) {
		for (LinkedListMultiset.MapTuple<T> tmp : mTree) {
			out.println(tmp.getValue() + printDelim + tmp.getIdx());
		}
	}


	/**
	 * Double linked list class
	 */
	public static class BinarySearchTree<U extends Comparable<U>> implements Iterable<U> {

		protected Node<U> mHead;

		public BinarySearchTree() {
			mHead = null;
		}

		/**
		 * Add a new value to the start of the list.
		 *
		 * @param newValue Value to add to list.
		 */
		public void add(U newValue) {
			if (mHead == null) {
				mHead = new Node<>(newValue, null);
				return;
			}
			add(newValue, mHead);
		}

		/** Recursive implementation of add. */
		private void add(U newValue, Node<U> node) {
			int diff = compare(newValue, node.getValue());
			if (diff < 0) {
				if (node.getLeft() == null) {
					node.setLeft(new Node<>(newValue, node));
					return;
				}
				add(newValue, node.getLeft());
			} else if (diff > 0) {
				if (node.getRight() == null) {
					node.setRight(new Node<>(newValue, node));
					return;
				}
				add(newValue, node.getRight());
			}
		}

		/**
		 * Delete given value from list (delete first instance found).
		 *
		 * @param value Value to remove.
		 * @return True if deletion was successful, otherwise false.
		 */
		public boolean remove(U value) {
			return remove(value, mHead);
		}

		/** Recursive implementation of remove. */
		private boolean remove(U value, Node<U> node) {
			if (node == null) {
				return false;
			}

			int diff = compare(value, node.getValue());
			if (diff < 0) {
				return remove(value, node.getLeft());
			}
			if (diff > 0) {
				return remove(value, node.getRight());
			}
			unlink(node);
			return true;
		}

		/**
		 * Unlinks a given node from the tree.
		 *
		 * @param node Node to remove.
		 */
		private void unlink(Node<U> node) {
			if (node.getLeft() == null && node.getRight() == null) {
				if (node.getParent() == null) {
					mHead = null;
				} else if (node.getParent().getLeft() == node) {
					node.getParent().setLeft(null);
				} else if (node.getParent().getRight() == node) {
					node.getParent().setRight(null);
				}
				return;
			}
			if (node.getLeft() == null && node.getRight() != null) {
				node.replaceWith(node.getRight());
				return;
			}
			if (node.getLeft() != null && node.getRight() == null) {
				node.replaceWith(node.getLeft());
				return;
			}

			Node<U> tmp = node.getLeft();
			while (tmp.getRight() != null) {
				tmp = tmp.getRight();
			}
			node.setValue(tmp.getValue());
			unlink(tmp);
		}

		/**
		 * Returns the value stored in the tree.
		 *
		 * @param value Value to search for.
		 * @return Value if value is in list, otherwise null.
		 */
		public U get(U value) {
			return get(value, mHead);
		}

		/** Recursive implementation of get. */
		public U get(U value, Node<U> node) {
			if (node == null) {
				return null;
			}

			int diff = compare(value, node.getValue());
			if (diff < 0) {
				return get(value, node.getLeft());
			}
			if (diff > 0) {
				return get(value, node.getRight());
			}
			return node.getValue();
		}

		/**
		 * Indicates if the value is contained in the tree.
		 *
		 * @param value Value to search for.
		 * @return True if value is in list, otherwise false.
		 */
		public boolean contains(U value) {
			return get(value) != null;
		}

		/**
		 * Iterator type
		 */
		public Iterator<U> iterator() {
			return new Iterator<U>() {
				Stack<Node<U>> mStack;

				{
					mStack = new Stack<>();
					Node<U> node = mHead;
					while (node != null) {
						mStack.push(node);
						node = node.getLeft();
					}
				}

				@Override
				public boolean hasNext() {
					return !mStack.empty();
				}

				@Override
				public U next() {
					if (!hasNext()) {
						throw new NoSuchElementException();
					}
					Node<U> node = mStack.pop();
					Node<U> tmp = node.getRight();

					while (tmp != null) {
						mStack.push(tmp);
						tmp = tmp.getLeft();
					}

					return node.getValue();
				}

				@Override
				public void remove() {
					Node<U> node = mStack.pop();
					BinarySearchTree.this.remove(node.getValue());
				}
			};
		}

		/**
		 * Node type
		 */
		protected class Node<V> {
			private V mValue;
			private Node<V> mRight;
			private Node<V> mLeft;
			private Node<V> mParent;

			public Node(V value, Node<V> parent) {
				mValue = value;
				mRight = null;
				mLeft = null;
				mParent = parent;
			}

			public void replaceWith(Node<V> other) {
				mValue = other.getValue();
				mRight = other.getRight();
				mLeft = other.getLeft();
				if (mRight != null) {
					mRight.setParent(this);
				}
				if (mLeft != null) {
					mLeft.setParent(this);
				}
			}

			public void setValue(V value) {
				mValue = value;
			}
			public V getValue() {
				return mValue;
			}

			public void setRight(Node<V> right) {
				mRight = right;
			}
			public Node<V> getRight() {
				return mRight;
			}

			public void setLeft(Node<V> left) {
				mLeft = left;
			}
			public Node<V> getLeft() {
				return mLeft;
			}


			public void setParent(Node<V> parent) {
				this.mParent = parent;
			}
			public Node<V> getParent() {
				return mParent;
			}
		}

		/**
		 * Compares two items.
		 *
		 * @param val1 First item to compare.
		 * @param val2 Second item to compare.
		 * @return 0 if items are equal, a positive or negative integer describing the difference between items.
		 */
		private int compare(U val1, U val2) {
			if (val1 == null) {
				return val2 == null ? 0 : -1;
			}
			return val1.compareTo(val2);
		}
	}
}
