
package br.gov.lexml.renderer.pdf.renderer.base;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

import br.gov.lexml.renderer.pdf.ITextUtil;
import br.gov.lexml.renderer.pdf.RendererPDFContext;
import br.gov.lexml.renderer.pdf.renderer.Renderer;

public abstract class AbstractRenderer implements Renderer {

    protected RendererPDFContext ctx;

    // ---- Métodos do Renderer

    @Override
    public void close() throws Exception {
        // Não faz nada
    }

    @Override
    public boolean isMixed() {
        return false;
    }

    @Override
    public void setContext(final RendererPDFContext ctx) {
        this.ctx = ctx;
    }

    // ---- Utilitários

    protected void addToPDF(final Element el) throws Exception {
        ctx.addToPdf(el);
    }

    protected void addToContainer(final Element el) {
        ctx.addToContainer(el);
    }

    protected void addToContainer(final String text) {
        ctx.addToContainer(new Chunk(text));
    }

    protected void addToNextContainer(final Element el) {
        ctx.addToNextContainer(el);
    }

    protected void addToNextContainer(final String text) {
        ctx.addToNextContainer(new Chunk(text));
    }

    protected void addToLastAddedNextContainer(final Element el) {
        ctx.addToLastAddedContainer(el);
    }

    protected void addToLastAddedContainer(final String text) {
        ctx.addToLastAddedContainer(new Chunk(text));
    }

    protected void addVerticalSpaceCM(final float cm) throws Exception {
        Paragraph p = new Paragraph();
        p.setFont(ctx.getFont(Font.NORMAL));
        p.setSpacingAfter(ITextUtil.cm2point(cm));
        addToPDF(p);
    }

    public boolean hasAncestor(final org.dom4j.Element el, final String name) throws Exception {
        return ctx.createXPath("ancestor::lexml:" + name).selectSingleNode(el) != null;
    }

}
