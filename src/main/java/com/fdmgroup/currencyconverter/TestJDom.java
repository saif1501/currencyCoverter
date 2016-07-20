package com.fdmgroup.currencyconverter;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.*;
/**
 * This is a test class for experimenting with JDom 2
 * @author max.mu, akindele.faulkner, hassam.hussain
 * @version 0.02
 * 
 */
public class TestJDom {
	private SAXBuilder builder;
	private HashMap<String, Double> dataMap;
	private InputStream is;
	
	public TestJDom(String path){
		this.builder = new SAXBuilder();
		this.is = getStreamThroughProxy("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
		this.dataMap = new HashMap<String, Double>();
	}
	
	public void parseCurrency() throws IOException, JDOMException{
		try{
			List<Element> list = parseChildrenXML();
			constructHashMapData(list);
		}
		catch (JDOMException e){
			System.out.println(e.getClass());
		}
		catch (IOException e){
			System.out.println(e.getClass());
		}
		finally{
			
		}

	}

    public static InputStream getStreamThroughProxy(String res) {
        URL url;
        try {
            url = new URL(res);
            Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("10.7.3.250", 3128));
            return (InputStream) url.openConnection(proxy).getContent();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

	private List<Element> parseChildrenXML() throws JDOMException, IOException{
		Document xmlDocument = (Document)this.builder.build(this.is);
		Element rootNode = xmlDocument.getRootElement();
		Element parsedChildren = rootNode.getChildren().get(2);
		List<Element> elementList = parsedChildren.getChildren().get(0).getChildren();
		return elementList;
	}
	
	private void constructHashMapData(List<Element> elementList){
		for(Element e: elementList){
			this.dataMap.put(e.getAttributeValue("currency"), Double.parseDouble(e.getAttributeValue("rate")));
		}
	}
	
	public Double convertToEuro(String currencySign, Double amount){
		if (this.dataMap.containsKey(currencySign)){
			return (amount / dataMap.get(currencySign));
		}
		else{
			return null;
		}
	}
	
	public Double convertFromEuro(String currencySign, Double amount){
		if (this.dataMap.containsKey(currencySign)){
			return (amount * dataMap.get(currencySign));
		}
		else{
			return null;
		}
	}
	
	public void convertToEuroDisplay(String currencySign, Double amount){
		Double result = this.convertToEuro(currencySign, amount);
		if (result == null){
			System.out.println("You should input a right currency sign");
		}
		else{
			System.out.println("If you convert " + amount.toString() + currencySign + "to Euro, you will get " + result.toString() + " Euros");
		}
	}
	
	public void convertEuroToDisplay(String currencySign, Double amount){
		Double result = this.convertFromEuro(currencySign, amount);
		if (result == null){
			System.out.println("You should input a right currency sign");
		}
		else{
			System.out.println("If you convert " + result.toString() + " Euros to " + currencySign + ", you could get " + result.toString() + currencySign);
		}
	}
	
}
