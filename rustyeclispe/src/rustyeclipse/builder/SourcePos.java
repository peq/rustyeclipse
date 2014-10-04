package rustyeclipse.builder;

import org.eclipse.jdt.annotation.NonNull;

/**
 * @author peter
 *
 */
public class SourcePos {
	private final String file;
	private final int startLine;
	private final int startColumn;
	private final int endLine;
	private final int endColumn;
	public SourcePos(String file, int startLine, int startColumn, int endLine, int endColumn) {
		this.file = file;
		this.startLine = startLine;
		this.startColumn = startColumn;
		this.endLine = endLine;
		this.endColumn = endColumn;
	}
	
	public int getStartLine() {
		return startLine;
	}
	public int getStartColumn() {
		return startColumn;
	}
	public int getEndLine() {
		return endLine;
	}
	public int getEndColumn() {
		return endColumn;
	}
	public String getFile() {
		return file;
	}

	@Override
	public String toString() {
		return "SourcePos [file=" + file + ", startLine=" + startLine + ", startColumn=" + startColumn + ", endLine="
				+ endLine + ", endColumn=" + endColumn + "]";
	}
	
	

}
