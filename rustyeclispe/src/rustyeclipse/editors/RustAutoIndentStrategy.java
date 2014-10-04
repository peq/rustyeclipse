package rustyeclipse.editors;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;

public class RustAutoIndentStrategy extends DefaultIndentLineAutoEditStrategy implements IAutoEditStrategy {

	@Override
	public void customizeDocumentCommand(@Nullable IDocument document, @Nullable DocumentCommand command) {
		super.customizeDocumentCommand(document, command);
	}

}
