package rustyeclipse.interpreter.launch;

import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.dltk.launching.AbstractScriptLaunchConfigurationDelegate;

public class RustLaunchConfigurationDelegate 
	extends AbstractScriptLaunchConfigurationDelegate 
	implements ILaunchConfigurationDelegate {

	@Override
	public String getLanguageId() {
		return "Rust";
	}

}
