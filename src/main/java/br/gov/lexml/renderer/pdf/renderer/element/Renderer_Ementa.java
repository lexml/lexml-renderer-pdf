
package br.gov.lexml.renderer.pdf.renderer.element;

import org.dom4j.Element;

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

import br.gov.lexml.renderer.pdf.ITextUtil;
import br.gov.lexml.renderer.pdf.PDFConfigs;
import br.gov.lexml.renderer.pdf.renderer.base.Renderer_Paragraph;

public class Renderer_Ementa extends Renderer_Paragraph {

    @Override
    protected void formatParagraph(final Element el, final Paragraph p) throws Exception {
        p.setIndentationLeft(ITextUtil.cm2point(8.5f));
        p.setFont(ctx.getFont(Font.NORMAL, ctx.getInt(PDFConfigs.FONT_SIZE_EMENTA)));
        p.setSpacingBefore(ITextUtil.cm2point(1));
        p.setSpacingAfter(ITextUtil.cm2point(2));
    }

}
