package rustyeclipse.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.Nullable;

import rustyeclipse.core.RustNature;
import rustyeclipse.processes.CargoProcess;

public class RustBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "rustyeclipse.RustBuilder";
	public static final String MARKER_TYPE = "rustyeclipse.rustProblem";

	@Override
	protected IProject @Nullable[] build(int kind, Map<String, String> args, @Nullable IProgressMonitor monitor) throws CoreException {
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
		
		// check for changes to rust or toml files
		boolean[] rustChanged = {false};
		IResourceDelta delta = getDelta(project);
		if (delta == null) {
			System.out.println("delta == null");
			return null;
		}
		delta.accept((IResourceDelta d) -> {
				IResource resource = d.getResource();
				if (!(resource instanceof IFile)) {
					return true;
				}
				IFile file = (IFile) resource;
				if (isRustRelatedFile(file)) {
					rustChanged[0] = true;
					return false;
				}
				return true;
		});
		if (!rustChanged[0]) {
			return null;
		}
		
		List<String> cargoArgs = new ArrayList<>();
		cargoArgs.add("build");
		CargoProcess proc = new CargoProcess(project.getLocation().toFile(), cargoArgs);
		// TODO add error markers
		nature.clearAllMarkers();
		processCompilerOutput(project, proc.getOutputErrorLines());
		
		return null;
	}

	private void processCompilerOutput(IProject project, List<String> outputLines) {
		Pattern p = Pattern.compile("^(.*):([0-9]+):([0-9]+):\\s+([0-9]+):([0-9]+)\\s+error:(.*)");
		String projectPath = project.getLocation().toOSString();
		for (String line : outputLines) {
			Matcher m = p.matcher(line);
			if (m.find()) {
			    String file = m.group(1);
			    int startLine = Integer.parseInt(m.group(2));
				int startColumn = Integer.parseInt(m.group(3));
				int endLine = Integer.parseInt(m.group(4));
				int endColumn = Integer.parseInt(m.group(5));
			    String message = m.group(6);
			    file = file.replaceFirst(projectPath, "");
			    IFile ifile = project.getFile(file);
			    if (ifile != null) {
			    	SourcePos position = new SourcePos(file, startLine-1, startColumn-1, endLine-1, endColumn-1);
			    	CompileError err = new CompileError(position, message, ErrorType.ERROR);
					RustNature.addErrorMarker(ifile, err, MARKER_TYPE);
			    }
			}
		}
		
		
	}

	private boolean isRustRelatedFile(IFile file) {
		return file.getName().endsWith(".rs")
				|| file.getName().endsWith(".toml");
	}
	
	@Override
	protected void clean(@Nullable IProgressMonitor monitor) throws CoreException {
		super.clean(monitor);
		RustNature nature = RustNature.get(getProject());
		if (nature != null) {
			nature.clearAllMarkers();
		}
		// TODO call cargo clean
	}

	public static boolean isRustMarker(IMarker marker) {
		try {
			return marker.isSubtypeOf(MARKER_TYPE);
		} catch (CoreException e) {
			return false;
		} 
	}
	
	
	
}
