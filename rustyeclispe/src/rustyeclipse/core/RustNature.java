package rustyeclipse.core;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import rustyeclipse.builder.CompileError;
import rustyeclipse.builder.ErrorType;
import rustyeclipse.builder.RustBuilder;
import rustyeclipse.builder.SourcePos;
import rustyeclipse.editors.RustEditor;

public class RustNature implements IProjectNature {
	public static final String NATURE_ID = RustCorePlugin.PLUGIN_ID + ".nature";
	private @Nullable IProject project;

	public static @Nullable RustNature get(@Nullable IProject p) {
		return get(p, false);
	}
	
	public static @Nullable RustNature get(final @Nullable IProject p, boolean askAddNature) {
		if (p == null) {
			return null;
		}
		try {
			IProjectNature nat = p.getNature(NATURE_ID);
			if (nat instanceof RustNature) {
				return (RustNature) nat;
			} else if (askAddNature) {
				final boolean answer[] = new boolean[] {false};
				Display.getDefault().syncExec(() -> {
					answer[0] = MessageDialog.openQuestion(null, "No Wurst nature", "No Wurst nature was found for the project " + p.getName() + ".\n"
							+ "Do you want to add the Wurst nature?");
				});
				if (answer[0]) {
					addNatureToProject(p);
					return get(p);
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addNatureToProject(IProject  p) throws CoreException {
		IProjectDescription desc = p.getDescription();
		ArrayList<String> natureIds = new ArrayList<>(Arrays.asList(desc.getNatureIds()));
		natureIds.add(NATURE_ID);
		desc.setNatureIds(natureIds.toArray(new String[0]));
		p.setDescription(desc, null);
	}

	public void clearAllMarkers() {
		clearMarkers(RustBuilder.MARKER_TYPE);
	}
	
	public void clearMarkers(String markerType) {
		try {
			getProject().deleteMarkers(markerType, false, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
		}
	}

	@Override
	public void configure() throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();

		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(RustBuilder.BUILDER_ID)) {
				return;
			}
		}

		ICommand[] newCommands = new ICommand[commands.length + 1];
		System.arraycopy(commands, 0, newCommands, 0, commands.length);
		ICommand command = desc.newCommand();
		command.setBuilderName(RustBuilder.BUILDER_ID);
		newCommands[newCommands.length - 1] = command;
		desc.setBuildSpec(newCommands);
		project.setDescription(desc, null);
	}

	@Override
	public void deconfigure() throws CoreException {
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(RustBuilder.BUILDER_ID)) {
				ICommand[] newCommands = new ICommand[commands.length - 1];
				System.arraycopy(commands, 0, newCommands, 0, i);
				System.arraycopy(commands, i + 1, newCommands, i,
						commands.length - i - 1);
				description.setBuildSpec(newCommands);
				project.setDescription(description, null);			
				return;
			}
		}
	}

	@Override
	public @Nullable IProject getProject() {
		return project;
	}

	@Override
	public void setProject(@Nullable IProject project) {
		this.project = project;
	}
	public static void open(IProject p, String fileName, int offset) {
		RustNature nature = get(p);
		if (nature != null) {
			nature.open(fileName, offset);
		}
	}
	
	private @Nullable RustEditor open(String fileName, int offset) {
		IFile file = getProject().getFile(fileName);
		if (file.exists()) {
			RustEditor editor = open(file, offset);
			return editor;
		} else { // open external file
			IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(fileName));
			if (!fileStore.fetchInfo().isDirectory() && fileStore.fetchInfo().exists()) {
			    try {
			        IEditorPart editor = IDE.openEditorOnFileStore(getActiveWorkbenchPage(), fileStore);
			        if (editor instanceof RustEditor) {
						RustEditor wurstEditor = (RustEditor) editor;
						wurstEditor.setHighlightRange(offset, 0, true);
						return wurstEditor;
					}
			    } catch (PartInitException e) {
			    	e.printStackTrace();
			    }
			}
		}
		return null;
	}
	
	private static IWorkbenchPage getActiveWorkbenchPage() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}

	public @Nullable RustEditor open(IFile file, int offset) {
		try {
			IEditorPart editor = IDE.openEditor(getActiveWorkbenchPage(), file);
			if (editor instanceof RustEditor) {
				RustEditor wurstEditor = (RustEditor) editor;
				wurstEditor.setHighlightRange(offset, 0, true);
				return wurstEditor;
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void addErrorMarker(IFile file, CompileError e, String markerType) {
		try {
			IMarker marker = file.createMarker(markerType);
			marker.setAttribute(IMarker.MESSAGE, e.getMessage());
			if (e.getErrorType() == ErrorType.ERROR) {
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			} else {
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
			}

			SourcePos source = e.getPosition();
			marker.setAttribute(IMarker.LINE_NUMBER, source.getStartLine());
			
			marker.setAttribute(RustConstants.POS_START_LINE, source.getStartLine());
			marker.setAttribute(RustConstants.POS_START_COLUMN, source.getStartColumn());
			marker.setAttribute(RustConstants.POS_END_LINE, source.getEndLine());
			marker.setAttribute(RustConstants.POS_END_COLUMN, source.getEndColumn());
			
		} catch (CoreException ex) {
		}
		
	}
}
