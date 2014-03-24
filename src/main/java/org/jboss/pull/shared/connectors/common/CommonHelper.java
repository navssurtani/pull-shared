/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.pull.shared.connectors.common;

import com.atlassian.jira.rest.client.IssueRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;
import org.jboss.pull.shared.Util;
import org.jboss.pull.shared.connectors.bugzilla.Bugzilla;
import org.jboss.pull.shared.connectors.jira.JiraIssue;

import java.net.URI;
import java.util.Properties;

/**
 * An implementation of {@link org.jboss.pull.shared.connectors.common.Helper} that will be a singleton which will
 * delegate REST calls to either the BZ or JIRA connectors.
 *
 * This should be instantiated by the {@link org.jboss.pull.shared.connectors.common.CommonHelperFactory}
 *
 * @author navssurtani
 */
public class CommonHelper implements Helper {

    private Bugzilla bz = null;
    private IssueRestClient jira = null;

    private static String BUGZILLA_BASE = "https://bugzilla.redhat.com/";

    private static String BUGZILLA_LOGIN;
    private static String BUGZILLA_PASSWORD;
    private static String JIRA_LOGIN;
    private static String JIRA_PASSWORD;
    private static String JIRA_BASE_URL;

    CommonHelper(final String configurationFileProperty, final String configurationFileDefault) throws Exception {
        // Empty package-local constructor that should only be used by the respective factory.
        Properties fromUtil = null;
        try {
            fromUtil = Util.loadProperties(configurationFileProperty, configurationFileDefault);
        } catch (Exception e) {
            System.err.printf("Cannot initialize: %s\n", e);
            e.printStackTrace(System.err);
            throw e;
        }
        loadConfigurationStrings(fromUtil);

        // Now instantiate the clients.
        this.bz = new Bugzilla(BUGZILLA_BASE, BUGZILLA_LOGIN, BUGZILLA_PASSWORD);

        // Jira requires a little bit more work.
        JerseyJiraRestClientFactory jiraFactory = new JerseyJiraRestClientFactory();
        this.jira = jiraFactory.createWithBasicHttpAuthentication(URI.create(JIRA_BASE_URL), JIRA_LOGIN,
                JIRA_PASSWORD).getIssueClient();
    }

    @Override
    public Issue findIssue(String issueId) throws IllegalArgumentException {
        // TODO: We need to think of a nice and robust way to do this. Is matching patterns too expensive?
        // Try and parse the String as an integer. If it is successful, then we know we are dealing with a Bugzilla,
        // if not, we will attempt to search JIRA for it.
        try {
            int id = Integer.parseInt(issueId);
            // If we get to this line, we have successfully parsed the integer, so we have a BZ issue.
            return bz.getBug(id);
        } catch (NumberFormatException nfe) {
            // Let's try and get it from JIRA then.
            return new JiraIssue(jira.getIssue(issueId, new NullProgressMonitor()));
        }
    }

    @Override
    public boolean updateStatus(String issueId, String newStatus) throws IllegalArgumentException {
        return false;
    }

    private void loadConfigurationStrings(Properties fromUtil) {
        // The BZ part.
        BUGZILLA_LOGIN = Util.require(fromUtil, "bugzilla.login");
        BUGZILLA_PASSWORD = Util.require(fromUtil, "bugzilla.password");

        // The JIRA part
        JIRA_LOGIN = Util.require(fromUtil, "jira.login");
        JIRA_PASSWORD = Util.require(fromUtil, "jira.password");
        JIRA_BASE_URL = Util.require(fromUtil, "jira.base.url");
    }
}
