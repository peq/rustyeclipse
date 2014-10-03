package rustyeclipse.interpreter.ui;

import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterLibraryBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.IAddInterpreterDialogRequestor;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Shell;

public class RustInterpreterDialog extends AddScriptInterpreterDialog {

	public RustInterpreterDialog(IAddInterpreterDialogRequestor requestor, Shell shell, IInterpreterInstallType[] interpreterInstallTypes, IInterpreterInstall editedInterpreter) {
		super(requestor, shell, interpreterInstallTypes, editedInterpreter);
	}
	
	@Override
	protected AbstractInterpreterLibraryBlock createLibraryBlock(@Nullable AddScriptInterpreterDialog dialog) {
		return new RustInterpreterLibraryBlock(dialog);
	}

}
