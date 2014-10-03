package rustyeclipse.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import rustyeclipse.core.RustConstants;
import rustyeclipse.core.RustCorePlugin;

public class RustDocumentProvider extends FileDocumentProvider {

	@Override
	protected @Nullable IDocument createDocument(@Nullable Object element) throws CoreException {
//		IDocument document = super.createDocument(element);
//		if (document != null) {
//			IDocumentPartitioner partitioner =
//				new FastPartitioner(
//					new RustPartitionScanner(),
//					new String[] {
//						RustPartitionScanner.RUST_MULTILINE_COMMENT });
//			partitioner.connect(document);
//			document.setDocumentPartitioner(partitioner);
//		}
//		return document;
		
		IDocument document = super.createDocument(element);
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3= (IDocumentExtension3) document;
			IDocumentPartitioner partitioner= new FastPartitioner(RustCorePlugin.getDefault().scanners().wurstPartitionScanner(), RustPartitionScanner.PARTITION_TYPES);
			extension3.setDocumentPartitioner(RustConstants.RUST_PARTITIONING, partitioner);
			partitioner.connect(document);
		}
		return document;
	}
}