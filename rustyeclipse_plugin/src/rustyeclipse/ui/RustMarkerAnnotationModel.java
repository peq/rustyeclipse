package rustyeclipse.ui;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Position;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;

import rustyeclipse.builder.RustBuilder;
import rustyeclipse.core.RustConstants;


public class RustMarkerAnnotationModel extends ResourceMarkerAnnotationModel {

	public RustMarkerAnnotationModel(@Nullable IResource resource) {
		super(resource);
	}

	@Override
	protected @Nullable Position createPositionFromMarker(@Nullable IMarker marker) {
		if(marker != null && RustBuilder.isRustMarker(marker)) {
			if (marker.getAttribute(RustConstants.POS_END_LINE, -1) == -1) {
				return null;
			}
			int endLine = marker.getAttribute(RustConstants.POS_END_LINE, -1);
			int endColumn = marker.getAttribute(RustConstants.POS_END_COLUMN, -1);
			int startLine = marker.getAttribute(RustConstants.POS_START_LINE, -1);
			int startColumn = marker.getAttribute(RustConstants.POS_START_COLUMN, -1);

			try {
				int start = fDocument.getLineOffset(startLine) + startColumn;
				int end = fDocument.getLineOffset(endLine) + endColumn;
				if (start >= 0 && end >= start) {
					return new Position(start, end-start);
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		return super.createPositionFromMarker(marker);
	}

}
