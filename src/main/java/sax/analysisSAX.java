package sax;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by luofeng on 2018-04-23 10:17
 * SAX方式解析XML：
 *  通过自己创建的Handler处理类逐个按顺序分析遇到的每一个节点
 * --------------
 *
 */
public class analysisSAX {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        parserXMLSAX();
        createXMLSAX();

    }

    public static ArrayList<Book> parserXMLSAX(){
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser parser = null;
        try {
            parser = spf.newSAXParser();
            SAXParserHandler dh = new SAXParserHandler();
            parser.parse("src/main/java/xml/books.xml",dh);
            System.out.println(dh.getBookList().size());
            System.out.println(dh.getBookList().toString());
            return dh.getBookList();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createXMLSAX(){
        ArrayList<Book> books=parserXMLSAX();
        //生成xml
        //创建一个TransformerFactory
        SAXTransformerFactory transformerFactory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
        try {
            TransformerHandler handler = transformerFactory.newTransformerHandler();
            //通过handler对象创建一个transformer对象
            Transformer transformer = handler.getTransformer();
            //添加有换行符的输出,文件编码设置
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
            //创建文件对象
            File file = new File("src/main/java/xml/createSAX.xml");
            if(!file.exists()){
                file.createNewFile();
            }
            //关联文件与handler
            Result streamResult = new StreamResult(new FileOutputStream(file));
            handler.setResult(streamResult);
            //使用handler对象对xml文件进行内容编写
            //打开xml文档 Document
            handler.startDocument();
            AttributesImpl attr=new AttributesImpl();

            handler.startElement("","","bookstore",attr);


                //父节点属性设置
            Map<String, String> objectObjectHashMap = new HashMap();
            objectObjectHashMap.put("id","写在平行节点里");
            objectObjectHashMap.put("name","book");
            List<Map<String,String>> list=new ArrayList();
            list.add(objectObjectHashMap);

            insertPingXingNode(handler, attr, books,"book",list);
                //结束插入平行子节点
               /* for(int i=0;i<fields.length;i++){
//                    System.out.println(fields[i].getType());
                    String name=fieldNames[i]=fields[i].getName();
                    name=name.substring(0,1).toUpperCase()+name.substring(1);
//                    Method method = book.getClass().getMethod("get" + name);
//                    Object invoke = method.invoke(book);
//                    System.out.println(fieldNames[i]+"|"+invoke);
//                    attr.clear();
                    handler.endElement("","",name);
                }
*/

            handler.endElement("","","bookstore");

            //关闭xml文档 Document
            handler.endDocument();


        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * xml文件生成--反射
     *
     * @param handler
     * @param attr
     * @param books   --平行子节点集合
     * @param parentNodeName --仅有一个父节点，父节点名称
     * @param list           --父节点属性集合
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws SAXException
     */
    private static void insertPingXingNode(TransformerHandler handler, AttributesImpl attr, List<Book> books,
                                           String parentNodeName,List<Map<String,String>> list


    ) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SAXException {
        attr.clear();
        for (Map<String,String> map:list){
            for(String key : map.keySet()){
                String value = map.get(key);
//                System.out.println(key+"  "+value);
                attr.addAttribute("","",key,"",value);
            }
        }
        handler.startElement("","",parentNodeName,attr);
        for(Book book:books) {
            Field[] fields = book.getClass().getDeclaredFields();
            String[] fieldNames = new String[fields.length];
            //开始插入平行子节点
            for (int i = 0; i < fields.length; i++) {
//                    System.out.println(fields[i].getType());
                String name = fieldNames[i] = fields[i].getName();
                String fact = name;
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                Method method = book.getClass().getMethod("get" + name);
                Object invoke = method.invoke(book);
                System.out.println(name + "|" + invoke);
                attr.clear();
                handler.startElement("", "", fact, attr);
                if (invoke != null) {
                    handler.characters(invoke.toString().toCharArray(), 0, invoke.toString().length());
                }
                handler.endElement("", "", fact);
            }
        }
        handler.endElement("","",parentNodeName);
    }
}
