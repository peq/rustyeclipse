package rustyeclipse.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import rustyeclipse.core.RustConstants;
import rustyeclipse.core.RustCorePlugin;
import rustyeclipse.util.Utils;

public class RustConfiguration extends SourceViewerConfiguration {
	private ColorManager colorManager;

	public RustConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return Utils.joinArrays(new String[] { IDocument.DEFAULT_CONTENT_TYPE }, RustPartitionScanner.PARTITION_TYPES);
	}
	
	@Override
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return RustConstants.RUST_PARTITIONING;
	}

	
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		RustCorePlugin plugin = RustCorePlugin.getDefault();
		PresentationReconciler reconciler= new PresentationReconciler();
//		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		reconciler.setDocumentPartitioning(RustConstants.RUST_PARTITIONING);

		DefaultDamagerRepairer dr= new DefaultDamagerRepairer(plugin.scanners().rustCodeScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		dr= new DefaultDamagerRepairer(plugin.scanners().rustCodeScanner());
		reconciler.setDamager(dr, RustPartitionScanner.RUST_OTHER);
		reconciler.setRepairer(dr, RustPartitionScanner.RUST_OTHER);

		dr= new DefaultDamagerRepairer(plugin.scanners().hotDocScanner());
		reconciler.setDamager(dr, RustPartitionScanner.HOT_DOC);
		reconciler.setRepairer(dr, RustPartitionScanner.HOT_DOC);

		dr= new DefaultDamagerRepairer(plugin.scanners().multilineCommentScanner());
		reconciler.setDamager(dr, RustPartitionScanner.RUST_MULTILINE_COMMENT);
		reconciler.setRepairer(dr, RustPartitionScanner.RUST_MULTILINE_COMMENT);

		return reconciler;
	}

}