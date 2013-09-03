
package br.gov.lexml.renderer.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.jaxen.NamespaceContext;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.lexml.renderer.pdf.renderer.StringTransformer;
import br.gov.lexml.renderer.pdf.renderer.font.LexmlFontFactory;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

public class RendererPDFContext {

    private static final Logger log = LoggerFactory.getLogger(RendererPDFContext.class);

    private final Properties p = new Properties();

    private OutputStream out;

    private Document pdf;

    private PdfWriter pdfWriter;

    private final List<Paragraph> protoParagraphs = new LinkedList<Paragraph>();
    
    private final List<StringTransformer> stringTransformers = new LinkedList<StringTransformer>();

    private final List<List<Element>> containers = new LinkedList<List<Element>>();

    private Phrase contentToNextContainer;

    private final List<Element> pdfBuffer = new ArrayList<Element>();

    private List<Element> lastAddedContainer;

    private boolean failToLastAddedContainer;

    private LexmlFontFactory fontFactory;

    private final List<Paragraph> omissisList = new ArrayList<Paragraph>();

    private NamespaceContext xpathNamespaceContext;
    
    private final List<String> idStack = new LinkedList<String>();

	private boolean mustAddAnchor;

	private final String autoridade;
	
	private final String tipoNorma;
	
	private final void loadPropertiesFromResource(String resourceName) throws IOException {
		InputStream is = RendererPDFContext.class.getResourceAsStream(resourceName);
		if(is != null) {
			p.load(is);
			is.close();
		}
	}
	
    public RendererPDFContext(String autoridade, String tipoNorma) throws IOException {
    	this.autoridade = autoridade;
    	this.tipoNorma = tipoNorma;

    	loadPropertiesFromResource("/RendererPDF-default.properties");

        loadPropertiesFromResource("/RendererPDF.properties");
        
        loadPropertiesFromResource("/pdf-renderer-profiles/" + autoridade + "/profile.properties");
        
        loadPropertiesFromResource("/pdf-renderer-profiles/" + autoridade + "/" + tipoNorma + "/profile.properties");
        
    }

    public void addConfig(final Map<String , String> config) {
        if (config != null) {
            p.putAll(config);
        }
    }

    public String getString(final String key) {
        return (String) p.get(key);
    }

    public float getFloat(final String key) {
        return Float.parseFloat(getString(key));
    }

    public float getPoints(final String key) {
        return ITextUtil.cm2point(Float.parseFloat(getString(key)));
    }

    public int getInt(final String key, final int defaultValue) {
        String s = getString(key);
        return s == null ? defaultValue : Integer.parseInt(s);
    }

    public int getInt(final String key) {
        return getInt(key, -1);
    }

    public void setOutputStream(final OutputStream out) {
        this.out = out;
    }

    public OutputStream getOutputStream() {
        return out;
    }

    public void setPdf(final Document pdf) {
        this.pdf = pdf;
    }

    public Document getPdf() {
        return pdf;
    }

    public void setPdfWriter(final PdfWriter pdfWriter) {
        this.pdfWriter = pdfWriter;        
    }

    public PdfWriter getPdfWriter() {
        return pdfWriter;
    }

    public void setXpathNamespaceContext(final NamespaceContext xpathNamespaceContext) {
        this.xpathNamespaceContext = xpathNamespaceContext;
    }

    @SuppressWarnings("unchecked")
    public void addToPdf(final Element el) throws DocumentException {
        if (el instanceof List< ? >) {
            // Armazena último container para poder adicionar conteúdo posteriormente
            lastAddedContainer = (List<Element>) el;
            flushPdf();
        }
        pdfBuffer.add(el);        
    }

    public void flushPdf() throws DocumentException {
        for (Element el : pdfBuffer) {
            if (omissisList.contains(el)) {
                trataOmissis((Paragraph) el);
                omissisList.remove(p);
            }
            pdf.add(el);
        }
        pdfBuffer.clear();
    }

    public void pushProtoParagraph(final Paragraph protoParagraph) {
        protoParagraphs.add(0, protoParagraph);
    }

    public Paragraph popProtoParagraph() {
        return protoParagraphs.isEmpty() ? null : protoParagraphs.remove(0);
    }
    
    public void pushStringTransformer(StringTransformer st) {
    	stringTransformers.add(0, st);
    }

    public StringTransformer popStringTransformer() {
    	return stringTransformers.isEmpty()? null: stringTransformers.remove(0);
    }
    
