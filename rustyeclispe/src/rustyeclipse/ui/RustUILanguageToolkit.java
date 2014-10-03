package rustyeclipse.ui;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.ui.AbstractDLTKUILanguageToolkit;
import org.eclipse.dltk.ui.IDLTKUILanguageToolkit;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.preference.IPreferenceStore;

import rustyeclipse.core.RustCorePlugin;
import rustyeclipse.core.RustLanguageToolkit;

public class RustUILanguageToolkit extends AbstractDLTKUILanguageToolkit implements IDLTKUILanguageToolkit {

	@Override
	public @Nullable IDLTKLanguageToolkit getCoreToolkit() {
		return RustLanguageToolkit.getDefault();
	}	

	@Override
	public @Nullable IPreferenceStore getPreferenceStore() {
		return RustCorePlugin.getDefault().getPreferenceStore();
	}

}
