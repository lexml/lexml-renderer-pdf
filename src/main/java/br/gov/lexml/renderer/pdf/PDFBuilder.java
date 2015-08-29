
package br.gov.lexml.renderer.pdf;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import org.jaxen.SimpleNamespaceContext;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import br.gov.lexml.renderer.pdf.renderer.Renderer;
import br.gov.lexml.renderer.pdf.renderer.RendererFactory;
import br.gov.lexml.renderer.pdf.renderer.decorator.PDFDecoratorList;
import br.gov.lexml.renderer.pdf.renderer.font.GentiumFontFactory;

public class PDFBuilder {

    private final RendererPDFContext ctx;

    private final Element root;

    private PDFDecoratorList decorators;

    public PDFBuilder(final RendererPDFContext ctx, final Element root) throws Exception {
        this.ctx = ctx;
        this.root = root;
    }

    public void build() throws Exception {

        // Para resolução de namespace padrão via XPATH
        Map<String , String> map = new HashMap<String , String>();
        map.put("lexml", root.getNamespaceURI());
        ctx.setXpathNamespaceContext(new SimpleNamespaceContext(map));

        // seta LexmlFontFactory
        ctx.setFontFactory(new GentiumFontFactory(ctx.getBoolean(PDFConfigs.ALLOW_UNDERLINES)));

        // Setup da página
        Rectangle pageSize = new Rectangle(ctx.getPoints(PDFConfigs.DOCUMENT_WIDTH),
                                           ctx.getPoints(PDFConfigs.DOCUMENT_HEIGHT));

        Document doc = new Document(pageSize, ctx.getPoints(PDFConfigs.DOCUMENT_MARGIN_LEFT),
                                    ctx.getPoints(PDFConfigs.DOCUMENT_MARGIN_RIGHT),
                                    ctx.getPoints(PDFConfigs.DOCUMENT_MARGIN_TOP),
                                    ctx.getPoints(PDFConfigs.DOCUMENT_MARGIN_BOTTOM));

        ctx.setPdf(doc);

        OutputStream out = ctx.getOutputStream();

        PdfWriter pdfWriter = PdfWriter.getInstance(doc, ctx.getOutputStream());
        pdfWriter.setPageEvent(new PdfPageListener());
        if(ctx.getBoolean(PDFConfigs.ADD_OUTLINE)) {
        	//System.out.println("build: outline enabled");
        	//pdfWriter.setViewerPreferences(PdfWriter.PageModeUseOutlines);
        }

        ctx.setPdfWriter(pdfWriter);

        // Inicializa decoradores
        decorators = new PDFDecoratorList(ctx.getString(PDFConfigs.DECORATOR_CLASSES));
        decorators.init(ctx, root);

        // Necessário para PDF/A
        pdfWriter.setPDFXConformance(PdfWriter.PDFA1B);
        createMetadata(doc, root, pdfWriter);

        doc.open();

        // Fonte / Parágrafo padrão
        Paragraph p = ctx.createParagraph();
        Font f = ctx.getFont(Font.NORMAL);
        p.setFont(f);
        p.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
        p.setSpacingAfter(ctx.getPoints(PDFConfigs.PARAGRAPH_SPACING));
        ctx.pushProtoParagraph(p);

        decorators.beforeContent(ctx, root);

        render(root);

        ctx.popProtoParagraph();

        decorators.afterContent(ctx, root);

        ctx.flushPdf();
        
        ctx.finalizeDocument();

        doc.close();

        out.close();

    }

    private void render(final Element el) throws Exception {

        Renderer renderer = RendererFactory.createRenderer(el, ctx);

        boolean abreAspas = el.attributeValue("abreAspas") != null;
        boolean fechaAspas = el.attributeValue("fechaAspas") != null;
        String notaAlteracao = el.attributeValue("notaAlteracao");

        final String id = el.attributeValue("id");
        
        if(id != null) {
        	ctx.pushId(id);
        }
        
        if (abreAspas) {
            ctx.addToNextContainer(new Chunk("“"));
        }

        if (renderer == null) {
            renderSubElements(el);
        }
        else if (renderer.render(el) == Renderer.NAO_ACABOU) {
            if (renderer.isMixed()) {
                renderAllChildNodes(el);
            }
            else {
                renderSubElements(el);
            }
            renderer.close();
        }

        StringBuilder lastTail = new StringBuilder(12);
        if (fechaAspas) {
            lastTail.append("” ");
        }
        if (!StringUtils.isEmpty(notaAlteracao)) {
            lastTail.append('(');
            lastTail.append(notaAlteracao);
            lastTail.append(')');
        }
        if (lastTail.length() > 0) {
            ctx.addToLastAddedContainer(new Chunk(lastTail.toString()));
        }
        if(id != null) {
        	ctx.popId();
        }

    }

    private void renderSubElements(final Element el) throws Exception {
        for (Object elFilho : el.elements()) {
            render((Element) elFilho);
        }
    }

    private void renderAllChildNodes(final Element el) throws Exception {

        if (el.isTextOnly()) {
            renderTextToContainer(el.getText());
        }
        else {
            for (Iterator< ? > it = el.nodeIterator(); it.hasNext();) {
                Node nFilho = (Node) it.next();

                if (nFilho instanceof Text) {
                    renderTextToContainer(nFilho.getText());
                }
                else if (nFilho instanceof Element) {
                    render((Element) nFilho);
                }
            }
        }

    }

    private void renderTextToContainer(final String text) { 
    	String str = ctx.stringTransform(ITextUtil.normalizeSpaces(text));
		final Chunk c = new Chunk(str);
		c.setFont(ctx.getFont(Font.NORMAL));
		if (ctx.isMustAddAnchor()) {
			Anchor a = new Anchor(c);
			a.setFont(ctx.getFont(Font.NORMAL));
			a.setName(ctx.getCurrentId());
			ctx.addToContainer(a);
			ctx.setMustAddAnchor(false);
		} else {
			ctx.addToContainer(c);
		}
    }

    private void createMetadata(final Document doc, final Element root, final PdfWriter writer) throws Exception {

        // Title
        String metadata = ctx.getString(PDFConfigs.METADATA_TITLE);
        if (StringUtils.isEmpty(metadata)) {
            metadata = ctx.createXPath("//lexml:Epigrafe/lexml:p").stringValueOf(root);
            if (StringUtils.isEmpty(metadata)) {
                metadata = "";
            }
        }
        doc.addTitle(metadata);

        // Subject
        metadata = ctx.getString(PDFConfigs.METADATA_SUBJECT);
        if (!StringUtils.isEmpty(metadata)) {
            doc.addSubject(metadata);
        }

        // Keywords
        metadata = ctx.getString(PDFConfigs.METADATA_KEYWORDS);
        if (!StringUtils.isEmpty(metadata)) {
            doc.addKeywords(metadata);
        }

        // Creator
        metadata = ctx.getString(PDFConfigs.METADATA_CREATOR);
        if (!StringUtils.isEmpty(metadata)) {
            doc.addCreator(metadata);
        }

        // Author
        metadata = ctx.getString(PDFConfigs.METADATA_AUTHOR);
        if (!StringUtils.isEmpty(metadata)) {
            doc.addAuthor(metadata);
        }

        writer.createXmpMetadata();
    }

    public class PdfPageListener extends PdfPageEventHelper {

        @Override
        public void onEndPage(final PdfWriter writer, final Document document) {
            try {
                decorators.onEndPage(ctx, root);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

}
