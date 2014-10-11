package rustyeclipse.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import rustyeclipse.core.RustConstants;
import rustyeclipse.core.RustCorePlugin;

public class RustPrefs {
	
	private IPreferenceStore pref;
	
	public RustPrefs(IPreferenceStore pref) {
		this.pref = pref;
	}
	
	public static RustPrefs get() {
		return new RustPrefs(RustCorePlugin.getDefaultPreferenceStore());
	}
	
	public String getRacerCommand() {
		return pref.getString(RustConstants.RUST_COMMAND_RACER);
	}

	public String getCargoCommand() {
		return pref.getString(RustConstants.RUST_COMMAND_CARGO);
	}

	public String getRustSrcPath() {
		return pref.getString(RustConstants.RUST_LIB_PATH);
	}

	public boolean autocompleteEnabled() {
		return pref.getBoolean(RustConstants.RUST_ENABLE_AUTOCOMPLETE);
	}

	public float autocompleteDelay() {
		return pref.getFloat(RustConstants.RUST_AUTOCOMPLETION_DELAY);
	}

}
