package JTE;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {

	public static void main(String[] args) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File("cities.xml"));
			Node root = doc.getElementsByTagName("routes").item(0);
			NodeList cardlist = root.getChildNodes();
			for (int i = 0; i < cardlist.getLength(); i++) {
				Node cardNode = cardlist.item(i);
				if (cardNode.getNodeType() == Node.ELEMENT_NODE) {
					NodeList cardAttrs = cardNode.getChildNodes();
					// one card
					for (int j = 0; j < cardAttrs.getLength(); j++) {
						if (cardAttrs.item(j).getNodeType() == Node.ELEMENT_NODE) {
							Node theNode = cardAttrs.item(j);
							switch (theNode.getNodeName()) {
							case "name": // here i have to wrte code that says, find a city wth this name in the graph
								System.out.println("City name: "
										+ theNode.getTextContent());
								break;
							case "land": // these will be it's land neighbors, so  will add each one of these to the arraylist
								NodeList landList = theNode.getChildNodes();
								for (int k = 0; k < landList.getLength(); k++) {
									if (landList.item(k).getNodeType() == Node.ELEMENT_NODE) {
										System.out.println("Land neighbour: "
												+ landList.item(k)
														.getTextContent());
									}
								}
								break;
							case "sea":
								NodeList seaList = theNode.getChildNodes();
								for (int k = 0; k < seaList.getLength(); k++) {
									if (seaList.item(k).getNodeType() == Node.ELEMENT_NODE) {
										System.out.println("Sea neighbour: "
												+ seaList.item(k)
														.getTextContent());
									}
								}
								break;
							}
						}
					}
				}
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
}
