package org.apache.velocity.test;

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights
 * reserved.
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
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Velocity", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

import java.io.*;

import junit.framework.*;

import org.apache.velocity.runtime.Runtime;
import org.apache.velocity.io.FastWriter;

/**
 * Base functionality to be extended by all Apache Velocity test cases.  Test 
 * case implementations are used to automatate testing via JUnit.
 *
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @version $Id: BaseTestCase.java,v 1.1 2000/10/15 03:21:41 dlr Exp $
 */
abstract class BaseTestCase extends TestCase
{
    /**
     * The properties file name of the application.
     */
    private static final String PROPS_FILE_NAME = "velocity.properties";

    /**
     * The writer used to output evaluated templates.
     */
    private FastWriter writer;

    /**
     * Creates a new instance.
     */
    public BaseTestCase (String name)
    {
        super(name);

        try
        {
            Runtime.init(PROPS_FILE_NAME);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    /**
     * Get the containing <code>TestSuite</code>.  This is always 
     * <code>VelocityTestSuite</code>.
     *
     * @return The <code>TestSuite</code> to run.
     */
    public static junit.framework.Test suite ()
    {
        return new VelocityTestSuite();
    }

    /**
     * Performs cleanup activities for this test case.
     */
    protected void tearDown ()
    {
        try
        {
            closeWriter();
        }
        catch (IOException e)
        {
            fail(e.getMessage());
        }
    }

    /**
     * Returns a <code>FastWriter</code> instance.
     *
     * @param out The output stream for the writer to write to.  If 
     *            <code>null</code>, defaults to <code>System.out</code>.
     * @return    The writer.
     */
    protected Writer getWriter (OutputStream out)
        throws UnsupportedEncodingException, IOException
    {
        if (writer == null)
        {
            if (out == null)
            {
                out = System.out;
            }

            writer = new FastWriter
                (out, Runtime.getString(Runtime.TEMPLATE_ENCODING));
            writer.setAsciiHack
                (Runtime.getBoolean(Runtime.TEMPLATE_ASCIIHACK));
        }
        return writer;
    }

    /**
     * Closes the writer (if it has been opened).
     */
    protected void closeWriter ()
        throws IOException
    {
        if (writer != null)
        {
            writer.flush();
            writer.close();
        }
    }
}
