
package br.gov.lexml.renderer.pdf;

public abstract class PDFConfigs {

    // Metadados
    public static final String METADATA_TITLE = "metadata.title";
    public static final String METADATA_SUBJECT = "metadata.subject";
    public static final String METADATA_KEYWORDS = "metadata.keywords";
    public static final String METADATA_CREATOR = "metadata.creator";
    public static final String METADATA_AUTHOR = "metadata.author";

    // Tamanho de página
    public static final String DOCUMENT_WIDTH = "document.width";
    public static final String DOCUMENT_HEIGHT = "document.height";

    // Margens
    public static final String DOCUMENT_MARGIN_LEFT = "document.margin.left";
    public static final String DOCUMENT_MARGIN_RIGHT = "document.margin.right";
    public static final String DOCUMENT_MARGIN_TOP = "document.margin.top";
    public static final String DOCUMENT_MARGIN_BOTTOM = "document.margin.bottom";

    // Espaçamento padrão
    public static final String PARAGRAPH_SPACING = "paragraph.spacing";

    // Decorators
    public static final String DECORATOR_CLASSES = "decorator.classes";

    // Fonte
    public static final String FONT_SIZE = "font.size";
    public static final String FONT_SIZE_EMENTA = "font.size.ementa";
    public static final String FONT_SIZE_EPIGRAFE = "font.size.epigrafe";
    
    // Permite sublinhado em links
    public static final String ALLOW_UNDERLINES = "font.underlines.permite";
	
    //Cria outline do documento
    public static final String ADD_OUTLINE = "outline.add";
    
}
