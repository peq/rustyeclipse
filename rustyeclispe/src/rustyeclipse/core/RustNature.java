package rustyeclipse.core;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.ScriptNature;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public class RustNature extends ScriptNature implements IProjectNature {
	public static final String RUST_NATURE = RustCorePlugin.PLUGIN_ID + ".nature";

	public static @Nullable RustNature get(@Nullable IProject p) {
		return get(p, false);
	}
	
	public static @Nullable RustNature get(final @Nullable IProject p, boolean askAddNature) {
		if (p == null) {
			return null;
		}
		try {
			IProjectNature nat = p.getNature(RUST_NATURE);
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
		natureIds.add(RUST_NATURE);
		desc.setNatureIds(natureIds.toArray(new String[0]));
		p.setDescription(desc, null);
	}

	public void clearAllMarkers() {
		// TODO Auto-generated method stub
	}

}
