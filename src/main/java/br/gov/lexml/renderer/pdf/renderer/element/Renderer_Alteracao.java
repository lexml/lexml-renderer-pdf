
package br.gov.lexml.renderer.pdf.renderer.element;


import org.dom4j.Element;

import br.gov.lexml.renderer.pdf.ITextUtil;
import br.gov.lexml.renderer.pdf.renderer.base.Renderer_ProtoParagraph;

import com.itextpdf.text.Annotation;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;

public class Renderer_Alteracao extends Renderer_ProtoParagraph {

    @Override
    protected void formatProtoParagraph(final Element el, final Paragraph p) {
        p.setIndentationLeft(ITextUtil.cm2point(3));
        p.setFirstLineIndent(ITextUtil.cm2point(1));
        if(el.attributeValue("base") != null) {
        	try {
				ctx.addToPdf(new Annotation("Documento base",el.attributeValue("base")));
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

}