    public Paragraph createParagraph() {
        if (protoParagraphs.isEmpty()) {
            return new Paragraph();
        }
        return (Paragraph) protoParagraphs.get(0).clone();
    }

    public void pushContainer(final List<Element> c) {
        containers.add(0, c);
        if (contentToNextContainer != null) {
            c.add(contentToNextContainer);
            contentToNextContainer = null;
        }
    }

    public List<Element> popContainer() {
        return containers.isEmpty() ? null : containers.remove(0);
    }

    public void addToContainer(final Element el) {
        List<Element> container = containers.isEmpty() ? null : containers.get(0);
        if (container != null) {
            container.add(el);
        }
        else if (failToLastAddedContainer) {
            addToLastAddedContainer(el);
        }
        else {
            addToNextContainer(el);
        }
    }

    public void addToNextContainer(final Element el) {
        if (contentToNextContainer == null) {
            contentToNextContainer = new Phrase();
        }
        contentToNextContainer.add(el);
    }

    public void addToLastAddedContainer(final Element el) {
        if (lastAddedContainer != null) {
            lastAddedContainer.add(el);
        }
        else {
            log.warn("Não havia container anterior para inclusão do elemento. Elemento ignorado.");
        }
    }

    public boolean isFailToLastAddedContainer() {
        return failToLastAddedContainer;
    }

    public void setFailToLastAddedContainer(final boolean failToLastAddedContainer) {
        this.failToLastAddedContainer = failToLastAddedContainer;
    }

    public void setFontFactory(final LexmlFontFactory fontFactory) {
        this.fontFactory = fontFactory;
    }

    public Font getFont(final int style, final float size) {
        return fontFactory.getFont(style, size);
    }

    public Font getFont(final int style) {
        return fontFactory.getFont(style, getFloat(PDFConfigs.FONT_SIZE));
    }

    public void addToOmissisList(final Paragraph p) {
        omissisList.add(p);
    }

    private void trataOmissis(final Paragraph p) {
        // p.add(".....");

        final String cincoPontos = ".....";

        float pageWidth = pdf.getPageSize().getWidth();

        float margins = getPoints(PDFConfigs.DOCUMENT_MARGIN_LEFT) + getPoints(PDFConfigs.DOCUMENT_MARGIN_RIGHT)
                        + p.getIndentationLeft() + p.getIndentationRight() + p.getFirstLineIndent();

        float lineWidth = pageWidth - margins;

        float currentLineWidth = 0;
        Chunk cOmissis = null;
        for (Chunk c : p.getChunks()) {
            currentLineWidth += c.getWidthPoint();
            if (c.getContent().contains(cincoPontos)) {
                cOmissis = c;
            }
        }

        if (currentLineWidth > lineWidth) {
            log.warn("Omissis com mais de uma linha não tratada.");
            return;
        }

        if (cOmissis == null) {
            log.warn("Chunk de omissis não identificado no parágrafo.");
            return;
        }

        Chunk c = new Chunk(".");
        c.setFont(cOmissis.getFont());
        int qtDot = (int) Math.floor((lineWidth - currentLineWidth) / c.getWidthPoint());

        cOmissis.append("").insert(cOmissis.getContent().indexOf(cincoPontos), StringUtils.repeat(".", qtDot));
    }

    public XPath createXPath(final String xpathExpression) throws Exception {
        XPath xpath = new Dom4jXPath(xpathExpression);
        xpath.setNamespaceContext(xpathNamespaceContext);
        return xpath;
    }

	public boolean getBoolean(String key,boolean defaultValue) {
		Object v = p.get(key);
		return v != null ? Boolean.valueOf(v.toString()) : defaultValue; 
	}
	
	public boolean getBoolean(String key) {
		return getBoolean(key,false); 
	}
	
	public void pushId(String id) {
		idStack.add(0,id);		
		mustAddAnchor = true;		
	}
				
	public boolean isMustAddAnchor() {
		return mustAddAnchor;
	}

	public void setMustAddAnchor(boolean mustAddAnchor) {
		this.mustAddAnchor = mustAddAnchor;
	}

	public String getCurrentId() {
		return idStack.isEmpty() ? null : idStack.get(0);
	}
	
	public String popId() {
		mustAddAnchor = false;
		return idStack.remove(0);
	}
	
	public String stringTransform(String str) {
		for(StringTransformer st: stringTransformers) {
			str = st.transform(str);
		}
		return str;
	}
	
	public void finalizeDocument() {
		
	}


}
