package rustyeclipse.editor;

import java.util.Optional;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import rustyeclipse.editors.RustEditor;


public class ToggleCommentHandler extends AbstractHandler implements IHandler {


	@Override
	public @Nullable Object execute(@Nullable ExecutionEvent event) throws ExecutionException {
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
		if (!(editorPart instanceof RustEditor)) {
			return null;
		}
		RustEditor editor = (RustEditor) editorPart;
		TextSelection  sel = (TextSelection) editor.getSelectionProvider().getSelection();
		int startLine = sel.getStartLine();
		Optional<IDocument> docOpt = editor.getDocumentOpt();
		if (!docOpt.isPresent()) {
			return null;
		}
		IDocument doc = docOpt.get();
		
		try {
			int startOffset = doc.getLineOffset(startLine);
			int endOffset = sel.getOffset() + sel.getLength();
			int len = endOffset - startOffset;
			String text = doc.get(startOffset, len);
			
			
			if (text.matches("^(//(.*)(\\r?\\n|\\r))*//.*$")) {
				// remove comments
				text = text.substring(2).replaceAll("(\\r?\\n|\\r)//", "$1");
			} else {
				// add comments
				text = "//" + text.replaceAll("(\\r?\\n|\\r)", "$1//");
				
			}
			
			doc.replace(startOffset, len, text);
			// select new text
			editor.selectAndReveal(startOffset, text.length());
			
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}
