import java.io.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class TestGenerator {
	private Integer multisetSize;
	private Integer nbOperations;
	private IntegerGenerator generator;
	private int[] data;

	public TestGenerator(int multisetSize, int nbOperations) {
		this.multisetSize = multisetSize;
		this.nbOperations = nbOperations;
		this.generator = new IntegerGenerator(0, multisetSize * 2, System.currentTimeMillis());
	}

	private void generateOperations(PrintWriter writer) throws IOException {
		String[] commands = {"A", "S", "RO", "RA"};

		int[] op = generator.sampleWithReplacement(nbOperations);
		for (int i = 0; i < op.length; i++) {
			writer.println(commands[op[i] % commands.length] + " " + data[i % data.length]);
		}
		writer.println("P");
	}

	private void generateMultiset(PrintWriter writer) throws IOException {
		data = generator.sampleWithReplacement(multisetSize);
		for (int i : data) {
			writer.println("A" + " " + i);
		}
	}

	private void removeDuplicateSearch(String name) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(name));
		Set<String> lines = new LinkedHashSet<>(multisetSize + nbOperations + 1);

		String line;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		reader.close();

		BufferedWriter writer = new BufferedWriter(new FileWriter(name));
		for (String unique : lines) {
			writer.write(unique);
			writer.newLine();
		}
		writer.close();
	}

	private void generateSequence(String name) throws IOException {
		new File(name).createNewFile();
		PrintWriter writer = new PrintWriter(new FileWriter(name), true);
		generateMultiset(writer);
		generateOperations(writer);
		removeDuplicateSearch(name);
	}

	private void generateOutput(String name) throws IOException {
		String input = name + ".in";
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

		generateSequence(name + ".in");
		generateOutput(name);
	}

	static public void main(String args[]) throws Exception {
		new TestGenerator(150, 300).generateTest();
	}
}
