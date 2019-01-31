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
// (C) COPYRIGHT International Business Machines Corp., 2001,2011
// All Rights Reserved * Licensed Materials - Property of IBM
//
package com.ibm.websphere.samples.pbw.war;

import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.FinderException;

import com.ibm.websphere.samples.pbw.ejb.MailerAppException;
import com.ibm.websphere.samples.pbw.ejb.MailerBean;
import com.ibm.websphere.samples.pbw.jpa.Customer;
import com.ibm.websphere.samples.pbw.utils.Util;

/**
 * This class sends the email confirmation message. 
 */

public class MailAction implements java.io.Serializable 
{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private MailerBean mailer;

/** Public constructor */
    public MailAction( ) { }

   /**
    * Send the email order confirmation message. 
    *
    * @param customer The customer information.
    * @param orderKey The order number.
    */
    public final void sendConfirmationMessage(Customer customer, String orderKey)
                             throws CreateException, FinderException
    {
       try 
       {
    	   System.out.println("mailer="+mailer);
          mailer.createAndSendMail(customer, orderKey);
       }
       // The MailerAppException will be ignored since mail may not be configured.
       catch (MailerAppException e) {
    	   Util.debug("Mailer threw exception, mail may not be configured. Exception:"+e);    	   
       }
    }

}
