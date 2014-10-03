package rustyeclipse.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

import rustyeclipse.core.RustNature;

public class RustEditor extends TextEditor {

	private ColorManager colorManager;

	public RustEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new RustConfiguration(colorManager));
		setDocumentProvider(new RustDocumentProvider());
		
		
	}
	
	@Override
	public void createPartControl(@Nullable Composite parent) {
		super.createPartControl(parent);
		RustNature.get(getProject(), true);
	}
	
	public @Nullable IProject getProject() {
		IFile file = getFile();
		if (file != null) {
			return file.getProject();
		}
		return null;
	}
	
	private @Nullable RustNature getNature() {
		
		IProject project = getProject();
		System.out.println("get nature: " + project);
		return RustNature.get(project);
	}
	
	public @Nullable IFile getFile() {
		if (getEditorInput() instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput = (IFileEditorInput) getEditorInput();
			return fileEditorInput.getFile();
		}
		return null;
	}
	
	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}


	public void refresh() {
		try {
			// TODO is there a nicer solution to refresh? :D
			doSetInput(getEditorInput());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

}
