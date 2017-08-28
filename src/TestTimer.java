import java.io.*;

public class TestTimer {

	private static long startTime = -1;

	private static void start(BufferedReader inReader, Multiset<String> multiset) throws IOException {
		String line;

		while ((line = inReader.readLine()) != null) {
			String[] tokens = line.split(" ");

			if (tokens.length < 1) {
				continue;
			}

			String command = tokens[0];
			switch (command.toUpperCase()) {
				case "A":
					if (tokens.length == 2) {
						multiset.add(tokens[1]);
					}
					break;
				case "S":
					if (tokens.length == 2) {
						multiset.search(tokens[1]);
					}
					break;
				case "RO":
					if (tokens.length == 2) {
						multiset.removeOne(tokens[1]);
					}
					break;
				case "RA":
					if (tokens.length == 2) {
						multiset.removeAll(tokens[1]);
					}
					break;
				case "P":
					multiset.print(new PrintStream(System.out));
					break;
				case "X":
					if (startTime == -1) {
						startTime = System.nanoTime();
					}
				case "Q":
					return;
				default:
			}
		}
	}

	public static void main(String[] args) {

		String imp = args[0];
		String input = args[1];

		Multiset<String> multiset;
		switch(imp) {
			case "linkedlist":
				multiset = new LinkedListMultiset<>();
				break;
			case "sortedlinkedlist":
				multiset = new SortedLinkedListMultiset<>();
				break;
			case "bst":
				multiset = new BstMultiset<>();
				break;
			case "hash":
				multiset = new HashMultiset<>();
				break;
			case "baltree":
				multiset = new BalTreeMultiset<>();
				break;
			default:
				System.err.println("Unknown implementation type.");
				return;
		}

		try {
			PrintStream original = System.out;
			System.setOut(new PrintStream(new OutputStream() {
				public void write(int b) {}
			}));

			long time = 0;
			for (int i = 0; i < 5; i++) {
				start(new BufferedReader(new InputStreamReader(new FileInputStream(input))), multiset);
				long endTime = System.nanoTime();
				time += (endTime - startTime);
			}
			System.setOut(original);

			System.out.println((time / 5) / Math.pow(10, 9));
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
