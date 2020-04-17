package com.liftric.octopusdeploy.api

data class BuildInformation(
    var Id: String? = null,
    var PackageId: String? = null,
    var Version: String? = null,
    var BuildEnvironment: String? = null,
    var BuildNumber: String? = null,
    var BuildUrl: String? = null,
    var Branch: String? = null,
    var VcsType: String? = null,
    var VcsRoot: String? = null,
    var VcsCommitNumber: String? = null,
    var VcsCommitUrl: String? = null,
    var IssueTrackerName: String? = null,
    var WorkItems: List<WorkItem>? = null,
    var Commits: List<Commit>? = null,
    var IncompleteDataWarning: String? = null,
    var Created: String? = null,
    var LastModifiedOn: String? = null,
    var LastModifiedBy: String? = null,
    var Links: Links? = null
)
