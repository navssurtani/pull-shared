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

/**
 * An interface defining the basis of essential functionality that we have to provide any client that is going
 * to make use of a {@link org.jboss.pull.shared.connectors.common.Issue}
 *
 * @author navssurtani
 */
public interface Helper {

    /**
     * A method to search either JIRA or BZ for a given issue.
     *
     * @param issueId - BZ issue number or JIRA issue id. If you are searching for a BZ Bug,
     *                just pass the int value as a String.
     * @return - an {@link org.jboss.pull.shared.connectors.common.Issue} bean. So could be {@link org.jboss.pull
     * .shared.connectors.bugzilla.Bug} or a {@link org.jboss.pull.shared.connectors.jira.JiraIssue}.
     *
     * @throws IllegalArgumentException if the issue id is not of the correct format or no such issue
     * exists on either of the servers.
     */
    Issue findIssue (String issueId) throws IllegalArgumentException;

    /**
     * To update the status of a task. If a PR has been sent on Github, this can go and make the relevant update on
     * JIRA, and once it is merged, it can mark it as resolved.
     *
     * @param issueId - BZ issue number or JIRA issue id. If you are searching for a BZ Bug,
     *                just pass the int value as a String.
     *
     * @param newStatus - a String representation of the new status to be updated to. As a hacky implementation,
     *                  just make a toString() call on the {@link org.jboss.pull.shared.connectors.jira.JiraIssue
     *                  .IssueStatus} or {@link org.jboss.pull.shared.connectors.bugzilla.Bug.Status} values. Since
     *                  there is no consistency between JIRA and BZ nomenclature, the underlying implementation will
     *                  have to figure out what issue tracker to use based off of the issueId.
     *
     * @return - whether or not the status has been updated successfully
     *
     * @throws IllegalArgumentException if the issue id is not of the correct format,
     * no such issue exists or there is an incorrect new status string being passed.
     */
    boolean updateStatus (String issueId, String newStatus) throws IllegalArgumentException;

}
