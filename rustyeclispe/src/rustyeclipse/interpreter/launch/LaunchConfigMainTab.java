package rustyeclipse.interpreter.launch;

import org.eclipse.dltk.debug.ui.launchConfigurations.MainLaunchConfigurationTab;

import rustyeclipse.core.RustNature;

public class LaunchConfigMainTab extends MainLaunchConfigurationTab {

	public LaunchConfigMainTab(String mode) {
		super(mode);
	}

	@Override
	public String getNatureID() {
		return RustNature.RUST_NATURE;
	}


}
