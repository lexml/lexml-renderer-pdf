package br.gov.lexml.renderer.pdf.renderer.element;

import org.dom4j.Element;

import br.gov.lexml.renderer.pdf.renderer.base.DecoratedPhrase;
import br.gov.lexml.renderer.pdf.renderer.base.LexmlAnchorDecorator;
import br.gov.lexml.renderer.pdf.renderer.base.Renderer_Phrase;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

public class Renderer_span extends Renderer_Phrase {

	@Override
	protected void formatPhrase(final Element el, final DecoratedPhrase p)
			throws Exception {
		// Negrito para identificar outra língua
		int style = 0;
		if (el.attribute("lang") != null) {
			style |= hasAncestor(el, "i") ? Font.BOLDITALIC : Font.BOLD;

		}
		if (el.attribute("style") != null) {
			String styleStr = el.attribute("style").getValue();
			if (styleStr.contains("font-style:italic"))
				style |= Font.ITALIC;
			if (styleStr.contains("font-weight:bold")
					|| styleStr.contains("font-weight:bolder"))
				style |= Font.BOLD;
		}
		Font f = ctx.getFont(style);		
		if (el.attribute("href") != null) {
			//System.out.println("element has href: " + el.attribute("href"));
			try {
				//style |= Font.UNDERLINE;
				f = new Font(f);
				f.setColor(BaseColor.GRAY);
				p.addDecorator(new LexmlAnchorDecorator(el.attribute("href")
						.getStringValue()));
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		} else {
			//System.out.println("element does not have href: " + el);
		}
		p.setFont(f);		
	}
}
