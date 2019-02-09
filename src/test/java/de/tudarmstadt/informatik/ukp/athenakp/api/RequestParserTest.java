package de.tudarmstadt.informatik.ukp.athenakp.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.tudarmstadt.informatik.ukp.athenakp.api.ast.NumberAttributeNode;
import de.tudarmstadt.informatik.ukp.athenakp.api.ast.NumberNode;
import de.tudarmstadt.informatik.ukp.athenakp.api.ast.RequestEntityNode;
import de.tudarmstadt.informatik.ukp.athenakp.api.ast.RequestHierarchyNode;
import de.tudarmstadt.informatik.ukp.athenakp.api.ast.RequestNode;
import de.tudarmstadt.informatik.ukp.athenakp.api.ast.StringAttributeNode;
import de.tudarmstadt.informatik.ukp.athenakp.api.ast.StringNode;
import de.tudarmstadt.informatik.ukp.athenakp.exception.SyntaxException;

public class RequestParserTest {
	private RequestParser uut1 = new RequestParser(new RequestScanner("/paper:author=Daniel+Klingbein&topic=vogonpoetry$author:obit=1993+05+22").scan()); //syntactically correct
	private RequestParser uut2 = new RequestParser(new RequestScanner("/paper:author=Daniel+Klingbein&topic|vogonpoetry$author:obit=1993+05+22").scan()); //syntactically incorrect, unknown symbol in the middle (|)
	private RequestParser uut3 = new RequestParser(new RequestScanner("/paper:author=Daniel+Klingbein&topic&vogonpoetry$author:obit=1993+05+22").scan()); //syntactically incorrect, symbol at incorrect position (& instead of = in the middle)

	@Test
	public void testCorrectParse() throws SyntaxException {
		RequestNode actual = uut1.parse();
		RequestNode expected = new RequestNode(0);

		RequestHierarchyNode theOneAndOnly = new RequestHierarchyNode(0);
		RequestEntityNode left = new RequestEntityNode(1);
		StringNode leftJoinNodeName = new StringNode(1);
		StringAttributeNode authorAttr = new StringAttributeNode(7);
		StringNode authorAttrName = new StringNode(7);
		StringNode authorAttrVal = new StringNode(14);
		StringAttributeNode topicAttr = new StringAttributeNode(31);
		StringNode topicAttrName = new StringNode(31);
		StringNode topicAttrVal = new StringNode(37);

		RequestEntityNode right = new RequestEntityNode(49);
		StringNode rightJoinNodeName = new StringNode(49);
		NumberAttributeNode obitAttr = new NumberAttributeNode(56);
		StringNode obitAttrName = new StringNode(56);
		NumberNode obitAttrVal1 = new NumberNode(61);
		NumberNode obitAttrVal2 = new NumberNode(66);
		NumberNode obitAttrVal3 = new NumberNode(69);

		obitAttrVal3.setNumber(22);
		obitAttrVal2.setNumber(5);
		obitAttrVal1.setNumber(1993);
		obitAttrName.setString("obit");
		obitAttr.addNumber(obitAttrVal1);
		obitAttr.addNumber(obitAttrVal2);
		obitAttr.addNumber(obitAttrVal3);
		obitAttr.setName(obitAttrName);
		right.addAttributeNode(obitAttr);
		rightJoinNodeName.setString("author");
		right.setEntityName(rightJoinNodeName);

		topicAttrVal.setString("vogonpoetry");
		topicAttrName.setString("topic");
		topicAttr.setValue(topicAttrVal);
		topicAttr.setName(topicAttrName);
		authorAttrVal.setString("Daniel Klingbein");
		authorAttrName.setString("author");
		authorAttr.setValue(authorAttrVal);
		authorAttr.setName(authorAttrName);
		left.addAttributeNode(authorAttr);
		left.addAttributeNode(topicAttr);
		leftJoinNodeName.setString("paper");
		left.setEntityName(leftJoinNodeName);

		theOneAndOnly.addEntity(left);
		theOneAndOnly.addEntity(right);
		expected.addHierarchyNode(theOneAndOnly);
		assertEquals("ASTs are not the same!", expected, actual);
	}

	@Test(expected = SyntaxException.class)
	public void testIncorrectSyntaxParse() throws SyntaxException {
		uut2.parse();
	}

	@Test(expected = SyntaxException.class)
	public void testUnexpectedTokenParse() throws SyntaxException {
		uut3.parse();
	}
}
