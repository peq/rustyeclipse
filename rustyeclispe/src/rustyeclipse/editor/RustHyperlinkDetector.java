package rustyeclipse.editor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.annotation.NonNull;
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

	public IHyperlink[] calculateHyperlinks(int offset, boolean useMouse) {
		try {
			System.out.println("Get hyperlinks at offset " + offset);
	//		$ export RUST_SRC_PATH=~/work/rust/src
	//		$ ~/work/racer/bin/racer find-definition 12 19 src/main.rs 
	//		MATCH return_two,6,3,src/main.rs,Function,fn return_two() -> int {
	
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
			File tempFile = new File(file.getParent(), file.getName() + ".racertmp");
			
			String text = document.get();
			Files.write(text, tempFile, Charsets.UTF_8);
			
			System.out.println("	file " + file + ", line = " + lineNr + ":" + columnNr);
			
			List<String> command = new ArrayList<>();
			command.add("racer");
			command.add("find-definition");
			command.add(""+lineNr);
			command.add(""+columnNr);
			command.add(tempFile.getAbsolutePath());
			System.out.println("command = " + command);
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.environment().put("RUST_SRC_PATH", "~/work/rust/src");
			Process proc = pb.start();
			
			List<String> output = Utils.streamToList(proc.getInputStream());
			List<String> errors = Utils.streamToList(proc.getErrorStream());
			
			for (String msg : errors) {
				System.out.println("ERROR: " + msg);
			}
			for (String msg : output) {
				System.out.println(msg);
			}
		
		} catch (BadLocationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
