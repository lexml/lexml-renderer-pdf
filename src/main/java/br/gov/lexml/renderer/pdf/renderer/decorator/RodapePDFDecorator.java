
package br.gov.lexml.renderer.pdf.renderer.decorator;

import com.itextpdf.text.pdf.PdfGState;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import br.gov.lexml.renderer.pdf.ITextUtil;
import br.gov.lexml.renderer.pdf.RendererPDFContext;

/**
 * Adiciona um rodapé a todas as páginas com texto à esquerda e à direita podendo apresentar o número da página.
 */
public class RodapePDFDecorator extends AbstractPDFDecorator {

    private static final String PAGE_NUMBER_MARKUP = "<numero_pagina>";

    private static final float SPACE_AFTER_LINE = 1;

    String textoEsquerda, textoDireita;
    float fontSize, y, left, right, yLine;
    BaseColor color;

    @Override
    public void init(final RendererPDFContext ctx, final Element root) throws Exception {
        textoEsquerda = ctx.getString("decorator.rodape.text_left");
        textoDireita = ctx.getString("decorator.rodape.text_right");

        Document doc = ctx.getPdf();

        fontSize = 12;
        y = doc.bottomMargin() + fontSize;
        yLine = y + fontSize + SPACE_AFTER_LINE;
        left = doc.leftMargin();
        right = doc.getPageSize().getWidth() - doc.rightMargin();

        doc.setMargins(doc.leftMargin(), doc.rightMargin(), doc.topMargin(), doc.bottomMargin()
            + ITextUtil.cm2point(1.2f));

        String fontColor = ctx.getString("decorator.rodape.font_color");
        color = null;
        if (StringUtils.isEmpty(fontColor)) {
            color = new BaseColor(0xff000000);
        }
        else {
            color = new BaseColor(0xff000000 | Integer.parseInt(fontColor, 16));
        }

    }

    @Override
    public void onEndPage(final RendererPDFContext ctx, final Element root) throws Exception {

        PdfWriter pdfWriter = ctx.getPdfWriter();

        String pageNumber = Integer.toString(pdfWriter.getPageNumber());

        PdfContentByte cb = pdfWriter.getDirectContent();

        // Linha
        PdfGState gstate = new PdfGState();
        gstate.setStrokeOpacity(1.0f);
        gstate.setFillOpacity(1.0f);
        cb.setGState(gstate);

        cb.moveTo(left, yLine);
        cb.lineTo(right, yLine);
        cb.setLineWidth(.1f);

        cb.stroke();

        // Texto
        Font f = new Font(ctx.getFont(Font.NORMAL));
        f.setColor(color);

        if (!StringUtils.isEmpty(textoEsquerda)) {
            Phrase p = new Phrase();
            p.setFont(f);
            p.add(textoEsquerda.replace(PAGE_NUMBER_MARKUP, pageNumber));
            PdfGState gstate2 = new PdfGState();
            gstate2.setFillOpacity(1.0f);
            gstate2.setStrokeOpacity(1.0f);
            cb.setGState(gstate2);
            ColumnText.showTextAligned(cb, com.itextpdf.text.Element.ALIGN_LEFT, p, left, y, 0);
        }

        if (!StringUtils.isEmpty(textoDireita)) {
            Phrase p = new Phrase();
            p.setFont(f);
            p.add(textoDireita.replace(PAGE_NUMBER_MARKUP, pageNumber));
            ColumnText.showTextAligned(cb, com.itextpdf.text.Element.ALIGN_RIGHT, p, right, y, 0);
        }

    }
}
