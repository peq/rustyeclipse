package rustyeclipse.core;

import java.util.regex.Pattern;

import org.eclipse.dltk.core.ScriptContentDescriber;

public class RustContentDescriber extends ScriptContentDescriber {

	
	public RustContentDescriber() {
	}

	@Override
	protected Pattern[] getHeaderPatterns() {
		// here would be something like Pattern.compile("^#!.*python.*", Pattern.MULTILINE)
		return new Pattern[] {};
	}

}
