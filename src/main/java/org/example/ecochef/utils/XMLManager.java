package org.example.ecochef.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;

public class XMLManager {

    public static <T> boolean writeXML(T c, String filename) {
        boolean result = false;
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(c.getClass());
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            m.marshal(c, new File(filename));
            result = true;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Este método lo seguimos dejando para archivos externos (como exportaciones)
    public static <T> T readXML(T c, String filename) {
        T result = c;
        try {
            JAXBContext context = JAXBContext.newInstance(c.getClass());
            Unmarshaller um = context.createUnmarshaller();
            result = (T) um.unmarshal(new File(filename));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Lee archivos que están dentro de la carpeta resources.
     * Usado para cargar el db_config.xml.
     */
    public static <T> T readXMLFromResources(Class<T> clazz, String resourcePath) {
        T result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller um = context.createUnmarshaller();
            // Esto busca el archivo dentro del paquete compilado
            InputStream is = clazz.getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("❌ No se encontró el recurso: " + resourcePath);
                return null;
            }
            result = (T) um.unmarshal(is);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return result;
    }
}