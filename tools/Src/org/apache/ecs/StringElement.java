/*
 * Copyright (c) 1999 The Java Apache Project.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. All advertising materials mentioning features or use of this
 *    software must display the following acknowledgment:
 *    "This product includes software developed by the Java Apache 
 *    Project. <http://java.apache.org/>"
 *
 * 4. The names "Java Apache Element Construction Set", "Java Apache ECS" and 
 *    "Java Apache Project" must not be used to endorse or promote products 
 *    derived from this software without prior written permission.
 *
 * 5. Products derived from this software may not be called 
 *    "Java Apache Element Construction Set" nor "Java Apache ECS" appear 
 *    in their names without prior written permission of the 
 *    Java Apache Project.
 *
 * 6. Redistributions of any form whatsoever must retain the following
 *    acknowledgment:
 *    "This product includes software developed by the Java Apache 
 *    Project. <http://java.apache.org/>"
 *    
 * THIS SOFTWARE IS PROVIDED BY THE JAVA APACHE PROJECT "AS IS" AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE JAVA APACHE PROJECT OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Java Apache Project. For more information
 * on the Java Apache Project please see <http://java.apache.org/>.
 *
 */
package org.apache.ecs;


/**
    This class is used to create a String element in ECS. A StringElement 
    has no tags wrapped around it, it is an Element without tags.
    
    @version $Id: StringElement.java,v 1.3 2004/02/11 18:49:50 jjanke Exp $
    @author <a href="mailto:snagy@servletapi.com">Stephan Nagy</a>
    @author <a href="mailto:jon@clearink.com">Jon S. Stevens</a>
*/
public class StringElement extends ConcreteElement implements Printable
{
    /**
        Basic constructor
    */
    public StringElement()
    {
    }
    
    /** 
        Basic constructor
    */
    public StringElement(String string)
    {
	if (string != null)
	    setTagText(string);
	else
	    setTagText("");
    }

    /** 
        Basic constructor
    */
    public StringElement(Element element)
    {
        addElement(element);
    }

    private StringElement append(String string)
    {
        setTagText(getTagText()+string);
        return this;
    }

    /** 
        Resets the interal string to be empty.
    */
    public StringElement reset()
    {
        setTagText("");
        return this;
    }

    /**
        Adds an Element to the element.
        @param  hashcode name of element for hash table
        @param  element Adds an Element to the element.
     */
    public StringElement addElement(String hashcode,Element element)
    {
        addElementToRegistry(hashcode,element);
        return(this);
    }

    /**
        Adds an Element to the element.
        @param  hashcode name of element for hash table
        @param  element Adds an Element to the element.
     */
    public StringElement addElement(String hashcode,String element)
    {
        // We do it this way so that filtering will work.
        // 1. create a new StringElement(element) - this is the only way that setTextTag will get called
        // 2. copy the filter state of this string element to this child.
        // 3. copy the filter for this string element to this child.

        StringElement se = new StringElement(element);
        se.setFilterState(getFilterState());
        se.setFilter(getFilter());
        addElementToRegistry(hashcode,se);
        return(this);
    }

    /**
        Adds an Element to the element.
        @param  element Adds an Element to the element.
     */
    public StringElement addElement(String element)
    {
        addElement(Integer.toString(element.hashCode()),element);
        return(this);
    }
    
    /**
        Adds an Element to the element.
        @param  element Adds an Element to the element.
     */
    public StringElement addElement(Element element)
    {
        addElementToRegistry(element);
        return(this);
    }
    
    /**
        Removes an Element from the element.
        @param hashcode the name of the element to be removed.
    */
    public StringElement removeElement(String hashcode)
    {
        removeElementFromRegistry(hashcode);
        return(this);
    }
    
    protected String createStartTag()
    {
        return("");
    }
    protected String createEndTag()
    {
        return("");
    }
}
