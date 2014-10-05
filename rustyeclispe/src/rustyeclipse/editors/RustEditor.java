package rustyeclipse.editors;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.texteditor.StatusTextEditor;

import rustyeclipse.core.RustConstants;
import rustyeclipse.core.RustNature;

public class RustEditor extends TextEditor {

	private static final String EDITOR_SCOPE = "rustyeclipse.rusteditorscope";
	private ColorManager colorManager;

	public RustEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new RustEditorConfig(this, colorManager));
		setDocumentProvider(new RustDocumentProvider());
	}
	
	@Override
	public void init(@Nullable IEditorSite site, @Nullable IEditorInput input) throws PartInitException {
		super.init(site, input);
		
        IContextService cs = (IContextService)getSite().getService(IContextService.class);
        cs.activateContext(EDITOR_SCOPE);
	}
	
	@Override
	public void createPartControl(@Nullable Composite parent) {
		super.createPartControl(parent);
		RustNature.get(getProject(), true);
	}
	
	public @Nullable IProject getProject() {
		IFile file = getFile();
		if (file != null) {
			return file.getProject();
		}
		return null;
	}
	
	private @Nullable RustNature getNature() {
		
		IProject project = getProject();
		System.out.println("get nature: " + project);
		return RustNature.get(project);
	}
	
	public @Nullable IFile getFile() {
		if (getEditorInput() instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput = (IFileEditorInput) getEditorInput();
			return fileEditorInput.getFile();
		}
		return null;
	}
	
	@Override
	public void dispose() {
		System.out.println("DISPOSE ");
		colorManager.dispose();
		super.dispose();
	}


	public void refresh() {
		try {
			// TODO is there a nicer solution to refresh? :D
			doSetInput(getEditorInput());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void configureSourceViewerDecorationSupport (@Nullable SourceViewerDecorationSupport support) {
		super.configureSourceViewerDecorationSupport(support);
		IPreferenceStore store = getPreferenceStore();
		char[] matchChars = {'(', ')', '[', ']', '{', '}'}; //which brackets to match
		ICharacterPairMatcher matcher = new DefaultCharacterPairMatcher(matchChars ,
				IDocumentExtension3.DEFAULT_PARTITIONING);
		support.setCharacterPairMatcher(matcher);
		support.setMatchingCharacterPainterPreferenceKeys(RustConstants.EDITOR_MATCHING_BRACKETS,RustConstants. EDITOR_MATCHING_BRACKETS_COLOR);
		//Enable bracket highlighting in the preference store
		store.setDefault(RustConstants.EDITOR_MATCHING_BRACKETS, true);
		store.setDefault(RustConstants.EDITOR_MATCHING_BRACKETS_COLOR, RustConstants.DEFAULT_MATCHING_BRACKETS_COLOR);
	}

	public IDocument getDocument() {
		return getDocumentOpt()
				.orElseThrow(() -> new RuntimeException("No document found"));
	}
	
	public Optional<IDocument> getDocumentOpt() {
		IEditorInput ei = getEditorInput();
		IDocument doc = getDocumentProvider().getDocument(ei);
		return Optional.ofNullable(doc);
	}

	
	
}
