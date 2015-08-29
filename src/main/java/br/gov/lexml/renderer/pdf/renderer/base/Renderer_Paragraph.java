
package br.gov.lexml.renderer.pdf.renderer.base;

import org.dom4j.Element;

import com.itextpdf.text.Paragraph;

import br.gov.lexml.renderer.pdf.renderer.Renderer;

public abstract class Renderer_Paragraph extends Renderer_inline {

    @Override
    public boolean render(final Element el) throws Exception {

        Paragraph p = ctx.createParagraph();
        ctx.pushContainer(p);

        formatParagraph(el, p);

        return Renderer.NAO_ACABOU;
    }

    @Override
    public void close() throws Exception {
        Paragraph p = (Paragraph) ctx.popContainer();
        addToPDF(p);
    }

    protected abstract void formatParagraph(Element el, Paragraph p) throws Exception;

}
