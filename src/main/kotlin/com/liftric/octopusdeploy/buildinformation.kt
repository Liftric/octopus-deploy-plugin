package com.liftric.octopusdeploy

import com.liftric.octopusdeploy.api.CommitCli
import com.liftric.octopusdeploy.api.WorkItem

fun parseCommitsForJira(
    commits: List<CommitCli>,
    jiraBaseBrowseUrl: String
): List<WorkItem> {
    val jiraIssues = commits
        .mapNotNull { it.Comment }
        .map { jiraKeyRegex.findAll(it).map { it.groupValues[1] }.toList() }
        .flatten()
        .toSet()
    println("parseCommitsForJira: found $jiraIssues")
    return jiraIssues.map {
        WorkItem(
            Id = it,
            LinkUrl = "${jiraBaseBrowseUrl.removeSuffix("/")}/$it",
            Description = "some placeholder text"
        )
    }
}

// from https://confluence.atlassian.com/stashkb/integrating-with-custom-jira-issue-key-313460921.html
private val jiraKeyRegex = Regex("((?<!([A-Z]{1,10})-?)[A-Z]+-\\d+)")
