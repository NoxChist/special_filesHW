import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.xml";
        List<Employee> list = parseXML(fileName);
        String json = listToJson(list);

        try {
            Files.writeString(Path.of("data2.json"), json);
        } catch (Exception e) {
        }
    }

    public static List<Employee> parseXML(String filePath) {
        List<Employee> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filePath));
            Node root = doc.getDocumentElement();
            NodeList nodeList = doc.getElementsByTagName("employee");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element elem = (Element) nodeList.item(i);
                NodeList nodeList_ = elem.getChildNodes();
                Employee employee = new Employee();
                for (int j = 0; j < nodeList_.getLength(); j++) {
                    Node tmp = nodeList_.item(j);
                    if (Node.ELEMENT_NODE == tmp.getNodeType()) {
                        Element elem_ = (Element) tmp;
                        switch (elem_.getTagName()) {
                            case "id":
                                employee.id = Long.valueOf(elem_.getTextContent());
                                break;
                            case "firstName":
                                employee.firstName = elem_.getTextContent();
                                break;
                            case "lastName":
                                employee.lastName = elem_.getTextContent();
                                break;
                            case "country":
                                employee.country = elem_.getTextContent();
                                break;
                            case "age":
                                employee.age = Integer.valueOf(elem_.getTextContent());
                                break;
                        }
                    }
                }
                list.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }
}
