package rustyeclipse.editor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import rustyeclipse.builder.SourcePos;
import rustyeclipse.editors.RustEditor;
import rustyeclipse.util.Utils;

public class RustHyperlinkDetector implements IHyperlinkDetector {

	private final RustEditor editor;

	public RustHyperlinkDetector(RustEditor editor) {
		this.editor = editor;
	}

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		int offset = region.getOffset();
		return getHyperlinks(offset);
	}

	public IHyperlink[] getHyperlinks(int offset) {
		boolean useMouse = true;
		return calculateHyperlinks(offset, useMouse);
	}

	public IHyperlink @Nullable[] calculateHyperlinks(int offset, boolean useMouse) {
		File tempFile = null;
		try {
	
			IEditorInput editorInput = editor.getEditorInput();
			if (!(editorInput instanceof FileEditorInput)) {
				return null;
			}
			FileEditorInput fileInput = (FileEditorInput) editorInput;
			IDocument document = editor.getDocumentProvider().getDocument(editorInput);
			int lineNr = document.getLineOfOffset(offset);
			int columnNr = offset - document.getLineOffset(lineNr);
			
			lineNr++;
			columnNr++;
			
			File file = fileInput.getFile().getLocation().toFile();
			tempFile = new File(file.getParent(), file.getName() + ".racertmp");
			
			String text = document.get();
			Files.write(text, tempFile, Charsets.UTF_8);
			
			List<String> command = new ArrayList<>();
			command.add("racer");
			command.add("find-definition");
			command.add(""+lineNr);
			command.add(""+columnNr);
			command.add(tempFile.getAbsolutePath());
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.environment().put("RUST_SRC_PATH", "~/work/rust/src");
			Process proc = pb.start();
			
			List<String> output = Utils.streamToList(proc.getInputStream());
			List<String> errors = Utils.streamToList(proc.getErrorStream());
			
			for (String msg : errors) {
				System.out.println("ERROR: " + msg);
			}
			List<RustHyperlink> hyperLinks = new ArrayList<>();
			for (String msg : output) {
				if (msg.startsWith("MATCH ")) {
					msg = msg.substring("MATCH ".length());
					String[] parts = msg.split(",");
					String matched = parts[0];
					int line = Integer.parseInt(parts[1]);
					int column = Integer.parseInt(parts[2]);
					String declFile = parts[3];
					String type = parts[4];
					String preview = parts[5];
					
					if (new File(declFile).equals(tempFile)) {
						declFile = file.getAbsolutePath();
					}
					
					line--;
					
					IProject project = editor.getProject();
					
					int start = Utils.findPosOfString(text, matched, offset);
					
					int end = start + matched.length() - 1;
					SourcePos declPos = new SourcePos(declFile, line, column, line, column+1);
					hyperLinks.add(new RustHyperlink(project, start, end, declPos));
				}
			}
			if (hyperLinks.isEmpty()) {
				return null;
			}
			return hyperLinks.toArray(new IHyperlink[0]);
		} catch (BadLocationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (tempFile != null) {
				tempFile.delete();
			}
		}
		return null;
	}

}
