package rustyeclipse.interpreter.launch;

import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.dltk.internal.debug.ui.launcher.AbstractScriptLaunchShortcut;

import rustyeclipse.core.RustNature;

public class Shortcut extends AbstractScriptLaunchShortcut {

	@Override
	protected ILaunchConfigurationType getConfigurationType() {
		return getLaunchManager().getLaunchConfigurationType(LaunchConstants.ID_RUST_SCRIPT);
	}

	@Override
	protected String getNatureId() {
		return RustNature.RUST_NATURE;
	}


}
