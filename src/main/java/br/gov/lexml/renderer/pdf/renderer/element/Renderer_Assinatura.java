
package br.gov.lexml.renderer.pdf.renderer.element;

import org.dom4j.Element;

import com.itextpdf.text.Paragraph;

import br.gov.lexml.renderer.pdf.ITextUtil;
import br.gov.lexml.renderer.pdf.renderer.base.Renderer_ProtoParagraph;

public class Renderer_Assinatura extends Renderer_ProtoParagraph {

    @Override
    protected void formatProtoParagraph(final Element el, final Paragraph p) {
        p.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
        p.setSpacingAfter(0);
        p.setIndentationLeft(ITextUtil.cm2point(9));
    }

}
