package br.gov.lexml.renderer.pdf;

import java.io.FileOutputStream;

public class ToPDF {
	
	public static void main(String[] args) {

		try {
			String to = args[0].replace(".xml", ".pdf");
            new RendererPDF().render(args[0], new FileOutputStream(to));
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
		
	}

}
