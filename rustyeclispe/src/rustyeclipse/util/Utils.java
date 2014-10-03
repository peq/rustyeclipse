package rustyeclipse.util;

import java.util.Arrays;

public class Utils {

	public static <T> T[] joinArrays(T[] a, T[] b) {
		T[] res = Arrays.copyOf(a, a.length+b.length);
		assert res != null;
		System.arraycopy(b, 0, res, a.length, b.length);
		return res;
	}

}
