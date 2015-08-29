
package br.gov.lexml.renderer.pdf.renderer.element;

import org.dom4j.Element;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

import br.gov.lexml.renderer.pdf.ITextUtil;
import br.gov.lexml.renderer.pdf.PDFConfigs;
import br.gov.lexml.renderer.pdf.renderer.Renderer;
import br.gov.lexml.renderer.pdf.renderer.base.AbstractRenderer;

public class Renderer_Rotulo extends AbstractRenderer {

    @Override
    public boolean render(final Element el) throws Exception {

        String parentName = el.getParent().getName();
        if (parentName.equals("Capitulo") || parentName.equals("Titulo") || parentName.equals("Livro")
            || parentName.equals("Parte") || parentName.equals("Secao") || parentName.equals("Subsecao")) {

            Paragraph p = ctx.createParagraph();
            p.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);

            float paragraphSpacing = ITextUtil.cm2point(ctx.getFloat(PDFConfigs.PARAGRAPH_SPACING));
            p.setSpacingBefore(paragraphSpacing / 2);
            p.setSpacingAfter(paragraphSpacing / 2);

            ctx.pushContainer(p); // Necessário para abertura de aspas

            // Negrito
            if (parentName.equals("Secao") || parentName.equals("Subsecao")) {
                p.setFont(ctx.getFont(Font.BOLD));
            }

            addToContainer(el.getTextTrim());

            ctx.popContainer();
            addToPDF(p);
        }
        else {
            String texto = el.getTextTrim();
            if (parentName.equals("Artigo") || parentName.equals("Paragrafo")) {
                texto += "  "; // Dois espaços
            }
            else {
                texto += " ";
            }

            Chunk c = new Chunk(texto);

            if (texto.toLowerCase().contains("parágrafo único")) {
                c.setFont(ctx.getFont(Font.ITALIC));
            }
            else {
                c.setFont(ctx.getFont(Font.BOLD));
            }

            addToNextContainer(c);

            if (renderizarOmissis(el)) {
                Renderer_Omissis.renderOmissis(ctx);
            }
        }

        return Renderer.ACABOU;
    }

    private boolean renderizarOmissis(final Element el) {
        Element parent = el.getParent();
        if (parent.attribute("textoOmitido") != null) {
            return true;
        }
        if (parent.getName().equals("Artigo")) {
            Element caput = parent.element("Caput");
            return caput != null && caput.attribute("textoOmitido") != null;
        }
        return false;
    }
}
