
package br.gov.lexml.renderer.pdf.renderer.font;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class LexmlFontFactory {

    private final Map<Integer , BaseFont> baseFontMap = new HashMap<Integer , BaseFont>();

    private final Map<String , Font> fontCache = new HashMap<String , Font>();
    
    private final boolean allowUnderline;
        
    public LexmlFontFactory(final String normal, final String bold, final String italic, final String boldItalic,
    		final boolean allowUnderline)
        throws Exception {
    	
        addBaseFont(Font.NORMAL, normal);
        addBaseFont(Font.BOLD, bold);
        addBaseFont(Font.ITALIC, italic);
        addBaseFont(Font.BOLDITALIC, boldItalic);
        this.allowUnderline = allowUnderline;
    }

    private void addBaseFont(final int style, final String resourceName) throws Exception {    	
    	InputStream is = getClass().getResourceAsStream(resourceName);
    	byte[] fdata = IOUtils.toByteArray(is);
    	IOUtils.closeQuietly(is);
        BaseFont bf = BaseFont.createFont(resourceName, BaseFont.CP1252, BaseFont.EMBEDDED,true,fdata,null);
        baseFontMap.put(style, bf);
    }
    
    public abstract class FontBuilder {
    	public abstract Font createFont();
    }
    
    public Font getFont(String key,FontBuilder builder) {
    	Font f = fontCache.get(key);

        if (f == null) {
        	f = builder.createFont();
            
            fontCache.put(key, f);            
        }

        return f;
    }
        
    public Font getFont(int _style, final float size) {
    	final int style;
    	if(!allowUnderline) {
    		style = _style & (~(Font.UNDERLINE));
    	} else {
    		style = _style;
    	}
    	String key = style + "," + size;
    	return getFont(key, new FontBuilder() {
    		@Override
    		public Font createFont() {
    	    	int baseStyle = style & Font.BOLDITALIC;
    			BaseFont bf = baseFontMap.get(baseStyle);
    	        return new Font(bf, size, style);
    		}
    	});    	
    }

}
 