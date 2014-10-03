package rustyeclipse.core;

import org.eclipse.dltk.core.AbstractLanguageToolkit;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.jdt.annotation.Nullable;

public class RustLanguageToolkit extends AbstractLanguageToolkit implements IDLTKLanguageToolkit {

	private static @Nullable RustLanguageToolkit toolkit;
	 
	public static RustLanguageToolkit getDefault() {
		RustLanguageToolkit t = toolkit;
		if (t == null) {
			toolkit = t = new RustLanguageToolkit();
		}
		return t;
	}
	
	@Override
	public String getLanguageContentType() {
		return "rustyeclipse.content-type";
	}

	@Override
	public String getLanguageName() {
		return "Rust";
	}

	@Override
	public String getNatureId() {
		return RustNature.RUST_NATURE;
	}

}
