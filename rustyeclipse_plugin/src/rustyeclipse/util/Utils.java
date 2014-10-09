package rustyeclipse.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

public class Utils {

	public static <T> T[] joinArrays(T[] a, T[] b) {
		T[] res = Arrays.copyOf(a, a.length+b.length);
		assert res != null;
		System.arraycopy(b, 0, res, a.length, b.length);
		return res;
	}

	public static String join(List<?> items, String delimiter) {
		return items.stream().map(Object::toString).collect(Collectors.joining(delimiter));
	}

	@SuppressWarnings("null")
	public static String escapeHtml(String s) {
		// TODO could use apache commons library?
		s = s.replace("&", "&amp;");
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		return s;
	}

	
	private static void collectInputStream(InputStream inputStream, List<String> collectTo) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line=null;
		    while ( (line = br.readLine()) != null) {
		        System.out.println(line);
				collectTo.add(line);
		    }
		}
	}
	
	public static List<String> streamToList(InputStream inputStream) throws IOException {
		List<String> result = new ArrayList<>();
		collectInputStream(inputStream, result);
		return result;
	}

	public static int findPosOfString(String text, String toFind, int offset) {
		for (int i=offset; i>=0; i--) {
			if (startsWithAt(text, toFind, i)) {
				return i;
			}
		}
		return offset;
	}

	private static boolean startsWithAt(String text, String toFind, int offset) {
		for (int i=0; i<toFind.length(); i++) {
			if (offset+i>=text.length() || text.charAt(offset+i) != toFind.charAt(i)) {
				return false;
			}
		}
		return true;
	}
}
