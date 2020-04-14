package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.shell
import junit.framework.TestCase
import java.io.File

fun File.setupGitRepo() {
    this.shell("git init .").exitCode mustBe 0
    this.shell("git add .").exitCode mustBe 0
    this.shell("git commit -m \"initial commit\"").exitCode mustBe 0
    this.shell("git tag first-one").exitCode mustBe 0
    this.shell("touch secondfile").exitCode mustBe 0
    this.shell("git add .").exitCode mustBe 0
    this.shell("git commit -m \"second commit\"").exitCode mustBe 0
    this.shell("git tag second-one").exitCode mustBe 0
}

infix fun Int.mustBe(expected: Int) {
    TestCase.assertEquals(expected, this)
}