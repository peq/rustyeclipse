package rustyeclipse.launch;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;

import rustyeclipse.editors.RustEditor;

import com.google.common.base.Preconditions;

public class Shortcut implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof TreeSelection) {
			TreeSelection sel = (TreeSelection) selection;
			Object elem = sel.getFirstElement();
			if (elem instanceof IFile) {
				IFile file = (IFile) elem;
				launchFile(file);
				return;
			} else if (elem instanceof IProject) {
				launchProject((IProject) elem);
			}
		}
		throw new Error("Cannot launch selection.");

	}


	private void launchProject(IProject project) {
		Preconditions.checkNotNull(project);
		try {
            String launchName = getLaunchManager().generateLaunchConfigurationName(project.getName());
            
            ILaunchConfigurationWorkingCopy launchConfig =
                    getLaunchConfigType().newInstance(null, launchName);

            launchConfig.setAttribute(LaunchConstants.PROJECT_NAME, project.getName());

            boolean exists = false;
            for (ILaunchConfiguration cfg : getLaunchManager().getLaunchConfigurations(getLaunchConfigType())) {
				if (cfg.getAttribute(LaunchConstants.PROJECT_NAME, "").equals(project.getName())) {
					exists = true;
				}
			}
            if (!exists) {
            	launchConfig.doSave();
            }
            DebugUITools.launch(launchConfig, "run");
        } catch (Exception e) {
        	throw new Error(e);
        }
	}


	private void launchFile(IFile file) {
		Preconditions.checkNotNull(file);
		launchProject(file.getProject());
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		if (editor instanceof RustEditor) {
			RustEditor re = (RustEditor) editor;
			launchFile(re.getFile());
		}
	}
	
	public static ILaunchConfigurationType getLaunchConfigType() {
        return getLaunchManager().getLaunchConfigurationType(LaunchConstants.LAUNCH_CONFIG_ID);
    }

    public static ILaunchManager getLaunchManager() {
        return DebugPlugin.getDefault().getLaunchManager();
    }

}
