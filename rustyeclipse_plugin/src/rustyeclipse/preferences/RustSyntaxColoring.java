package rustyeclipse.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import rustyeclipse.core.RustConstants;
import rustyeclipse.core.RustCorePlugin;



/**
 * Preference page for syntax highlighting of the editor.
 */
public class RustSyntaxColoring extends PreferencePage implements IWorkbenchPreferencePage{

	private ColorFieldEditor color;
	
	private BooleanFieldEditor styleBold;
	private BooleanFieldEditor styleItalic;
	private BooleanFieldEditor styleUnderline;
	private BooleanFieldEditor styleStrikethrough;
	
	final String[] options = {
			RustConstants.SYNTAXCOLOR_KEYWORD,
			RustConstants.SYNTAXCOLOR_STRING, 
			RustConstants.SYNTAXCOLOR_ANNOTATION,
			RustConstants.SYNTAXCOLOR_COMMENT,
			RustConstants.SYNTAXCOLOR_DOCCOMMENT,
			RustConstants.SYNTAXCOLOR_JASSTYPE,
			RustConstants.SYNTAXCOLOR_TEXT,			
	};
	
	/**
	 * Selection listener handling selection events on the list of elements
	 * which can be highlighted differently
	 */
	private final class SomeSelectionListener implements SelectionListener {
		private final List list;

		private SomeSelectionListener(List list) {
			this.list = list;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			performOk();
			
			int i = list.getSelectionIndex();
			if (i >= 0 && i < options.length) {
				updateEditors(options[i]);
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(RustCorePlugin.getDefaultPreferenceStore());		
		setDescription("Choose colors of syntax highlighting here");
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		GridLayout gridLayout = new GridLayout(2, false);
				
		container.setLayout(gridLayout);
		
		//Initialize and populate list
		final List list = new List(container, SWT.NONE);
		list.setLayoutData(new GridData(SWT.FILL));
		populateList(list);
		list.setSelection(0);
		list.setSize(10, 20);
		list.addSelectionListener(new SomeSelectionListener(list));
		
		Composite selectorContainer = new Composite(container, SWT.NONE);
		selectorContainer.setLayout(new GridLayout(1, false));
		
		String selection = options[0];
		
		//Add color editor
		Composite colorContainer = new Composite(selectorContainer, SWT.NONE);
		color = new ColorFieldEditor(RustConstants.SYNTAXCOLOR_COLOR + selection, "Color", colorContainer);

		//Create and add style group
		Group styleContainer = new Group(selectorContainer, SWT.NONE);
		styleContainer.setText("Style");
		
		GridLayout gridLayout2 = new GridLayout(1, false);
		styleContainer.setLayout(gridLayout2);
		
		Composite styleBoldContainer          = new Composite(styleContainer, SWT.NONE);
		Composite styleItalicContainer        = new Composite(styleContainer, SWT.NONE);
		Composite styleUnderlineContainer     = new Composite(styleContainer, SWT.NONE);
		Composite styleStrikethroughContainer = new Composite(styleContainer, SWT.NONE);
		
		styleBold          = new BooleanFieldEditor(RustConstants.SYNTAXCOLOR_BOLD + selection, "Bold", styleBoldContainer);
		styleItalic        = new BooleanFieldEditor(RustConstants.SYNTAXCOLOR_ITALIC + selection, "Italic", styleItalicContainer);
		styleUnderline     = new BooleanFieldEditor(RustConstants.SYNTAXCOLOR_UNDERLINE + selection, "Underline", styleUnderlineContainer);
		styleStrikethrough = new BooleanFieldEditor(RustConstants.SYNTAXCOLOR_STRIKETHROUGH + selection, "Strike through", styleStrikethroughContainer);
		
		//Link editors with the default preferenceStore
		color.setPreferenceStore(RustCorePlugin.getDefaultPreferenceStore());
		styleBold.setPreferenceStore(RustCorePlugin.getDefaultPreferenceStore());
		styleItalic.setPreferenceStore(RustCorePlugin.getDefaultPreferenceStore());
		styleUnderline.setPreferenceStore(RustCorePlugin.getDefaultPreferenceStore());
		styleStrikethrough.setPreferenceStore(RustCorePlugin.getDefaultPreferenceStore());
		
		//Load values for keyword
		updateEditors(selection);
		return container;
	}

	/**
	 * Populates a given list with a predefined set of elements which
	 * can be highlighted differently
	 * @param list The list to be populated
	 */
	private void populateList(final List list) {
		if(list != null){
			for (String option : options) {
				list.add(option);
			}
		}
	}
	
	private void updateEditors(String s){
		//Link editors with new values of preferenceStore
		color.setPreferenceName(RustConstants.SYNTAXCOLOR_COLOR+s);
		styleBold.setPreferenceName(RustConstants.SYNTAXCOLOR_BOLD+s);
		styleItalic.setPreferenceName(RustConstants.SYNTAXCOLOR_ITALIC+s);
		styleUnderline.setPreferenceName(RustConstants.SYNTAXCOLOR_UNDERLINE+s);
		styleStrikethrough.setPreferenceName(RustConstants.SYNTAXCOLOR_STRIKETHROUGH+s);
		
		//Load currently stored values for those
		color.load();
		styleBold.load();
		styleItalic.load();
		styleUnderline.load();
		styleStrikethrough.load();
	}
	
	@Override
	protected void performDefaults() {		
		color.loadDefault();				
		styleBold.loadDefault();
		styleItalic.loadDefault();
		styleUnderline.loadDefault();
		styleStrikethrough.loadDefault();
		
		super.performDefaults();
	}
	
	@Override
	public boolean performOk() {
		color.store();		
		styleBold.store();
		styleItalic.store();
		styleUnderline.store();
		styleStrikethrough.store();
				
		return super.performOk();
	}
	
}
