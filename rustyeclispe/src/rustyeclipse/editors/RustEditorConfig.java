package rustyeclipse.editors;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.widgets.Shell;

import rustyeclipse.core.RustConstants;
import rustyeclipse.core.RustCorePlugin;
import rustyeclipse.editor.RustHyperlinkDetector;
import rustyeclipse.editors.autocomplete.RustContentAssistant;
import rustyeclipse.ui.RustTextHover;
import rustyeclipse.util.Utils;

public class RustEditorConfig extends SourceViewerConfiguration {
	private final ColorManager colorManager;
	private final RustEditor editor;
	private @Nullable ContentAssistant assistant;

	public RustEditorConfig(RustEditor editor, ColorManager colorManager) {
		this.editor = editor;
		this.colorManager = colorManager;
	}

	@Override
	public String[] getConfiguredContentTypes(@Nullable ISourceViewer sourceViewer) {
		return Utils.joinArrays(new String[] { IDocument.DEFAULT_CONTENT_TYPE }, RustPartitionScanner.PARTITION_TYPES);
	}

	@Override
	public String getConfiguredDocumentPartitioning(@Nullable ISourceViewer sourceViewer) {
		return RustConstants.RUST_PARTITIONING;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(@Nullable ISourceViewer sourceViewer) {
		RustCorePlugin plugin = RustCorePlugin.getDefault();
		PresentationReconciler reconciler = new PresentationReconciler();
		// reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		reconciler.setDocumentPartitioning(RustConstants.RUST_PARTITIONING);

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(plugin.scanners().rustCodeScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(plugin.scanners().rustCodeScanner());
		reconciler.setDamager(dr, RustPartitionScanner.RUST_OTHER);
		reconciler.setRepairer(dr, RustPartitionScanner.RUST_OTHER);

		dr = new DefaultDamagerRepairer(plugin.scanners().hotDocScanner());
		reconciler.setDamager(dr, RustPartitionScanner.HOT_DOC);
		reconciler.setRepairer(dr, RustPartitionScanner.HOT_DOC);

		dr = new DefaultDamagerRepairer(plugin.scanners().multilineCommentScanner());
		reconciler.setDamager(dr, RustPartitionScanner.RUST_MULTILINE_COMMENT);
		reconciler.setRepairer(dr, RustPartitionScanner.RUST_MULTILINE_COMMENT);

		return reconciler;
	}

	@Override
	public IInformationControlCreator getInformationControlCreator(@Nullable ISourceViewer sourceViewer) {
		return new IInformationControlCreator() {
			public IInformationControl createInformationControl(@Nullable Shell parent) {
				// return new DefaultInformationControl(parent,new
				// HTMLTextPresenter(false));
				// return new BrowserInformationControl(parent, "sans", false);
				return new RustInformationControl(parent);
			}

		};
	}

	@Override
	public ITextHover getTextHover(@Nullable ISourceViewer sourceViewer, @Nullable String contentType) {
		return new RustTextHover(sourceViewer, editor);
	}

	@Override
	public IAnnotationHover getAnnotationHover(@Nullable ISourceViewer sourceViewer) {
		return new DefaultAnnotationHover();
	}

	@Override
	public IHyperlinkDetector[] getHyperlinkDetectors(@Nullable ISourceViewer sourceViewer) {
		return new IHyperlinkDetector[] { new RustHyperlinkDetector(editor) };
	}

	@Override
	public IAutoEditStrategy[] getAutoEditStrategies(@Nullable ISourceViewer sourceViewer, @Nullable String contentType) {
		return new IAutoEditStrategy[] { new RustAutoIndentStrategy() };
	}
	
	
	
	@Override
	public IContentAssistant getContentAssistant(@Nullable ISourceViewer sourceViewer) {
		ContentAssistant a = assistant;
		if (a == null) {
			assistant = a = new ContentAssistant();
			a.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
			a.setContentAssistProcessor(new RustContentAssistant(editor), IDocument.DEFAULT_CONTENT_TYPE);
			
			a.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
			a.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
	//		assistant.setContextInformationPopupBackground(...);
			a.setInformationControlCreator(getInformationControlCreator(sourceViewer));
			a.enableAutoInsert(true);
		}
		if (RustCorePlugin.config().autocompleteEnabled()) {
			a.enableAutoActivation(true);
			a.setAutoActivationDelay((int) (1000*RustCorePlugin.config().autocompleteDelay()));
		} else {
			a.enableAutoActivation(false);
		}
		return a;
	}

}