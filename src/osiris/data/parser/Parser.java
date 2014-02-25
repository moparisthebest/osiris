package osiris.data.parser;

/*
 * Osiris Emulator
 * Copyright (C) 2011  Garrett Woodard, Blake Beaupain, Travis Burtrum
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 * 
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

// TODO: Auto-generated Javadoc
/**
 * The Class Parser.
 * 
 * @author samuraiblood2
 * 
 */
public abstract class Parser implements Runnable {

	/** The file. */
	private File file;

	/**
	 * Instantiates a new parser with a pre-given directory.
	 * 
	 * @param file
	 *            the file
	 */
	public Parser(String file) {
		this(new File("./data/config/" + file));
	}

	/**
	 * Instantiates a new parser.
	 * 
	 * @param file
	 *            the file
	 */
	public Parser(File file) {
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(getFile());
			doc.getDocumentElement().normalize();

			// XXX: We can use this to parse other files with the same parser.
			doIncludeTag(doc);

			onParse(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Anything done on parse.
	 * 
	 * @param doc
	 *            the doc
	 * @throws Exception
	 *             the exception
	 */
	public abstract void onParse(Document doc) throws Exception;

	/**
	 * If there is an include tag, we change the file name and run the thread.
	 * This way we can have XML files calling other files with the same parser.
	 * 
	 * @param doc
	 *            the doc
	 */
	private void doIncludeTag(Document doc) {
		Element includeElement = (Element) doc.getElementsByTagName("include").item(0);
		if (includeElement != null) {
			String dir = "./data/config/";
			if (includeElement.getAttribute("dir") != null) {
				dir = includeElement.getAttribute("dir");
			}
			setFile(new File(dir + includeElement.getAttribute("name")));
			run();
		}
	}

	/**
	 * Gets the value inside the specified tag as a String.
	 * 
	 * @param element
	 *            the element
	 * @param tag
	 *            the tag
	 * @return the text value
	 */
	public String getTextValue(Element element, String tag) {
		NodeList list = element.getElementsByTagName(tag);

		if (list == null) {
			return null;
		}

		if (list.getLength() <= 0) {
			return null;
		}

		Element tmp = (Element) list.item(0);
		return tmp.getFirstChild().getNodeValue();
	}

	/**
	 * Sets the file.
	 * 
	 * @param file
	 *            the file
	 */
	private void setFile(File file) {
		this.file = file;
	}

	/**
	 * Gets the file.
	 * 
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
}
