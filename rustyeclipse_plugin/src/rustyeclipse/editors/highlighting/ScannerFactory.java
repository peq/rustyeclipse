package rustyeclipse.editors.highlighting;


import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import rustyeclipse.core.RustConstants;
import rustyeclipse.editors.RustPartitionScanner;
import rustyeclipse.util.UtilityFunctions;


public class ScannerFactory {

	private IPreferenceStore preferencestore;

	public ScannerFactory() {
		preferencestore = UtilityFunctions.getDefaultPreferenceStore();
	}
	
	private TextAttribute getStyle(String key) {
		return new TextAttribute(
				new Color(Display.getCurrent(), 
						PreferenceConverter.getColor(preferencestore, RustConstants.SYNTAXCOLOR_COLOR+ key)), 
						null, 
						UtilityFunctions.computeAttributes(preferencestore, key));
	}

	public ITokenScanner rustCodeScanner() {
		return new SimpleCodeScanner();
	}

	public ITokenScanner hotDocScanner() {
		return new SingleTokenScanner(getStyle(RustConstants.SYNTAXCOLOR_DOCCOMMENT));
	}

	public ITokenScanner multilineCommentScanner() {
		return new SingleTokenScanner(getStyle(RustConstants.SYNTAXCOLOR_COMMENT));
	}

	public IPartitionTokenScanner rustPartitionScanner() {
		return new RustPartitionScanner();                         
	}

}
