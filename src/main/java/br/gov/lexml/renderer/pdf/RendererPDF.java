
package br.gov.lexml.renderer.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;

public class RendererPDF {

    public void render(final InputStream in, final OutputStream out) throws Exception {
        render(in, out, null);
    }
        
    public void render(final InputStream in, final OutputStream out, final Map<String , String> config)
        throws Exception {
    	SAXReader reader = new SAXReader();
        Document document = reader.read(in);
        
        XPath xpath = new DefaultXPath("/l:LexML/l:Metadado/l:Identificacao");
        Map<String,String> ns = new HashMap<String,String>();
        ns.put("l","http://www.lexml.gov.br/1.0");
        xpath.setNamespaceURIs(ns);        
        Node n = xpath.selectSingleNode(document);
        System.out.println("n:" + n);
        System.out.println("attributes: " + ((Element) n).attributes());
        String urn = ((Element)n).attributeValue("URN");
        System.out.println("urn:" + urn);
        String[] urnComps = urn.split(":");
        String autoridade = urnComps[3];
        String tipoNorma = urnComps[4];
                    	    	    	
        RendererPDFContext ctx = new RendererPDFContext(autoridade,tipoNorma);
        ctx.addConfig(config);
        
        Element root = document.getRootElement();

        ctx.setOutputStream(out);

        new PDFBuilder(ctx, root).build();

    }

    public void render(final String fileName, final OutputStream out) throws Exception {
        render(fileName, out, null);
    }

    public void render(final String fileName, final OutputStream out, final Map<String , String> config)
        throws Exception {

        File f = new File(fileName);

        if (!f.isFile() || !f.canRead()) {
            throw new Exception("Arquivo " + fileName + " não existe ou não pode ser lido.");
        }

        render(new FileInputStream(f), out);
    }

    private static void printHelp() {
        System.out.println("Utilize: java " + RendererPDF.class.getName() + " <arquivo_lexml>");
    }

    public static void main(final String[] args) {

        if (args.length != 1) {
            RendererPDF.printHelp();
            System.exit(1);
        }

        File f = new File(args[0]);
        
        if(!f.isFile()) {
        	System.out.println("Arquivo " + args[0] + " não encontrado.");
        	System.exit(1);
        }
        
        try {
            new RendererPDF().render(args[0], System.out);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
    }

}
