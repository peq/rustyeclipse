package rustyeclipse.interpreter.ui;

import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.InterpretersBlock;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.jdt.annotation.Nullable;

import rustyeclipse.core.RustNature;

public class RustInterpretersBlock extends InterpretersBlock {

	@Override
	protected String getCurrentNature() {
		return RustNature.RUST_NATURE;
	}

	@Override
	protected AddScriptInterpreterDialog createInterpreterDialog(@Nullable IInterpreterInstall standin) {
		RustInterpreterDialog dialog = new RustInterpreterDialog(this, getShell(),
				ScriptRuntime.getInterpreterInstallTypes(getCurrentNature()), standin);
		return dialog;
	}

}
