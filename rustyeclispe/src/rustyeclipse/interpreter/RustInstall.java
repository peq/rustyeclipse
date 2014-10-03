package rustyeclipse.interpreter;

import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.dltk.launching.AbstractInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.IInterpreterRunner;

import rustyeclipse.core.RustNature;

public class RustInstall extends AbstractInterpreterInstall implements IInterpreterInstall {

	public RustInstall(IInterpreterInstallType type, String id) {
		super(type, id);
	}
	
	@Override
	public String getNatureId() {
		return RustNature.RUST_NATURE;
	}
	
	@Override
	public IInterpreterRunner getInterpreterRunner(String mode) {
		final IInterpreterRunner runner = super.getInterpreterRunner(mode);

		if (runner != null) {
			return runner;
		}

		if (mode.equals(ILaunchManager.RUN_MODE)) {
			return new RustInterpreterRunner(this);
		}

		return null;
	}

}
