package rustyeclipse.editor;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;

import rustyeclipse.builder.SourcePos;
import rustyeclipse.core.RustNature;

public class RustHyperlink implements IHyperlink {

	private final IProject project;
	private final int startOffset;
	private final int endOffset;
	private final SourcePos declPos;

	public RustHyperlink(IProject project, int start, int end, SourcePos declPos) {
		this.project = project;
		this.startOffset = start;
		this.endOffset = end;
		this.declPos = declPos;
	}

	@Override
	public IRegion getHyperlinkRegion() {
		return new Region(startOffset, endOffset+1-startOffset);
	}

	@Override
	public String getTypeLabel() {
		return null;
	}

	@Override
	public String getHyperlinkText() {
		return null;
	}

	@Override
	public void open() {
		System.out.println("OPENING " + declPos);
		RustNature.open(project, declPos);

	}

}
