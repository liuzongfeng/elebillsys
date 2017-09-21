package rest.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class testRest {

	@RequestMapping(value = "/testRest", method = RequestMethod.GET)
	@ResponseBody
	public String testRest(){
		
		return "Hello World";
	}
}
