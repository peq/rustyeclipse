package rustyeclipse.processes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CargoProcess extends RustToolProcess {

	private final Process proc;
	private final List<String> outputLines = new ArrayList<>();
	private final List<String> outputErrorLines = new ArrayList<>();

	public CargoProcess(List<String> args) {
		Runtime rt = Runtime.getRuntime();
		ArrayList<String> commands = new ArrayList<>();
		commands.add("cargo");
		commands.addAll(args);
		try {
			proc = rt.exec(commands.toArray(new String[0]));
			
			collectInputStream(proc.getInputStream(), outputLines);
			collectInputStream(proc.getErrorStream(), outputErrorLines);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void collectInputStream(InputStream inputStream, List<String> collectTo) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line=null;
		    while ( (line = br.readLine()) != null) {
		        System.out.println(line);
				collectTo.add(line);
		    }
		}
	}

}
