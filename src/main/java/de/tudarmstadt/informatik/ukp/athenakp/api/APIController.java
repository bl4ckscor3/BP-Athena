package de.tudarmstadt.informatik.ukp.athenakp.api;

import java.util.Deque;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import de.tudarmstadt.informatik.ukp.athenakp.api.ast.RequestNode;
import de.tudarmstadt.informatik.ukp.athenakp.exception.SyntaxException;
import de.tudarmstadt.informatik.ukp.athenakp.exception.VerificationFailedException;

@RestController
public class APIController {
	@RequestMapping("/**") //matches the complete path (containing all subpaths), just make sure that there are no ? in there!!
	public Object apiConnector(HttpServletRequest request) { //the argument contains everything that was not matched to any other argument

		try {
			//scan and parse the request
			String apiRequest = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
			RequestScanner scanner = new RequestScanner(apiRequest);
			Deque<RequestToken> tokens = scanner.scan();
			RequestParser parser = new RequestParser(tokens);
			RequestNode tree = parser.parse();

			try {
				RequestVerifier.verify(tree); //if no exception is thrown, the verification was successful
			}
			catch(VerificationFailedException e) {
				return e.getMessage();
			}

			return tree.toString();
		}
		catch(SyntaxException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
