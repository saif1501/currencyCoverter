package com.fdmgroup.currencyconverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.jdom2.JDOMException;

public class CommandLineUI {
	public static void main(String[] args) throws IOException, JDOMException {
		TestJDom testJDom = new TestJDom("asdfasdf");
		Double input = new Double(100000);
		Double result = testJDom.convertToEuro("JPY", input);
		System.out.println(result);

	}
}
