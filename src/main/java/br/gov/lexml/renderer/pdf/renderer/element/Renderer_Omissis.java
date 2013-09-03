
package br.gov.lexml.renderer.pdf.renderer.element;

import org.dom4j.Element;

import com.itextpdf.text.Paragraph;

import br.gov.lexml.renderer.pdf.RendererPDFContext;
import br.gov.lexml.renderer.pdf.renderer.Renderer;
import br.gov.lexml.renderer.pdf.renderer.base.Renderer_Paragraph;

public class Renderer_Omissis extends Renderer_Paragraph {

    @Override
    protected void formatParagraph(final Element el, final Paragraph p) throws Exception {
        p.add(".....");
        ctx.addToOmissisList(p);
    }

    public static void renderOmissis(final RendererPDFContext ctx) throws Exception {
        Renderer rOmissis = new Renderer_Omissis();
        rOmissis.setContext(ctx);
        rOmissis.render(null);
        rOmissis.close();
    }
}
