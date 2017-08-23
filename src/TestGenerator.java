import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestGenerator {
	private int multisetSize;
	private int nbOperations;
	private String name;
	private IntegerGenerator generator;
	private int[] data;
	private List<String> commands;

	public TestGenerator(int multisetSize) {
		this(multisetSize, 0, "");
	}

	public TestGenerator(int multisetSize, int nbOperations, String name) {
		this(multisetSize, nbOperations, Arrays.asList("A", "S", "RO", "RA"), name);
	}

	public TestGenerator(int multisetSize, int nbOperations, List<String> commands, String name) {
		this.multisetSize = multisetSize;
		this.nbOperations = nbOperations;
		this.generator = new IntegerGenerator(0, multisetSize * 2, System.currentTimeMillis());
		this.commands = commands;
		this.name = name;
	}

	private void generateOperations(PrintWriter writer) {
		List<Integer> searches = new ArrayList<>();

		int[] op = generator.sampleWithReplacement(nbOperations);
		for (int i = 0; i < op.length; i++) {
			String command = commands.get(op[i] % commands.size());
			int number = data[i % data.length];
			if (command.equals("S")) {
				if (!searches.contains(number)) {
					searches.add(number);
				} else {
					continue;
				}
			}
			writer.println(command + " " + number);
		}
		writer.println("P");
	}

	private void generateMultiset(PrintWriter writer) throws IOException {
		data = generator.sampleWithReplacement(multisetSize);
		for (int i : data) {
			writer.println("A" + " " + i);
		}
		writer.println("P");
	}

	private void generateSequence(String name) throws IOException {
		new File(name).createNewFile();
		PrintWriter writer = new PrintWriter(new FileWriter(name), true);
		generateMultiset(writer);
		generateOperations(writer);
	}

	private void generateOutput(String name) throws IOException {
		String input = name + "." + this.name + ".in";
		String out = name + ".exp";
		String searchOut = name + ".search.exp";

		new File(out).createNewFile();
		new File(searchOut).createNewFile();

		BufferedReader inReader = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
		PrintWriter searchOutWriter = new PrintWriter(new FileWriter(searchOut), true);

		MultisetTester.outStream = new PrintStream(new FileOutputStream(out));
		MultisetTester.processOperations(inReader, searchOutWriter, new HashMultiset<>());
	}

	private String generateName() throws Exception {
		File dir = new File("Tests");
		if (dir.exists()) {
			File[] files = dir.listFiles((file, s) -> s.startsWith("test") && s.endsWith(".in"));
			if (files == null) {
				throw new Exception();
			}
			int max = 0;
			for (File file : files) {
				String number = file.getName().substring(4);
				String[] tmp = number.split("\\.");
				int j = Integer.parseInt(tmp[0]);
				if (j > max) {
					max = j;
				}
			}
			return "Tests/" + "test" + Integer.toString(max + 1);
		}
		throw new Exception();
	}

	public void generateTest() throws Exception {
		String name = generateName();

		generateSequence(name + "." + this.name + ".in");
		generateOutput(name);
	}

	public static class Builder {

		private int multisetSize;
		private int nbOperations;
		private String name;
		private List<String> commands;

		public Builder() {
			commands = new ArrayList<>();
		}

		public TestGenerator build() {
			if (commands.size() != 0) {
				return new TestGenerator(multisetSize, nbOperations, commands, name);
			}
			return new TestGenerator(multisetSize, nbOperations, name);
		}

		public Builder size(int size) {
			this.multisetSize = size;
			return this;
		}

		public Builder operations(int operations) {
			this.nbOperations = operations;
			return this;
		}

		public Builder add() {
			commands.add("A");
			return this;
		}

		public Builder remove() {
			commands.add("RO");
			return this;
		}

		public Builder removeAll() {
			commands.add("RA");
			return this;
		}

		public Builder search() {
			commands.add("S");
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}
	}

	static public void main(String args[]) throws Exception {
		for (int i = 0; i < 5; i++) {
			int size = (int) Math.pow(10, i);
			//Growing multiset
			new TestGenerator.Builder().name("grow" + " " + i).size(size).add().build().generateTest();
			//Roughly static multiset
			new TestGenerator.Builder().name("maintain" + " " + i).size(size).operations(size).add().remove().build().generateTest();
			//Shrinking multiset
			new TestGenerator.Builder().name("decrease" + " " + i).size(size).operations(size).remove().build().generateTest();
			//Growing multiset with searches
			new TestGenerator.Builder().name("grow-search" + " " + i).size(size).operations(size).add().search().build().generateTest();
			//Roughly static multiset with searches
			new TestGenerator.Builder().name("maintain-search" + " " + i).size(size).operations(size).add().remove().search().build().generateTest();
			//Shrinking multiset with searches
			new TestGenerator.Builder().name("decrease-search" + " " + i).size(size).operations(size).remove().search().build().generateTest();
			//Multiset with searches
			new TestGenerator.Builder().name("search" + " " + i).size(size).operations(size).search().build().generateTest();
			//General multiset
			new TestGenerator.Builder().name("random" + " " + i).size(size).operations(size).build().generateTest();
		}
	}
}
