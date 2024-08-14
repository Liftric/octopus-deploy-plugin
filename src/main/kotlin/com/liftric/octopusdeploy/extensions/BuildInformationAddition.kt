package com.liftric.octopusdeploy.extensions

import com.liftric.octopusdeploy.api.CommitCli
import com.liftric.octopusdeploy.api.LinksCli
import com.liftric.octopusdeploy.api.WorkItem
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional

data class BuildInformationCli(
    var Id: String?,
    var PackageId: String?,
    var Version: String?,
    var BuildEnvironment: String?,
    var BuildNumber: String?,
    var BuildUrl: String?,
    var Branch: String?,
    var VcsType: String?,
    var VcsRoot: String?,
    var VcsCommitNumber: String?,
    var VcsCommitUrl: String?,
    var IssueTrackerName: String?,
    var WorkItems: List<WorkItem>,
    var Commits: List<CommitCli>,
    var IncompleteDataWarning: String?,
    var Created: String?,
    var LastModifiedOn: String?,
    var LastModifiedBy: String?,
    var Links: LinksCli?,
)

@Suppress("MemberVisibilityCanBePrivate")
class BuildInformationAdditionBuilder(@get:Internal val proj: Project) {
    @get:Input
    @get:Optional
    val Id: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val PackageId: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val Version: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val BuildEnvironment: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val BuildNumber: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val BuildUrl: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val Branch: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val VcsType: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val VcsRoot: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val VcsCommitNumber: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val VcsCommitUrl: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val IssueTrackerName: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val WorkItems: ListProperty<WorkItem> = proj.objects.listProperty(WorkItem::class.java)

    @get:Input
    @get:Optional
    var Commits: ListProperty<CommitCli> = proj.objects.listProperty(CommitCli::class.java)

    @get:Input
    @get:Optional
    val IncompleteDataWarning: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val Created: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val LastModifiedOn: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val LastModifiedBy: Property<String?> = proj.objects.property(String::class.java)

    @get:Input
    @get:Optional
    val Links: Property<LinksCli?> = proj.objects.property(LinksCli::class.java)


    fun build(): BuildInformationCli = BuildInformationCli(
        Id = this.Id.orNull,
        PackageId = this.PackageId.orNull,
        Version = this.Version.orNull,
        BuildEnvironment = this.BuildEnvironment.orNull,
        BuildNumber = this.BuildNumber.orNull,
        BuildUrl = this.BuildUrl.orNull,
        Branch = this.Branch.orNull,
        VcsType = this.VcsType.orNull,
        VcsRoot = this.VcsRoot.orNull,
        VcsCommitNumber = this.VcsCommitNumber.orNull,
        VcsCommitUrl = this.VcsCommitUrl.orNull,
        IssueTrackerName = this.IssueTrackerName.orNull,
        WorkItems = this.WorkItems.getOrElse(listOf()),
        Commits = this.Commits.getOrElse(listOf()),
        IncompleteDataWarning = this.IncompleteDataWarning.orNull,
        Created = this.Created.orNull,
        LastModifiedOn = this.LastModifiedOn.orNull,
        LastModifiedBy = this.LastModifiedBy.orNull,
        Links = this.Links.orNull,
    )

    fun buildForGitlabCi(): BuildInformationCli = BuildInformationCli(
        Id = this.Id.orNull,
        PackageId = this.PackageId.orNull,
        Version = this.Version.orNull,
        BuildEnvironment = "GitLabCI",
        BuildNumber = System.getenv("CI_PIPELINE_IID"),
        BuildUrl = System.getenv("CI_PIPELINE_URL"),
        Branch = System.getenv("CI_COMMIT_REF_NAME"),
        VcsType = "Git",
        VcsRoot = System.getenv("CI_PROJECT_URL"),
        VcsCommitNumber = System.getenv("CI_COMMIT_SHORT_SHA"),
        VcsCommitUrl = "${System.getenv("CI_PROJECT_URL")?.removeSuffix("/")}/commit/${System.getenv("CI_COMMIT_SHA")}",
        IssueTrackerName = this.IssueTrackerName.orNull,
        WorkItems = this.WorkItems.getOrElse(listOf()),
        Commits = this.Commits.getOrElse(listOf()),
        IncompleteDataWarning = this.IncompleteDataWarning.orNull,
        Created = this.Created.orNull,
        LastModifiedOn = this.LastModifiedOn.orNull,
        LastModifiedBy = System.getenv("GITLAB_USER_NAME"),
        Links = this.Links.orNull,
    )
}