package rustyeclipse.interpreter.ui;

import org.eclipse.dltk.internal.debug.ui.interpreters.InterpretersBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.ScriptInterpreterPreferencePage;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class RustInterpreterPreferencePage extends ScriptInterpreterPreferencePage implements IWorkbenchPreferencePage {

	@Override
	public InterpretersBlock createInterpretersBlock() {
		return new RustInterpretersBlock();
	}


}
