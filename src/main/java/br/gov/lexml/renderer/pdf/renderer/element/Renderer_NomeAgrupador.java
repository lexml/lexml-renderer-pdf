
package br.gov.lexml.renderer.pdf.renderer.element;

import org.dom4j.Element;

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

import br.gov.lexml.renderer.pdf.renderer.base.Renderer_Paragraph;

public class Renderer_NomeAgrupador extends Renderer_Paragraph {

    @Override
    protected void formatParagraph(final Element el, final Paragraph p) throws Exception {
        p.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        String parentName = el.getParent().getName();
        if (parentName.equals("Secao") || parentName.equals("Subsecao")) {
            p.setFont(ctx.getFont(Font.BOLD));
        }
    }

}
