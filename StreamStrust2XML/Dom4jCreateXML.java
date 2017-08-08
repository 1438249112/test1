

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class Dom4jCreateXML{
    public void testCreateXml() {
        //����һ��xml�ĵ�
        Document doc = DocumentHelper.createDocument();
        //��xml�ļ������ע��
        doc.addComment("������ע��");
        //����һ����Ϊstudents�Ľڵ㣬��Ϊ�ǵ�һ�������������Ǹ��ڵ�,��ͨ��doc����һ����ᱨ��
        Element root = doc.addElement("students");
        //��root�ڵ��´���һ����Ϊstudent�Ľڵ�
        Element stuEle = root.addElement("student");
        //��student�ڵ��������
        stuEle.addAttribute("id", "101");
        //��student�ڵ����һ���ӽڵ�
        Element nameEle = stuEle.addElement("name");
        //�����ӽڵ���ı�
        nameEle.setText("����");
        //���ڸ�ʽ��xml���ݺ�����ͷ����ǩ
        OutputFormat format = OutputFormat.createPrettyPrint();
        //����xml�ĵ��ı���Ϊutf-8
        format.setEncoding("utf-8");
        Writer out;
        try {
            //����һ�����������
            out = new FileWriter("E://xml//new.xml");
            //����һ��dom4j����xml�Ķ���
            XMLWriter writer = new XMLWriter(out, format);
            //����write������doc�ĵ�д��ָ��·��
            writer.write(doc);
            writer.close();
            System.out.print("����XML�ļ��ɹ�");
        } catch (IOException e) {
            System.out.print("����XML�ļ�ʧ��");
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        Dom4jCreateXML xml = new Dom4jCreateXML();
        xml.testCreateXml();
    }
}