package rustyeclipse.editor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import rustyeclipse.editors.RustEditor;

public class JumpToDeclHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
		if (!(editorPart instanceof RustEditor)) {
			return null;
		}
		RustEditor editor = (RustEditor) editorPart;
		TextSelection  sel = (TextSelection) editor.getSelectionProvider().getSelection();
		boolean useMouse = false;
		IHyperlink[] links = new RustHyperlinkDetector(editor).calculateHyperlinks(sel.getOffset(), useMouse); 
		if (links != null && links.length > 0) {
			links[0].open();
		}
		return null;
	}


}
