
package br.gov.lexml.renderer.pdf.renderer.base;

import org.dom4j.Element;

import com.itextpdf.text.Paragraph;

import br.gov.lexml.renderer.pdf.renderer.Renderer;

public abstract class Renderer_ProtoParagraph extends AbstractRenderer {

    @Override
    public boolean render(final Element el) throws Exception {

        Paragraph p = ctx.createParagraph();

        formatProtoParagraph(el, p);

        ctx.pushProtoParagraph(p);

        return Renderer.NAO_ACABOU;
    }

    @Override
    public void close() throws Exception {
        ctx.popProtoParagraph();
    }

    protected abstract void formatProtoParagraph(Element el, Paragraph p) throws Exception;

}
