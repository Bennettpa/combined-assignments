package com.cooksys.ftd.assignments.file;

import com.cooksys.ftd.assignments.file.model.Contact;
import com.cooksys.ftd.assignments.file.model.Instructor;
import com.cooksys.ftd.assignments.file.model.Session;
import com.cooksys.ftd.assignments.file.model.Student;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {

    /**
     * Creates a {@link Student} object using the given studentContactFile.
     * The studentContactFile should be an XML file containing the marshaled form of a
     * {@link Contact} object.
     *
     * @param studentContactFile the XML file to use
     * @param jaxb the JAXB context to use
     * @return a {@link Student} object built using the {@link Contact} data in the given file
     * @throws JAXBException 
     */
    public static Student readStudent(File studentContactFile, JAXBContext jaxb) throws JAXBException {
        Student student =  new Student();        
        Unmarshaller jaxbUnmarshaller = jaxb.createUnmarshaller();
        Contact c = (Contact) jaxbUnmarshaller.unmarshal(studentContactFile);
        student.setContact(c);
    	return student;		
    }

    /**
     * Creates a list of {@link Student} objects using the given directory of student contact files.
     *
     * @param studentDirectory the directory of student contact files to use
     * @param jaxb the JAXB context to use
     * @return a list of {@link Student} objects built using the contact files in the given directory
     * @throws JAXBException 
     */
    public static List<Student> readStudents(File studentDirectory, JAXBContext jaxb) throws JAXBException {
    	List<Student> students = new ArrayList<Student>();
    	for(File student : studentDirectory.listFiles()){
    		students.add(readStudent(student, jaxb));
    	}
        return students;
    }

    /**
     * Creates an {@link Instructor} object using the given instructorContactFile.
     * The instructorContactFile should be an XML file containing the marshaled form of a
     * {@link Contact} object.
     *
     * @param instructorContactFile the XML file to use
     * @param jaxb the JAXB context to use
     * @return an {@link Instructor} object built using the {@link Contact} data in the given file
     * @throws JAXBException 
     */
    public static Instructor readInstructor(File instructorContactFile, JAXBContext jaxb) throws JAXBException {
        Instructor student =  new Instructor();        
        Unmarshaller jaxbUnmarshaller = jaxb.createUnmarshaller();
        Contact c = (Contact) jaxbUnmarshaller.unmarshal(instructorContactFile);
        student.setContact(c);
    	return student;		
    }

    /**
     * Creates a {@link Session} object using the given rootDirectory. A {@link Session}
     * root directory is named after the location of the {@link Session}, and contains a directory named
     * after the start date of the {@link Session}. The start date directory in turn contains a directory named
     * `students`, which contains contact files for the students in the session. The start date directory
     * also contains an instructor contact file named `instructor.xml`.
     *
     * @param rootDirectory the root directory of the session data, named after the session location
     * @param jaxb the JAXB context to use
     * @return a {@link Session} object built from the data in the given directory
     * @throws JAXBException 
     */
    public static Session readSession(File rootDirectory, JAXBContext jaxb) throws JAXBException {
    	String location = rootDirectory.getName();
    	String startDate = rootDirectory.listFiles()[0].getName();
    	Session session = new Session();
    	File studentDirectory = new File(rootDirectory.getPath()+"\\"+startDate+"\\students");
    	File instructorContactFile = new File(rootDirectory.getPath()+"\\"+startDate+"\\instructor.xml");
    	session.setInstructor(readInstructor(instructorContactFile, jaxb));
    	session.setLocation(location);
    	session.setStartDate(startDate);
    	session.setStudents(readStudents(studentDirectory, jaxb));
    	System.out.println(session);
        return session; 
    }

    /**
     * Writes a given session to a given XML file
     *
     * @param session the session to write to the given file
     * @param sessionFile the file to which the session is to be written
     * @param jaxb the JAXB context to use
     * @throws JAXBException 
     * @throws FileNotFoundException 
     */
    public static void writeSession(Session session, File sessionFile, JAXBContext jaxb) throws JAXBException, FileNotFoundException {
        Marshaller marshaller = jaxb.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(session, new FileOutputStream(sessionFile));
    }

    /**
     * Main Method Execution Steps:
     * 1. Configure JAXB for the classes in the com.cooksys.serialization.assignment.model package
     * 2. Read a session object from the <project-root>/input/memphis/ directory using the methods defined above
     * 3. Write the session object to the <project-root>/output/session.xml file.
     *
     * JAXB Annotations and Configuration:
     * You will have to add JAXB annotations to the classes in the com.cooksys.serialization.assignment.model package
     *
     * Check the XML files in the <project-root>/input/ directory to determine how to configure the {@link Contact}
     *  JAXB annotations
     *
     * The {@link Session} object should marshal to look like the following:
     *      <session location="..." start-date="...">
     *           <instructor>
     *               <contact>...</contact>
     *           </instructor>
     *           <students>
     *               ...
     *               <student>
     *                   <contact>...</contact>
     *               </student>
     *               ...
     *           </students>
     *      </session>
     * @throws JAXBException 
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws JAXBException, FileNotFoundException {
    	JAXBContext jaxbContext = JAXBContext.newInstance(Contact.class);
		File rootDirectory = new File("input/memphis");
		Session session = readSession(rootDirectory , jaxbContext);
		jaxbContext = JAXBContext.newInstance(Session.class);
		File output = new File("output/session.xml");
		writeSession(session,output,jaxbContext);
    }
}
