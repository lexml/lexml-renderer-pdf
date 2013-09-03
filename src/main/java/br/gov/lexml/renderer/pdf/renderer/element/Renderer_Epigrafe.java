
package br.gov.lexml.renderer.pdf.renderer.element;

import org.dom4j.Element;

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

import br.gov.lexml.renderer.pdf.PDFConfigs;
import br.gov.lexml.renderer.pdf.renderer.StringTransformer;
import br.gov.lexml.renderer.pdf.renderer.base.Renderer_Paragraph;

public class Renderer_Epigrafe extends Renderer_Paragraph implements StringTransformer {

    @Override
    protected void formatParagraph(final Element el, final Paragraph p) throws Exception {
        p.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        p.setFont(ctx.getFont(Font.BOLD, ctx.getInt(PDFConfigs.FONT_SIZE_EPIGRAFE)));
        ctx.pushStringTransformer(this);
    }
    
    @Override
    public void close() throws Exception {
    	super.close();
    	ctx.popStringTransformer();
    }

	@Override
	public String transform(String str) {
		str = str.replace("LEXML_EPIGRAFE_NUMERO", "_______");
		str = str.replace("LEXML_EPIGRAFE_DATA", "_______");
		return str;
	}

}
