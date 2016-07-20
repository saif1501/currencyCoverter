package com.fdmgroup.currencyconverter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.Proxy.Type;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * This is a currency converter that could be used to convert any amount in certain currency to euro or convert any amount 
 * of euro to certain currency 
 *
 * @author max.mu, hassam.hussain, akindele.faulkner
 * @version 0.02
 *
 */

public class CurrencyConverter {
	private SAXBuilder builder;
	private HashMap<String, Double> dataMap;
	private InputStream is;
	
	/**
	 * Constructor for CurrencyConverter, you could gain a currency converter with updated data fetched from www.ecb.europa.eu
	 * @throws JDOMException
	 * @throws IOException
	 */
	public CurrencyConverter() throws JDOMException, IOException{
		this.builder = new SAXBuilder();
		this.is = getStreamThroughProxy("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
		this.dataMap = new HashMap<String, Double>();
		
		this.constructHashMap();
		
	}
	
	/**
	 * Helper function that could be used to fetch the updated data from www.ecb.europa.eu and used in the constructor
	 * @param res
	 * @return InputStream
	 */
    private static InputStream getStreamThroughProxy(String res) {
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
    
    /**
     * Helper function that could be used to construct the HashMap saved with updated currency data
     * @throws JDOMException
     * @throws IOException
     */
    
	private void constructHashMap() throws JDOMException, IOException{
		InputStream is = this.getIs();
		Document xmlDocument = (Document)this.getBuilder().build(this.getIs());
		Element rootNode = xmlDocument.getRootElement();
		Element parsedChildren = rootNode.getChildren().get(2);
		List<Element> elementList = parsedChildren.getChildren().get(0).getChildren();
		for(Element e: elementList){
			this.dataMap.put(e.getAttributeValue("currency"), Double.parseDouble(e.getAttributeValue("rate")));
		}
	}

	/**
     * Getter funtion for this.inputstream
     * @return InputStream
     */
    public InputStream getIs() {
		return this.is;
	}
    
    /**
     * Getter method for this.builder
     * @return SAXBuilder
     */
	public SAXBuilder getBuilder() {
		return this.builder;
	}

	/**
	 * Getter method for this.dataMap
	 * @return HashMap<String, Double>
	 */
	public HashMap<String, Double> getDataMap() {
		return this.dataMap;
	}
	
	/**
	 * You could gain the amount of euro when you want to convert "amount" of "currencySign"
	 * @param currencySign
	 * @param amount
	 * @return Double
	 */
	public Double convertToEuro(String currencySign, Double amount){
		if (this.dataMap.containsKey(currencySign)){
			return (amount / dataMap.get(currencySign));
		}
		else{
			return null;
		}
	}
	
	/**
	 * You could gain the amount of "currencySign" when you want to convert "amount" of euros
	 * @param currencySign
	 * @param amount
	 * @return Double
	 */
	public Double convertFromEuro(String currencySign, Double amount){
		if (this.dataMap.containsKey(currencySign)){
			return (amount * dataMap.get(currencySign));
		}
		else{
			return null;
		}
	}
	
	/**
	 * Display a result the amount of euro when you want to convert "amount" of "currencySign"
	 * @param currencySign
	 * @param amount
	 */
	public void convertToEuroDisplay(String currencySign, Double amount){
		Double result = this.convertToEuro(currencySign, amount);
		if (result == null){
			System.out.println("You should input a right currency sign");
		}
		else{
			System.out.println("If you convert " + amount.toString() + currencySign + " to Euro, you will get " + result.toString() + "Euros");
		}
	}
	
	/**
	 * Display a result of the amount of "currencySign" when you want to convert "amount" of euros
	 * @param currencySign
	 * @param amount
	 */
	public void convertEuroToDisplay(String currencySign, Double amount){
		Double result = this.convertFromEuro(currencySign, amount);
		if (result == null){
			System.out.println("You should input a right currency sign");
		}
		else{
			System.out.println("If you convert " + amount.toString() + "Euros to " + currencySign + ", you could get " + result.toString() + currencySign);
		}
	}
	
	public static void main(String[] args) throws IOException, JDOMException {
		CurrencyConverter cc = new CurrencyConverter();
		cc.convertEuroToDisplay("JPY", 10000.0);
		cc.convertToEuroDisplay("JPY", 10000.0);
	}
	
	
    
}
