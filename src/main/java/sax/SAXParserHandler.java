package sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * create by lishengbo on 2018-04-23 11:20
 */
public class SAXParserHandler extends DefaultHandler {

    int bookIndex=0;

    String value=null;

    Book book=null;

    private ArrayList<Book> bookList=new ArrayList();

    public ArrayList<Book> getBookList() {
        return bookList;
    }

    /**
     * 用来遍历xml文件的开始标签
     * @param uri
     * @param localName
     * @param qName  某个节点的节点名称
     * @param attributes  属性
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        //开始解析book元素的属性
        if(qName.equals("book")){
            book=new Book();
            bookIndex++;
            System.out.println("==================开始遍历第几本书："+bookIndex);
            //已知book节点中有id属性
            String id = attributes.getValue("id");
            System.out.println("属性值："+id);
            //不知道book节点中有多少属性
            int length = attributes.getLength();
            for (int i = 0; i <length ; i++) {
                String qName1 = attributes.getQName(i);
                String value = attributes.getValue(i);
                System.out.println("属性名/属性值："+qName1+"/"+value);
                if(qName1.equals("id")){
                    book.setId(Integer.parseInt(value));
                }

            }
        }else if(!qName.equals("bookstore")&&!qName.equals("book")){
            System.out.print("除去根节点bookstore和book节点外的其他节点名："+qName+"|");
        }
    }

    /**
     * 用来遍历xml文件的结束标签
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        //判断book节点是否已经遍历结束
        if(qName.equals("book")){
            bookList.add(book);
            book=null;
            System.out.println("==================结束遍历第几本书："+bookIndex);

        }else if(qName.equals("name")){
            book.setName(value);
        }else if(qName.equals("author")){
            book.setAuthor(value);
        }else if(qName.equals("year")){
            book.setYear(value);
        }else if(qName.equals("price")){
            book.setPrice(value);
        }else if(qName.equals("language")){
            book.setLanguage(value);
        }
    }

    /**
     * 用来标志解析开始
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        System.out.println("SAX解析开始+++++++++");
    }
    /**
     * 用来标志解析结束
     * @throws SAXException
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("SAX解析结束+++++++++");

    }

    /**
     *
     * @param ch      节点中的所有内容
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        value = new String(ch, start, length);
        if(!value.trim().equals("")){
            System.out.println(value);
        }
    }
}
