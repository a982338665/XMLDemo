package jdom;//package pers.li.xml;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.List;

/**
 * create by luofeng on 2018-04-23 10:17
 * JDOM方式解析XML：
 * 第三方解析 及乱码处理（支持中文编码的GBK，GB2312等）
 * <p>
 * 若包含乱码问题：
 * 1.查看xml文件的文件头是否为utf-8的编码
 * 2.代码解决乱码
 * --------------
 */
public class analysisJDOM {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        parserJDOM();
        createXMLJDOM();


    }

    private static void createXMLJDOM() throws IOException {
        //生成一个根节点
        Element rss = new Element("rss");
        Element channel = new Element("channel");
        Element title = new Element("title");
        title.setText("<国内最新新闻/>");
        rss.addContent(channel);
        channel.addContent(title);
        rss.setAttribute("version","2.0");
        //创建format对象
       /* Format compactFormat = Format.getCompactFormat();
        compactFormat.setIndent("");//设置换行*/
        Format compactFormat = Format.getPrettyFormat();
        compactFormat.setEncoding("GBK");

        //创建一个document对象
        Document document = new Document(rss);
        //创建XMLOutputter对象
        XMLOutputter outputter = new XMLOutputter(compactFormat){
            //重写此方法解决，元素内容转义字符
            @Override
            public String escapeElementEntities(String str) {
                return str;
            }
        };
        //利用此对象接那个document装换为xml文档
        outputter.output(document,new FileOutputStream(new File("src/main/java/xml/createJDOM.xml")));


    }


    private static void parserJDOM() throws IOException {
        //创建一个对象
        SAXBuilder saxBuilder = new SAXBuilder();
        //创建一个输入流
        InputStream inputStream = new FileInputStream("src/main/java/xml/books-gbk.xml");
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //乱码解决
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        try {
            //将输入流加载到SAXBuilder中
            Document document = saxBuilder.build(inputStreamReader);
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //通过document对象获取xml文件根节点
            Element element = document.getRootElement();
            //获取根节点下的子节点
            List<Element> children = element.getChildren();
            for (Element book :
                    children) {
                System.out.println("开始解析第几本书===================：" + (children.indexOf(book) + 1));
                //解析book属性
                List<Attribute> attributes = book.getAttributes();
                //获取指定属性名的属性值
                String id = book.getAttributeValue("id");
                System.out.println("属性名-->属性值：id=" + id);
                //遍历获取属性名属性值
                for (Attribute attr :
                        attributes) {

                    String name = attr.getName();
                    String value = attr.getValue();
                    System.out.println("属性名/属性值：" + name + "/" + value);
                }
                //对book节点的子节点节点名和节点值的遍历
                List<Element> bookChild = book.getChildren();
                for (Element child :
                        bookChild) {
                    System.out.println("节点名/节点值：" + child.getName() + "/" + child.getValue());
                }

            }
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }
}
