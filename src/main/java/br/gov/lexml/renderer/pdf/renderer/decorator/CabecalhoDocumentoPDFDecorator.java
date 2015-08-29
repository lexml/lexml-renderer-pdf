
package br.gov.lexml.renderer.pdf.renderer.decorator;

import java.io.FileNotFoundException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;

import br.gov.lexml.renderer.pdf.ITextUtil;
import br.gov.lexml.renderer.pdf.RendererPDFContext;

/**
 * Adiciona um cabeçalho padrão no início do documento com imagem, título e subtítulo centralizados.
 * <p>
 * <a href="CabecalhoPadraoDocumentoPDFDecorator.html"><i>Código Fonte</i></a>
 * </p>
 */
public class CabecalhoDocumentoPDFDecorator extends AbstractPDFDecorator {

    @Override
    public void beforeContent(final RendererPDFContext ctx, final Element root) throws Exception {

        String imageResourceName = ctx.getString("decorator.cabecalho_documento.image_resource");
        String title = ctx.getString("decorator.cabecalho_documento.title");
        String subtitle = ctx.getString("decorator.cabecalho_documento.subtitle");
        String fontColor = ctx.getString("decorator.cabecalho_documento.font_color");

        boolean hasImage = false;
        if (!StringUtils.isEmpty(imageResourceName)) {
            hasImage = true;
            URL resource = getClass().getResource(imageResourceName);
            if (resource == null) {
                throw new FileNotFoundException("Arquivo de recurso " + imageResourceName + " não encontrado.");
            }
            Image img = Image.getInstance(resource);
            img.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            img.scalePercent(30);
            ctx.addToPdf(img);
        }

        BaseColor color = null;
        if (StringUtils.isEmpty(fontColor)) {
            color = new BaseColor(0);
        }
        else {
            color = new BaseColor(Integer.parseInt(fontColor, 16));
        }

        if (!StringUtils.isEmpty(title)) {
            Paragraph p = ctx.createParagraph();
            Font f = new Font(ctx.getFont(Font.BOLD, 18));
            f.setColor(color);
            p.setFont(f);
            p.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            p.add(title);
            if (hasImage) {
                p.setSpacingBefore(ITextUtil.cm2point(.3f));
            }
            p.setSpacingAfter(0);
            ctx.addToPdf(p);
        }

        if (!StringUtils.isEmpty(subtitle)) {
            Paragraph p = ctx.createParagraph();
            Font f = new Font(ctx.getFont(Font.BOLD, 14));
            f.setColor(color);
            p.setFont(f);
            p.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            p.add(subtitle);
            p.setSpacingBefore(ITextUtil.cm2point(.1f));
            p.setSpacingAfter(ITextUtil.cm2point(1));
            ctx.addToPdf(p);
        }
        else {
            Paragraph p = ctx.createParagraph();
            p.setSpacingAfter(ITextUtil.cm2point(1));
            ctx.addToPdf(p);
        }

    }
}
