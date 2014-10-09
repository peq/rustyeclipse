package rustyeclipse.editors.autocomplete;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Image;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import rustyeclipse.builder.SourcePos;
import rustyeclipse.editor.RustHyperlink;
import rustyeclipse.editors.RustEditor;
import rustyeclipse.ui.Icons;
import rustyeclipse.util.Utils;

public class RustContentAssistant implements IContentAssistProcessor {

	private RustEditor editor;

	public RustContentAssistant(RustEditor editor) {
		this.editor = editor;
	}

	@Override
	public ICompletionProposal @Nullable [] computeCompletionProposals(@Nullable ITextViewer viewer, int offset) {
		File tempFile = null;
		try {
			IDocument document = viewer.getDocument();
			int lineNr = document.getLineOfOffset(offset);
			int columnNr = offset - document.getLineOffset(lineNr);
			lineNr++;
			// columnNr++;

			File file = editor.getFile().getLocation().toFile();
			tempFile = new File(file.getParent(), file.getName() + ".racertmp");

			String text = document.get();
			Files.write(text, tempFile, Charsets.UTF_8);

			List<String> command = new ArrayList<>();
			command.add("racer");
			command.add("complete");
			command.add("" + lineNr);
			command.add("" + columnNr);
			command.add(tempFile.getAbsolutePath());
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.environment().put("RUST_SRC_PATH", "/home/peter/work/rust/src");
			Process proc = pb.start();

			List<String> output = Utils.streamToList(proc.getInputStream());
			List<String> errors = Utils.streamToList(proc.getErrorStream());

			for (String msg : errors) {
				System.out.println("ERROR: " + msg);
			}
			List<ICompletionProposal> completions = new ArrayList<>();
			int replacementOffset = 0;
			String alreadyEntered = "";
			System.out.println(command);
			for (String msg : output) {
				try {
					System.out.println(msg);
					if (msg.startsWith("PREFIX ")) {
						msg = msg.substring("PREFIX ".length());
						String[] parts = msg.split(",");
						int line = Integer.parseInt(parts[0]);
						int column = Integer.parseInt(parts[1]);
						if (parts.length > 2) {
							alreadyEntered = parts[2];
						}
						replacementOffset = offset-alreadyEntered.length(); 
						
						// correct
//						replacementOffset = Utils.findPosOfString(text, alreadyEntered, replacementOffset);

					} else if (msg.startsWith("MATCH ")) {
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
							// TODO remove duplicates
						}

						String replacementString = matched;
						int replacementLength = alreadyEntered.length();
						int cursorPosition = matched.length();
						Image image = null;
						String displayString = matched;
						IContextInformation contextInformation = null;
						String additionalProposalInfo = null;
						if (type.equals("Function")) {
							replacementString += "()";
							cursorPosition += 1;
							image = Icons.function;
						} else if (type.equals("Module")) {
							image = Icons.wpackage;
						} else if (type.equals("Struct")) {
							image = Icons.wclass;
						} else if (type.equals("Enum") || type.equals("EnumVariant")) {
							image = Icons.wenum;
						} else if (type.equals("Type") || type.equals("Trait")) {
							image = Icons.winterface; // TODO better icon?	
						} else {
							displayString = type + " " + displayString;
						}

						completions.add(new CompletionProposal(replacementString, replacementOffset, replacementLength,
								cursorPosition, image, displayString, contextInformation, additionalProposalInfo));
					}
				} catch (Throwable t) {
					//
					t.printStackTrace();
				}

			}

			if (!completions.isEmpty()) {
				return completions.toArray(new ICompletionProposal[0]);
			}
		} catch (IOException | BadLocationException e) {
			e.printStackTrace();
		} finally {
			if (tempFile != null) {
				tempFile.delete();
			}
		}
		return null;
	}

	@Override
	public IContextInformation[] computeContextInformation(@Nullable ITextViewer viewer, int offset) {
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '.', ':' };
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

}
