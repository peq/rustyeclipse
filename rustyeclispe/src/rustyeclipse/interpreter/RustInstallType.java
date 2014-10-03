package rustyeclipse.interpreter;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.internal.launching.AbstractInterpreterInstallType;
import org.eclipse.dltk.launching.EnvironmentVariable;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.LibraryLocation;
import org.osgi.framework.Bundle;

import rustyeclipse.core.RustCorePlugin;
import rustyeclipse.core.RustNature;

public class RustInstallType extends AbstractInterpreterInstallType {

	@Override
	public String getName() {
		return "Rust Interpreter Install Type";
	}

	@Override
	public String getNatureId() {
		return RustNature.RUST_NATURE;
	}
	
	@Override
	protected IPath createPathFile(IDeployment deployment) throws IOException {
		Bundle bundle = RustCorePlugin.getDefault().getBundle();
		// TODO
		return deployment.add(bundle, "scripts/path.rs");
	}
	
	@Override
	protected String retrivePaths(IExecutionEnvironment exeEnv, IFileHandle installLocation,
			List<LibraryLocation> locations, IProgressMonitor monitor, IFileHandle locator,
			EnvironmentVariable[] variables) {
		IPath path = Path.fromOSString("/home/peter/work/rust"); // TODO update path from somewhere
		path = EnvironmentPathUtils.getFullPath(exeEnv.getEnvironment(), path);
		if (! EnvironmentPathUtils.isFull(path)) {
			throw new RuntimeException("not full path: " + path);
		}
		locations.add(new LibraryLocation(path));
		return "/fake/path";
	}

	@Override
	protected IInterpreterInstall doCreateInterpreterInstall(String id) {
		return new RustInstall(this, id);
	}

	@Override
	protected ILog getLog() {
		return RustCorePlugin.getDefault().getLog();
	}

	@Override
	protected String getPluginId() {
		return RustCorePlugin.PLUGIN_ID;
	}

	@Override
	protected String[] getPossibleInterpreterNames() {
		return new String[] { "rustc" };
	}

}
