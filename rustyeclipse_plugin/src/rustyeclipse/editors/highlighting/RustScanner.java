package rustyeclipse.editors.highlighting;

import org.eclipse.jface.text.rules.ITokenScanner;

public interface RustScanner extends ITokenScanner {
	String getPartitionType();

}
