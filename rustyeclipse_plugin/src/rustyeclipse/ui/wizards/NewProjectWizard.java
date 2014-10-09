package rustyeclipse.ui.wizards;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import rustyeclipse.core.RustNature;
import rustyeclipse.util.UtilityFunctions;

import com.google.common.base.Charsets;

public class NewProjectWizard extends Wizard implements INewWizard {

	private IWorkbench workbench;
	private WizardNewProjectCreationPage mainPage;

	private final String OVERWRITE_TITLE = "Overwriting project";
	private final String OVERWRITE_TEXT  = "A .project file exist in the given location" +
			" (maybe an existing project). Should the project be overwritten?";

	
	public NewProjectWizard() {
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		setWindowTitle("New Rust project");
	}

	@Override
	public void addPages() {
		mainPage = new WizardNewProjectCreationPage("New Rust project");
		mainPage.setDescription("Create a new Rust project");
		mainPage.setTitle("New Rust project");
	    addPage(mainPage);
	}
	
	@Override
	public boolean performFinish() {
		try{
			IProject project = mainPage.getProjectHandle();
			IProjectDescription desc;
			File projectFile;
			if(!mainPage.useDefaults()){
				projectFile = new File(mainPage.getLocationPath().toFile(), ".project");
				
				desc = ResourcesPlugin.getWorkspace().newProjectDescription(mainPage.getProjectName());
				desc.setLocation(mainPage.getLocationPath());
			} else {
				File projectDir = new File(mainPage.getLocationPath().toFile(), mainPage.getProjectName());
				projectFile = new File(projectDir, ".project");
				desc = ResourcesPlugin.getWorkspace().newProjectDescription(mainPage.getProjectName());
			}
			if(projectFile.exists()){
				boolean overwrite = MessageDialog.
					openQuestion(getShell(), OVERWRITE_TITLE,OVERWRITE_TEXT);
				if(overwrite)
					projectFile.delete();
				else
					return false;
			}
			project.create(desc, null);
			project.open(null);
			desc.setNatureIds(new String[]{RustNature.NATURE_ID});
			project.setDescription(desc, null);
			
			// TODO show perspective
			// workbench.showPerspective(RustPlugin.Rust_PERSPECTIVE_ID, workbench.getActiveWorkbenchWindow());
			// TODO create initial files with cargo
			createProjectSkeleton(projectFile.getParentFile(), project.getName());
			//createRustDependenciesFile(projectFile.getParentFile());
			project.refreshLocal(1, null);
			
			return true;
		}catch(CoreException ex){
			ex.printStackTrace();
			UtilityFunctions.showErrorMessage("Could not create new Project: \n"+ex.getLocalizedMessage());
		}
		return false;
	}

	private void createProjectSkeleton(File f, String projectName) {
		try {
			String cargoContents = "[package]\n\n"
					+ "name = \""+projectName+"\"\n"
							+ "version = \"0.0.1\"\n"
							+ "authors = []\n";
			File cargoFile = new File(f, "Cargo.toml");
			if (!cargoFile.exists()) {
				Files.write(cargoContents, cargoFile, Charsets.UTF_8);
			}
			
			
			
			String rs = "fn main() {\n\tprintln!(\"Hello World!\");\n}\n";
			File srcDir = new File(f, "src");
			if (!srcDir.exists()) {
				srcDir.mkdirs();
				File rsFile = new File(srcDir, "main.rs");
				Files.write(rs, rsFile, Charsets.UTF_8);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

//	private void createRustDependenciesFile(File f) {
//		try {
//			Files.write("C:\\<insert path to Rust here>\\RustScript\\Rustpack\\Rustscript\\lib\n", new File(f, "Rust.dependencies"), Charsets.UTF_8);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}

	
}
