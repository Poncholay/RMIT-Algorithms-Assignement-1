import org.junit.Before;
import org.junit.Test;

public class TestDoubleLinkedList {

	private LinkedListMultiset.DoubleLinkedList<String> mList;

	@Before
	public void setupList() {
		mList = new LinkedListMultiset.DoubleLinkedList<>();
	}

	@Test
	public void testAdd() throws IndexOutOfBoundsException {
		mList.add("1");
	}

	@Test
	public void testAddIndexValid() throws IndexOutOfBoundsException {
		mList.add(0, "2");
		mList.add(0, "0");
		mList.add(2, "4");
		mList.add(2, "3");
		mList.add(1, "1");
		for (String s : mList) {
			System.out.println(s);
		}
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testAddIndexInvalid() throws IndexOutOfBoundsException {
		mList.add(2, "1");
	}
}
