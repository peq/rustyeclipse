package rustyeclipse.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import rustyeclipse.core.RustNature;
import rustyeclipse.processes.CargoProcess;

public class RustBuilder extends IncrementalProjectBuilder {

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		IProject project = getProject();
		System.out.println("builder invoked on project " + project);
		if (project == null) {
			throw new RuntimeException("no project given.");
		}
		RustNature nature = RustNature.get(project);
		if (nature == null) {
			System.out.println("Not a rust project, could not get nature");
			// not a rust project
			return null;
		}
		IFolder srcFolder = project.getFolder("src");
		if (srcFolder == null) {
			throw new RuntimeException("no src folder");
		}
		IFile mainFile = srcFolder.getFile("main.rs");
		if (mainFile == null) {
			throw new RuntimeException("no main.rs file");
		}
		
		List<String> cargoArgs = new ArrayList<>();
		cargoArgs.add("build");
		CargoProcess proc = new CargoProcess(cargoArgs);
		
		
		
		return null;
	}

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		super.clean(monitor);
		RustNature nature = RustNature.get(getProject());
		if (nature != null) {
			nature.clearAllMarkers();
		}
		// TODO call cargo clean
	}
	
	
	
}
