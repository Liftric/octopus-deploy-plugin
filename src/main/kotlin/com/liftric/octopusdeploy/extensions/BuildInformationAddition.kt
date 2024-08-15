package com.liftric.octopusdeploy.extensions

import com.liftric.octopusdeploy.api.CommitCli
import com.liftric.octopusdeploy.api.LinksCli
import com.liftric.octopusdeploy.api.WorkItem

data class BuildInformationCli(
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
    var WorkItems: List<WorkItem> = emptyList(),
    var Commits: List<CommitCli> = emptyList(),
    var IncompleteDataWarning: String? = null,
    var Created: String? = null,
    var LastModifiedOn: String? = null,
    var LastModifiedBy: String? = null,
    var Links: LinksCli? = null,
)
