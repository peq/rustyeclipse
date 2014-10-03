package rustyeclipse.util;

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
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		return s;
	}

}
