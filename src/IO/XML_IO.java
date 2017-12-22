package IO;

import java.io.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XML_IO implements InputOutput {

    @Override
    public void serialize(Object object, File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(object, file);
        } catch (JAXBException e) {
        	e.printStackTrace();
            System.out.println("FFFFFFFFF"+e.getMessage());
        }
    }

    @Override
    public Object deserialize(Class c, File file) {
        Object object = null;
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            object = unmarshaller.unmarshal(file);
        } 
        catch (JAXBException e) {
            System.out.println("AAAAAAA"+e.getMessage());
        } 
        finally 
        {
            return object;
        }
    }
}


