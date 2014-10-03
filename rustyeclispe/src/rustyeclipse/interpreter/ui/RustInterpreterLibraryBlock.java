package rustyeclipse.interpreter.ui;

import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterLibraryBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.LibraryLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;

public class RustInterpreterLibraryBlock extends AbstractInterpreterLibraryBlock {
	
	public RustInterpreterLibraryBlock(AddScriptInterpreterDialog d) {
		super(d);
	}

	@Override
	protected IBaseLabelProvider getLabelProvider() {
		return new LibraryLabelProvider();
	}

}
