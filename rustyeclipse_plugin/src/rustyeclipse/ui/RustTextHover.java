package rustyeclipse.ui;

import java.util.Iterator;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.ISourceViewerExtension2;
import org.eclipse.ui.texteditor.SimpleMarkerAnnotation;

import rustyeclipse.builder.RustBuilder;
import rustyeclipse.editors.RustEditor;
import rustyeclipse.util.Utils;


public class RustTextHover implements ITextHover,
//		ITextHoverExtension,
		ITextHoverExtension2 {

	private ISourceViewer sourceViewer;
	private RustEditor editor;

	public RustTextHover(ISourceViewer sourceViewer, RustEditor editor) {
		this.sourceViewer = sourceViewer;
		this.editor = editor;
	}

	@Override
	public @Nullable Object getHoverInfo2(@Nullable ITextViewer textViewer, @Nullable IRegion hoverRegion) {
		String annotationHover = getAnnotationHover(hoverRegion);
		if (annotationHover != null) {
			return annotationHover;
		}
		
		
		return null;
	}


	private @Nullable String getAnnotationHover(IRegion hoverRegion) {
		IAnnotationModel model= getAnnotationModel(sourceViewer);
		if (model == null)
			return null;

		Iterator<?> e= model.getAnnotationIterator();
		while (e.hasNext()) {
			Annotation a= (Annotation) e.next();
			if (isIncluded(a)) {
				Position p= model.getPosition(a);
				if (p != null && p.overlapsWith(hoverRegion.getOffset(), hoverRegion.getLength())) {
					String msg= a.getText();
					if (msg != null && msg.trim().length() > 0)
						return Utils.escapeHtml(msg);
				}
			}
		}
		return null;
	}

	private boolean isIncluded(Annotation annotation) {
		if(annotation instanceof SimpleMarkerAnnotation){
			SimpleMarkerAnnotation markerannotation = (SimpleMarkerAnnotation)annotation;
			return markerannotation.getMarker().exists() 
					&& RustBuilder.isRustMarker(markerannotation.getMarker());
		}
		return false;
	}

	private @Nullable IAnnotationModel getAnnotationModel(ISourceViewer viewer) {
		if (viewer instanceof ISourceViewerExtension2) {
			ISourceViewerExtension2 extension= (ISourceViewerExtension2) viewer;
			return extension.getVisualAnnotationModel();
		}
		return viewer.getAnnotationModel();
	}
	
	@Override
	public @Nullable String getHoverInfo(@Nullable ITextViewer textViewer, @Nullable IRegion hoverRegion) {
		return getHoverInfo2(textViewer, hoverRegion).toString();
	}

	@Override
	public IRegion getHoverRegion(@Nullable ITextViewer textViewer, int offset) {
		// TODO smarter region
		return new Region(offset,0);
	}

}
