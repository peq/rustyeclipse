package rustyeclipse.editors;

import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextUtilities;

public class RustAutoIndentStrategy extends DefaultIndentLineAutoEditStrategy implements IAutoEditStrategy {

	
	private void autoIndentAfterNewLine(IDocument d, DocumentCommand c) {

		if (c.offset == -1 || d.getLength() == 0)
			return;

		try {
			// find start of line
			final int p= (c.offset == d.getLength() ? c.offset  - 1 : c.offset);
			final IRegion lineInfo= d.getLineInformationOfOffset(p);
			final int start= lineInfo.getOffset();
			
			final String line = d.get(lineInfo.getOffset(), lineInfo.getLength());
			final int cursorPosInLine = Math.min(p - lineInfo.getOffset(), line.length());
			int startOfWord = start + cursorPosInLine;
			int spaces = 0;
			
			for (int i=0; i< cursorPosInLine; i++) {
				if (line.charAt(i) == ' ') {
					spaces++;
				} else if (line.charAt(i) == '\t') {
					spaces+=4;
				} else {
					startOfWord = start + i; 
					break;
				}
			}
			int indent;
			if (c.offset < lineInfo.getOffset() + lineInfo.getLength()) {
				// we are in the middle of a line
				indent = -1; // 
			} else {
				// we are at the end of a line
				indent = spaces/4; 
			
				
				String lineT = line.substring(startOfWord-start);
				if (lineT.endsWith("{")) {
					// increase indent after open curly brace
					indent++;
				} else {
					// indent of -1 means: use same indent
					indent=-1;
				}
			}
			boolean usingSpaces = line.length() > 0 && line.charAt(0) == ' ';
			StringBuffer buf= new StringBuffer(c.text);
			if (indent >= 0) {
				makeIndent(indent, usingSpaces, buf);
			} else {
				// use same indentation:
				if (startOfWord > start) {
					// append to input
					buf.append(d.get(start, startOfWord - start));
				}
			}
			c.text= buf.toString();
		} catch (BadLocationException excp) {
			// stop work
		}
	}

	private void makeIndent(int indent, boolean usingSpaces, StringBuffer buf) {
		for (int i=0; i<indent; i++) {
			buf.append(usingSpaces ? "    " : "\t");
		}
	}

	@Override
	public void customizeDocumentCommand(@Nullable IDocument d, @Nullable DocumentCommand c) {
		if (c==null || d == null) {
			return;
		}
		if (c.length == 0 && c.text != null) {
			if (TextUtilities.endsWith(d.getLegalLineDelimiters(), c.text) != -1) {
				autoIndentAfterNewLine(d, c);
			} else if (c.text.equals("}")) {
				adjustClosingBracePosition(d, c);
			}
		}
	}

	private void adjustClosingBracePosition(IDocument d, DocumentCommand c) {
		try {
			if (d.get(c.offset-1, 1).equals("\t")) { 
				c.offset -=1;
				c.length++;
			} else if (d.get(c.offset-4, 4).equals("    ")) {
				c.offset -=4;
				c.length +=4;
			}
		} catch (BadLocationException e) {
			// ignore, just do nothing
		}
	}

}
