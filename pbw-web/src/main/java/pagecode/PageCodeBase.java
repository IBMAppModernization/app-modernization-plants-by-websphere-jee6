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

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot ;
import javax.faces.context.FacesContext;

/**
 * Provides a common base class for all generated Page Code files.
 */
@SuppressWarnings({"rawtypes","unchecked"})
public abstract class PageCodeBase {

	public PageCodeBase() {
	}

	protected void gotoPage(String pageName) {
		if (pageName != null) {
			FacesContext context = getFacesContext();
			UIViewRoot newView =
				context.getApplication().getViewHandler().createView(
					context,
					pageName);
			context.setViewRoot(newView);
			context.renderResponse();
		}
	}

	/**
	 * <p>Return the {@link UIComponent} (if any) with the specified
	 * <code>id</code>, searching recursively starting at the specified
	 * <code>base</code>, and examining the base component itself, followed
	 * by examining all the base component's facets and children.
	 * Unlike findComponent method of {@link UIComponentBase}, which
	 * skips recursive scan each time it finds a {@link NamingContainer},
	 * this method examines all components, regardless of their namespace
	 * (assuming IDs are unique).
	 *
	 * @param base Base {@link UIComponent} from which to search
	 * @param id Component identifier to be matched
	 */
	public static UIComponent findComponent(UIComponent base, String id) {

		// Is the "base" component itself the match we are looking for?
		if (id.equals(base.getId())) {
			return base;
		}

		// Search through our facets and children
		UIComponent kid = null;
		UIComponent result = null;
		Iterator kids = base.getFacetsAndChildren();
		while (kids.hasNext() && (result == null)) {
			kid = (UIComponent) kids.next();
			if (id.equals(kid.getId())) {
				result = kid;
				break;
			}
			result = findComponent(kid, id);
			if (result != null) {
				break;
			}
		}
		return result;
	}

	public static UIComponent findComponentInRoot(String id) {
		UIComponent ret = null;

		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			UIComponent root = context.getViewRoot();
			ret = findComponent(root, id);
		}

		return ret;
	}

	/**
	 * Place an Object on the tree's attribute map
	 * 
	 * @param key
	 * @param value
	 */
	protected void putTreeAttribute(String key, Object value) {
		getFacesContext().getViewRoot().getAttributes().put(key, value);
	}

	/**
	 * Retrieve an Object from the tree's attribute map
	 * @param key
	 * @return	Object	object to fetch
	 */
	protected Object getTreeAttribute(String key) {
		return getFacesContext().getViewRoot().getAttributes().get(key);
	}

	/**
	 * Return the result of the resolved expression
	 * 
	 * @param expression
	 * @return	Object	result of expression evaluation
	 */
	protected Object resolveExpression(String expression) {
		Object value = null;
		if ((expression.indexOf("#{") != -1)
			&& (expression.indexOf("#{") < expression.indexOf('}'))) {
			value =	getFacesContext().getApplication().getExpressionFactory().createValueExpression(
						getFacesContext().getELContext(), expression, Object.class).getValue(getFacesContext().getELContext());
		} else {
			value = expression;
		}
		return value;
	}
	
	/**
	 * Return the managed bean with the given name
	 * 
	 * @param mgdBeanName   the name of the managed bean to retrieve
	 * @return	Object	manage bean instance
	 */	
	protected Object getManagedBean( String mgdBeanName ) {
		String expression = "#{" + mgdBeanName + "}";
		return resolveExpression(expression);
	}	

	/**
	 * Resolve all parameters passed in via the argNames/argValues array pair, and 
	 * add them to the provided paramMap. If a parameter can not be resolved, then it
	 * will attempt to be retrieved from a cachemap stored using the cacheMapKey
	 * 
	 * @param paramMap
	 * @param argNames
	 * @param argValues
	 * @param cacheMapKey
	 */
	protected void resolveParams(
		Map paramMap,
		String[] argNames,
		String[] argValues,
		String cacheMapKey) {

		Object rawCache = getTreeAttribute(cacheMapKey);
		Map cache = Collections.EMPTY_MAP;
		if (rawCache instanceof Map) {
			cache = (Map) rawCache;
		}
		for (int i = 0; i < argNames.length; i++) {
			Object result = resolveExpression(argValues[i]);
			if (result == null) {
				result = cache.get(argNames[i]);
			}
			paramMap.put(argNames[i], result);
		}
		putTreeAttribute(cacheMapKey, paramMap);
	}
	
	


	/** 
	 * Returns a full system path for a file path given relative to the web project
	 */
	protected static String getRealPath(String relPath) {
		String path = relPath;
		try {
			URL url =
				FacesContext.getCurrentInstance()
					.getExternalContext()
					.getResource(
					relPath);
			if (url != null) {
				path = url.getPath();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return path;
	}
	
	/** 
	 * Returns an InputStream for a resource at the given path
	 */
	protected static InputStream getResourceInputStream(String relPath) {
		return FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(relPath);
	}
	
	protected void logException(Throwable throwable) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		throwable.printStackTrace(printWriter);
		log(stringWriter.toString());
	}

	protected void log(String message) {
		System.out.println(message);
	}

	public Map getApplicationScope() {
		return getFacesContext().getExternalContext().getApplicationMap();
	}

	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public Map getRequestParam() {
		return getFacesContext().getExternalContext().getRequestParameterMap();
	}

	public Map getRequestScope() {
		return getFacesContext().getExternalContext().getRequestMap();
	}

	public Map getSessionScope() {
		return getFacesContext().getExternalContext().getSessionMap();
	}

}