/*
 * Copyright (c) 2002-2026, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.galleryimage.service;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import fr.paris.lutece.plugins.galleryimage.web.ManageGalleryJspBean;
import fr.paris.lutece.plugins.galleryimage.web.ManageImageJspBean;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * The rights declared by galleryimage.xml are installed by the SQL scripts and given to the administrator
 */
public class GalleryImageRightsTest extends LuteceTestCase
{
    private static final String DESCRIPTOR = "webapp/WEB-INF/plugins/galleryimage.xml";
    private static final int ID_ADMIN = 1;

    /**
     * Each right of the descriptor exists in the database with its level and the administrator has it
     */
    @Test
    public void testDescriptorRightsAreInstalledAndGivenToTheAdministrator( ) throws Exception
    {
        Map<String, Integer> declared = readDescriptorRights( );
        assertFalse( "rights of the descriptor", declared.isEmpty( ) );

        for ( Map.Entry<String, Integer> entry : declared.entrySet( ) )
        {
            Right installed = RightHome.findByPrimaryKey( entry.getKey( ) );
            assertNotNull( "installed right " + entry.getKey( ), installed );
            assertEquals( "level of the right " + entry.getKey( ), entry.getValue( ).intValue( ), installed.getLevel( ) );
            assertTrue( "right " + entry.getKey( ) + " of the administrator",
                    AdminUserHome.getRightsListForUser( ID_ADMIN ).containsKey( entry.getKey( ) ) );
        }
    }

    /**
     * The right checked by the back office controllers is a right of the descriptor
     */
    @Test
    public void testControllerRightIsDeclared( ) throws Exception
    {
        Map<String, Integer> declared = readDescriptorRights( );
        assertTrue( "right of ManageGalleryJspBean", declared.containsKey( ManageGalleryJspBean.RIGHT_GALLERY_IMAGE_MANAGEMENT ) );
        assertTrue( "right of ManageImageJspBean", declared.containsKey( ManageImageJspBean.RIGHT_GALLERY_IMAGE_MANAGEMENT ) );
    }

    private static Map<String, Integer> readDescriptorRights( ) throws Exception
    {
        Map<String, Integer> rights = new LinkedHashMap<>( );
        NodeList features = DocumentBuilderFactory.newInstance( ).newDocumentBuilder( ).parse( new File( DESCRIPTOR ) )
                .getElementsByTagName( "admin-feature" );

        for ( int i = 0; i < features.getLength( ); i++ )
        {
            Element feature = (Element) features.item( i );
            rights.put( text( feature, "feature-id" ), Integer.valueOf( text( feature, "feature-level" ) ) );
        }

        return rights;
    }

    private static String text( Element element, String strTag )
    {
        return element.getElementsByTagName( strTag ).item( 0 ).getTextContent( ).trim( );
    }
}
