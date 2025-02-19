package biz.next_evolution;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

public class XmlToXX {

    public static void main(String[] args) throws IOException {

        var objectMapper = new ObjectMapper();

        System.out.println("----- xml file to json -----");
        var xmlFile = Path.of("./data/XmlToXX.xml").toFile();
        var xmlMapper = new XmlMapper();
        Object objJson = xmlMapper.readValue(xmlFile, Object.class);

        String jsonString = objectMapper.writeValueAsString(objJson);
        System.out.println(jsonString);

        Map<String, Object> obj = objectMapper.readValue(jsonString,
                                                         new TypeReference<Map<String, Object>>() {
                                                         });

        System.out.println("----- xml to properties -----");
        JavaPropsMapper javaPropsMapper = new JavaPropsMapper();
        Properties props = javaPropsMapper.writeValueAsProperties(obj);
        props.store(new FileOutputStream(new File("./data/XmlToXX.properties")), "");

        System.out.println("----- json to yaml -----");
        DumperOptions opts = new DumperOptions();
        opts.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        String yaml = new Yaml(opts).dump(obj);
        System.out.println(yaml);
    }

}