//
// COPYRIGHT LICENSE: This information contains sample code provided in source code form. You may copy, 
// modify, and distribute these sample programs in any form without payment to IBM for the purposes of 
// developing, using, marketing or distributing application programs conforming to the application 
// programming interface for the operating platform for which the sample code is written. 
// Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE ON AN "AS IS" BASIS 
// AND IBM DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, ANY IMPLIED 
// WARRANTIES OR CONDITIONS OF MERCHANTABILITY, SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE, 
// TITLE, AND ANY WARRANTY OR CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR ANY DIRECT, 
// INDIRECT, INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR OPERATION OF THE 
// SAMPLE SOURCE CODE. IBM HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS 
// OR MODIFICATIONS TO THE SAMPLE SOURCE CODE.  
//
// (C) COPYRIGHT International Business Machines Corp., 2011
// All Rights Reserved * Licensed Materials - Property of IBM
//

package pagecode;

import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlCommandButton;

/**
 * @author knutson
 *
 */
public class Help extends PageCodeBase {

	protected HtmlSelectBooleanCheckbox logging1;
	protected HtmlForm help;
	protected HtmlCommandButton resetdb;
	protected HtmlSelectBooleanCheckbox getLogging1() {
		if (logging1 == null) {
			logging1 = (HtmlSelectBooleanCheckbox) findComponentInRoot("logging1");
		}
		return logging1;
	}

	protected HtmlForm getHelp() {
		if (help == null) {
			help = (HtmlForm) findComponentInRoot("help");
		}
		return help;
	}

	protected HtmlCommandButton getResetdb() {
		if (resetdb == null) {
			resetdb = (HtmlCommandButton) findComponentInRoot("resetdb");
		}
		return resetdb;
	}

}