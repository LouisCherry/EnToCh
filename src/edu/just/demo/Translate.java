package edu.just.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Translate<V> {
	public static void main(String[] args) {
		System.out.println("请输入你要查找的单词:");
		Scanner sc=new Scanner(System.in);
		String wordString=sc.nextLine();
		if (!exitFile("file\\dictionary.out")) {
			// 获得SAX工厂实例
			SAXParserFactory factory = SAXParserFactory.newInstance();
			// 获得解析器实例
			SAXParser parse = null;
			try {
				parse = factory.newSAXParser();
			} catch (ParserConfigurationException | SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				parse.parse(new File("file\\大学英语四级（CET 4）有道词库.xml"), new MyHandler());
			} catch (SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//反序列化
		ObjectInputStream oin=null;
		try {
			oin=new ObjectInputStream(new FileInputStream("file\\dictionary.out"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dictionary dictionary=null;
		try {
			dictionary=(Dictionary) oin.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				oin.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		TreeMap<String,Word> map=dictionary.word;
		Word word=map.get(wordString);
		if(map.containsValue(word)){
			System.out.println("释义:");
			System.out.println(word.trans);
		}
		else {
			System.out.println("没有找到");
		}
	}
	public static boolean exitFile(String filename){
		File file=new File(filename);
		if(!file.exists()){
			return false;
		}else{
			return true;
		}
	}
}

class MyHandler<V> extends DefaultHandler {
	// 栈用于暂存解析文件的标签
	private Stack<String> stack = new Stack<String>();
	String word;// 英语
	String trans;// 汉语翻译
	String phonetic;// 读音
	String tags;// 标签
	Word word2;
	Dictionary<V> dictionary = new Dictionary<V>();
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		stack.push(qName);
	}
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		stack.pop();
	}
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String tag = stack.peek();// 查找出栈顶的元素
		if ("word".equals(tag)) {
			word2=new Word();
			word = new String(ch, start, length);
			word=word.trim();
			word2.word = word;			
		} else if ("trans".equals(tag)) {
			trans = new String(ch, start, length);
			word2.trans = trans;
		} else if ("phonetic".equals(tag)) {
			phonetic = new String(ch, start, length);
			word2.phonetic = phonetic;
		} else if ("tags".equals(tag)) {
			tags = new String(ch, start, length);
			word2.tags = tags;
			dictionary.word.put(word, word2);
		} else {
		}
	}
	@Override
	public void endDocument() throws SAXException {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream("file\\dictionary.out"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			oos.writeObject(dictionary);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
