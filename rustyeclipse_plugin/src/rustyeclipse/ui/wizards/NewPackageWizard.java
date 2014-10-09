package rustyeclipse.ui.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import rustyeclipse.core.RustNature;

public class NewPackageWizard extends Wizard implements INewWizard {


	private IStructuredSelection selection;
	private WizardNewFileCreationPage mainPage;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		setWindowTitle("Create new Rust file");
	}

    @Override
    public void addPages() {
            IStructuredSelection projectSelectionFromModulePath = getProjectSelection();
            mainPage = new WizardNewFileCreationPage("Create new Rust file", projectSelectionFromModulePath);
            mainPage.setTitle("Create new Rust file");
            mainPage.setFileExtension("rs");
            mainPage.setDescription("Create a new Rust file");
            addPage(mainPage);
    }
	
	private IStructuredSelection getProjectSelection() {
		IStructuredSelection s = selection;
//		TODO
		return s;
	}


	@Override
	public boolean performFinish() {
        IFile file = mainPage.createNewFile();
        if (file != null) {
                    RustNature nature = RustNature.get(file.getProject(), true);
                    if (nature == null) {
                    	throw new RuntimeException("No Rust nature found.");
                    }
                    nature.open(file, e -> 0);
                	
//                    IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
//                    String code = ""; // TODO add some initial code to the file?
//					doc.set(code);
//					editor.setHighlightRange(code.length()-1, 0, true); 
                       
                return true;
        } else
                return false;
	}

}
